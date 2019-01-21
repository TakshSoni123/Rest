package com.taksh.android.stockmanagementsystem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;

public class EditPartyList extends AppCompatActivity {

    ListView partyList;
    ArrayList<String> arrayList;

    SQLiteDatabase partyDatabase;
    Cursor cursor;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_party_list);

        partyList = (ListView)findViewById(R.id.listView);

        partyDatabase = openOrCreateDatabase(Constants.PARTY_LIST_DATBASE_NAME, MODE_PRIVATE,null);
        partyDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+Constants.PARTY_LIST_DATABASE_TABLE+"(name VARCHAR);");
        cursor = partyDatabase.rawQuery("SELECT * FROM "+Constants.PARTY_LIST_DATABASE_TABLE, null);
        arrayList = new ArrayList<String>();

        showList();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        partyList.setAdapter(arrayAdapter);

        partyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView)view;
                String name = text.getText().toString();
                Toast.makeText(EditPartyList.this,text.getText().toString(),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(EditPartyList.this,EditOrDeleteParty.class);
                intent.putExtra("party", name);
                partyDatabase.close();
                cursor.close();
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(EditPartyList.this,AddParty.class);
                partyDatabase.close();
                startActivity(intent);
                return (true);

        }
        return (super.onOptionsItemSelected(item));
    }

    public void showList(){
        if (cursor.moveToFirst()){
            do{
                String name = cursor.getString(0);
                arrayList.add(name);
            }while (cursor.moveToNext());
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditPartyList.this, EditQueryData.class);
        startActivity(intent);
    }
}
