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

public class SelectSubProduct extends AppCompatActivity {

    ListView listView;
    Intent intent;
    Bundle bundle;
    String productName;

    QueryDetails queryDetails;

    ArrayList<String> variantNames;

    SQLiteDatabase database;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sub_product);

        intent = getIntent();
        bundle = intent.getExtras();

        variantNames = new ArrayList<>();

        queryDetails = (QueryDetails)bundle.getSerializable(Constants.BUNDLE_KEY);
        productName = queryDetails.productName;

        database = openOrCreateDatabase(Constants.PRODUCT_DESCRIPTION_DATABASE_NAME, MODE_PRIVATE, null);
        cursor = database.rawQuery("SELECT * FROM "+productName.replaceAll("\\s",""),null);

        init();

        listView = (ListView)findViewById(R.id.listView);

        Collections.sort(variantNames, new Comparator<String>() {
            public int compare(String v1, String v2) {
                return v1.compareTo(v2);
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,variantNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView)view;
                Toast.makeText(SelectSubProduct.this,text.getText(),Toast.LENGTH_SHORT).show();
                queryDetails.subProduct = text.getText().toString();
                bundle.putSerializable(Constants.BUNDLE_KEY,queryDetails);

                intent = new Intent(SelectSubProduct.this,SelectSetSizes.class);
                intent.putExtra("position",String.valueOf(position));
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                CustomIntent.customType(SelectSubProduct.this,"fadein-to-fadeout");
            }
        });
    }

    void init(){
        if (cursor.moveToFirst()){
            do {
                variantNames.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
    }
}
