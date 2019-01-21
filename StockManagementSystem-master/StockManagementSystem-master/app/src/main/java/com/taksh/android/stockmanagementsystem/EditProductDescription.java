package com.taksh.android.stockmanagementsystem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditProductDescription extends AppCompatActivity {

    ListView listView;

    ArrayList<String> arrayList;

    SQLiteDatabase productDatabase;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product_description);

        listView = (ListView)findViewById(R.id.listView);

        productDatabase = openOrCreateDatabase(Constants.PRODUCT_DESCRIPTION_DATABASE_NAME, MODE_PRIVATE,null);
        productDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+Constants.PRODUCT_LIST_DATABASE_MAIN_TABLE+"(name VARCHAR);");
        cursor = productDatabase.rawQuery("SELECT * FROM "+Constants.PRODUCT_LIST_DATABASE_MAIN_TABLE, null);

        arrayList = new ArrayList<String>();
        initList();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView)view;
                String name = text.getText().toString();
                Toast.makeText(EditProductDescription.this,name,Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(EditProductDescription.this,EditOrDeleteProduct.class);
                intent.putExtra("product", name);
                productDatabase.close();
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
                Intent intent = new Intent(EditProductDescription.this,AddProduct.class);
                startActivity(intent);
                return (true);

        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditProductDescription.this, EditQueryData.class);
        startActivity(intent);
    }

    public void initList(){
        if (cursor.moveToFirst()){
            do {
                String name = cursor.getString(0);
                arrayList.add(name);
            }while (cursor.moveToNext());
        }
    }
}
