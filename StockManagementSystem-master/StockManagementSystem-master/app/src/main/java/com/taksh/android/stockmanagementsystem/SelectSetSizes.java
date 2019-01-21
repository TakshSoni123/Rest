package com.taksh.android.stockmanagementsystem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import maes.tech.intentanim.CustomIntent;

public class SelectSetSizes extends AppCompatActivity {

    ListView listView;
    Button confirmButton;
    Intent intent;
    int[] setSizes = new int[10];
    Bundle bundle;
    QueryDetails queryDetails;
    SQLiteDatabase database;
    Cursor cursor;

    ArrayList<String> variantSizes;
    String[] sizes = {"1","2","3","4","5","6","7","8","9","10"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_set_sizes);

        intent = getIntent();
        bundle = intent.getExtras();
        queryDetails = (QueryDetails)bundle.getSerializable(Constants.BUNDLE_KEY);

        variantSizes = new ArrayList<>();
        database = openOrCreateDatabase(Constants.PRODUCT_DESCRIPTION_DATABASE_NAME, MODE_PRIVATE, null);

        String productName = queryDetails.productName;

        cursor = database.rawQuery("SELECT * FROM "+productName.replaceAll("\\s",""), null);

        init();

        for (int i=0;i<10;i++){
            setSizes[i]=0;
        }

        listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,variantSizes);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView)view;
                String size = text.getText().toString();

                Toast.makeText(SelectSetSizes.this,size,Toast.LENGTH_SHORT).show();
               if (setSizes[position]==0) {
                    view.setBackgroundColor(Color.GREEN);
                    setSizes[position]=1;
               }
               else {
                   view.setBackgroundColor(Color.WHITE);
                   setSizes[position] = 0;
               }
            }
        });

        confirmButton = (Button)findViewById(R.id.confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryDetails.setSizes=setSizes;
                bundle.putSerializable(Constants.BUNDLE_KEY,queryDetails);

                Intent intent = new Intent(SelectSetSizes.this,SelectNumberOfSets.class);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                CustomIntent.customType(SelectSetSizes.this,"fadein-to-fadeout");
            }
        });
    }

    void init(){
        cursor.moveToPosition(Integer.parseInt(getIntent().getStringExtra("position")));

        String sizes = cursor.getString(1);

        for (int i=0;i<sizes.length();i++){
            if (sizes.charAt(i)>=48 && sizes.charAt(i)<=57){
                if (sizes.charAt(i+1)>=48 && sizes.charAt(i+1)<=57){
                    String s = sizes.charAt(i)+""+sizes.charAt(i+1);
                    variantSizes.add(s);
                    i++;
                }
                else {
                    String s1= sizes.charAt(i)+"";
                    variantSizes.add(s1);
                }
            }
        }
    }
}
