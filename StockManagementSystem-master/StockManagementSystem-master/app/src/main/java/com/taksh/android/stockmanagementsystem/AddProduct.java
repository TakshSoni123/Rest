package com.taksh.android.stockmanagementsystem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddProduct extends AppCompatActivity {

    EditText productName;
    Button addVariant;
    String product;

    SQLiteDatabase productDatabase;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productName = (EditText)findViewById(R.id.productName);
        addVariant = (Button) findViewById(R.id.addVariants);



        productDatabase = openOrCreateDatabase(Constants.PRODUCT_DESCRIPTION_DATABASE_NAME, MODE_PRIVATE, null);
        productDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+Constants.PRODUCT_LIST_DATABASE_MAIN_TABLE+"(name VARCHAR);");

        addVariant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product = productName.getText().toString();
                if (product.equalsIgnoreCase("")){
                    productName.setError("Cannot be empty");
                    return;
                }
                productDatabase.execSQL("INSERT INTO "+Constants.PRODUCT_LIST_DATABASE_MAIN_TABLE+" VALUES('"+product+"');");
                Toast.makeText(AddProduct.this, "product created", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddProduct.this, EditOrDeleteProduct.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }
        });
    }
}
