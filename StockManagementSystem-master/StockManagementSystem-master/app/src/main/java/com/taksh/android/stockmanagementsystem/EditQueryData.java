package com.taksh.android.stockmanagementsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EditQueryData extends AppCompatActivity {

    Button editParty;
    Button editProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_query_data);

        editParty = (Button)findViewById(R.id.party);
        editProduct = (Button)findViewById(R.id.product);

        editParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditQueryData.this, EditPartyList.class);
                startActivity(intent);
            }
        });

        editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditQueryData.this, EditProductDescription.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditQueryData.this, MainActivity.class);
        startActivity(intent);
    }
}
