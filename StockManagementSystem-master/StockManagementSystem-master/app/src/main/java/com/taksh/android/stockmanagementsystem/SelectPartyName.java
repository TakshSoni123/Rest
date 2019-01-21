package com.taksh.android.stockmanagementsystem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.session.PlaybackState;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import maes.tech.intentanim.CustomIntent;


public class SelectPartyName extends AppCompatActivity {
    ListView listView;
   ArrayList<String> partyNames;

    QueryDetails queryDetails;
    Bundle bundle;

    SQLiteDatabase partyDatabase;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_party_name);

        queryDetails = new QueryDetails();
        bundle = new Bundle();

        partyNames = new ArrayList<String>();

        partyDatabase = openOrCreateDatabase(Constants.PARTY_LIST_DATBASE_NAME,MODE_PRIVATE,null);
        partyDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+Constants.PARTY_LIST_DATABASE_TABLE+"(name VARCHAR);");
        cursor = partyDatabase.rawQuery("SELECT * FROM "+Constants.PARTY_LIST_DATABASE_TABLE, null);

        initList();

        listView = (ListView)findViewById(R.id.listView);

        Collections.sort(partyNames, new Comparator<String>() {
            public int compare(String v1, String v2) {
                return v1.compareTo(v2);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,partyNames);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView)view;
                Toast.makeText(SelectPartyName.this,"You Selected "+text.getText(),Toast.LENGTH_SHORT).show();

                queryDetails.partyName = text.getText().toString();

                bundle.putSerializable(Constants.BUNDLE_KEY,queryDetails);

                Intent intent = new Intent(SelectPartyName.this, SelectProdctType.class);
                intent.putExtras(bundle);
                partyDatabase.close();
                startActivity(intent);
                CustomIntent.customType(SelectPartyName.this,"fadein-to-fadeout");
            }
        });
    }

    public void initList(){

        if (cursor.moveToFirst()){
            do {
                String name = cursor.getString(0);
                partyNames.add(name);
            }while (cursor.moveToNext());
        }


    }
}