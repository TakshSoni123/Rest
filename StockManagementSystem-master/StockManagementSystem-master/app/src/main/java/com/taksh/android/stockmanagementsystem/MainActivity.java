package com.taksh.android.stockmanagementsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity {

    Button newQuery;
    Button customExcel;
    Button editData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newQuery = (Button)findViewById(R.id.newQuery);
        customExcel = (Button)findViewById(R.id.customExcel);
        editData = (Button)findViewById(R.id.edit);

        newQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SelectPartyName.class);
                startActivity(intent);
                CustomIntent.customType(MainActivity.this,"bottom-to-up");
            }
        });

        customExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MakeCustomExcel.class);
                startActivity(intent);
                CustomIntent.customType(MainActivity.this,"bottom-to-up");
            }
        });

        editData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EditQueryData.class);
                startActivity(intent);
                CustomIntent.customType(MainActivity.this,"bottom-to-up");

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}
