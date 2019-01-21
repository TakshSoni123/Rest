package com.taksh.android.stockmanagementsystem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import maes.tech.intentanim.CustomIntent;

public class SelectProdctType extends AppCompatActivity {

    Intent intent;
    Bundle bundle;
    ListView listView;

    QueryDetails s;

    SQLiteDatabase productDatabase;
    Cursor cursor;
    ArrayList<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_prodct_type);

        intent = getIntent();
        bundle = intent.getExtras();
        s = (QueryDetails)bundle.getSerializable(Constants.BUNDLE_KEY);

        names = new ArrayList<>();

        productDatabase = openOrCreateDatabase(Constants.PRODUCT_DESCRIPTION_DATABASE_NAME, MODE_PRIVATE, null);
        cursor = productDatabase.rawQuery("SELECT * FROM "+Constants.PRODUCT_LIST_DATABASE_MAIN_TABLE,null);

        init();

        listView = (ListView)findViewById(R.id.listView);

        Collections.sort(names, new Comparator<String>() {
            public int compare(String v1, String v2) {
                return v1.compareTo(v2);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView)view;

                s.productName=text.getText().toString();

                bundle.putSerializable(Constants.BUNDLE_KEY,s);

                Toast.makeText(SelectProdctType.this,"You Selected "+text.getText(),Toast.LENGTH_SHORT).show();

                intent = new Intent(SelectProdctType.this, SelectSubProduct.class);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                CustomIntent.customType(SelectProdctType.this,"fadein-to-fadeout");
            }
        });
    }

    void init(){
        if (cursor.moveToFirst()){
            do {
                String name = cursor.getString(0);
                names.add(name);
            }while (cursor.moveToNext());
        }
    }
}
