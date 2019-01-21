package com.taksh.android.stockmanagementsystem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class EditOrDeleteProduct extends AppCompatActivity {

    EditText productName;
    Button save;
    Button delete;
    Button addVariant;
    ListView variantList;

    ArrayList<String> variants;

    SQLiteDatabase productDatabase;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_delete_product);

        productName = (EditText)findViewById(R.id.productName);
        save = (Button)findViewById(R.id.save);
        delete = (Button)findViewById(R.id.delete);
        addVariant = (Button)findViewById(R.id.addVariants);
        variantList = (ListView)findViewById(R.id.listView);
        variants = new ArrayList<>();

        variants = init();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, variants);
        variantList.setAdapter(arrayAdapter);

        final String product = getIntent().getStringExtra("product");

        productName.setText(getIntent().getStringExtra("product"));

        productDatabase = openOrCreateDatabase(Constants.PRODUCT_DESCRIPTION_DATABASE_NAME, MODE_PRIVATE,null);
        productDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+Constants.PRODUCT_LIST_DATABASE_MAIN_TABLE+"(name VARCHAR);");
        cursor = productDatabase.rawQuery("SELECT * FROM "+Constants.PRODUCT_LIST_DATABASE_MAIN_TABLE, null);

        variantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView text = (TextView)view;
                String variant = text.getText().toString();

                Intent intent = new Intent(EditOrDeleteProduct.this, EditOrDeleteVariant.class);
                intent.putExtra("product", product);
                intent.putExtra("variant", variant);
                intent.putExtra("position", String.valueOf(position));
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newProductName = productName.getText().toString();
                if (!newProductName.equalsIgnoreCase(product)) {
                    productDatabase.execSQL("DELETE FROM " + Constants.PRODUCT_LIST_DATABASE_MAIN_TABLE + " WHERE name = '" + product + "';");
                    productDatabase.execSQL("INSERT INTO " + Constants.PRODUCT_LIST_DATABASE_MAIN_TABLE + " VALUES('" + newProductName + "')");
                    productDatabase.execSQL("ALTER TABLE " + product.replaceAll("\\s", "") + " RENAME TO " + newProductName.replaceAll("\\s", ""));
                }
                Intent intent = new Intent(EditOrDeleteProduct.this,EditProductDescription.class);
                productDatabase.close();
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDatabase.execSQL("DELETE FROM "+Constants.PRODUCT_LIST_DATABASE_MAIN_TABLE+" WHERE name = '"+product+"';");
                productDatabase.execSQL("DROP TABLE IF EXISTS "+product.replaceAll("\\s","")+";");
                productDatabase.close();
                Intent intent = new Intent(EditOrDeleteProduct.this,EditProductDescription.class);
                startActivity(intent);
            }
        });

        addVariant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDatabase.close();
                Intent intent = new Intent(EditOrDeleteProduct.this, AddVariant.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }
        });
    }

    public ArrayList<String> init(){
        ArrayList<String> names = new ArrayList();

        productDatabase = openOrCreateDatabase(Constants.PRODUCT_DESCRIPTION_DATABASE_NAME, MODE_PRIVATE,null);
        productDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+getIntent().getStringExtra("product").replaceAll("\\s","")+"(name VARCHAR, sizes VARCHAR);");
        cursor = productDatabase.rawQuery("SELECT * FROM "+getIntent().getStringExtra("product").replaceAll("\\s",""), null);

        if (cursor.moveToFirst()){
            do {
            names.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        productDatabase.close();
        return names;
    }
}
