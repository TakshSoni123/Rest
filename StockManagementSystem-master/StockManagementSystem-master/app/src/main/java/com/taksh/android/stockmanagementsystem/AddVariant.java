package com.taksh.android.stockmanagementsystem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddVariant extends AppCompatActivity {

    TextView productName;

    EditText variantName;

    CheckBox checkBox6;
    CheckBox checkBox7;
    CheckBox checkBox8;
    CheckBox checkBox9;
    CheckBox checkBox10;
    CheckBox checkBox11;
    CheckBox checkBox12;
    CheckBox checkBox13;
    CheckBox checkBox14;
    CheckBox checkBox15;
    CheckBox checkBox16;
    CheckBox checkBox17;
    CheckBox checkBox18;
    CheckBox checkBox19;
    CheckBox checkBox20;
    CheckBox checkBox21;
    CheckBox checkBox22;
    CheckBox checkBox23;
    CheckBox checkBox24;

    Button save;

    String variant;
    ArrayList<String> sizes;

    SQLiteDatabase database;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_variant);
        productName = (TextView)findViewById(R.id.productName);
        variantName = (EditText) findViewById(R.id.variantName);

        checkBox6 = (CheckBox)findViewById(R.id.checkBox6);
        checkBox7 = (CheckBox)findViewById(R.id.checkBox7);
        checkBox8 = (CheckBox)findViewById(R.id.checkBox8);
        checkBox9 = (CheckBox)findViewById(R.id.checkBox9);
        checkBox10 = (CheckBox)findViewById(R.id.checkBox10);
        checkBox11 = (CheckBox)findViewById(R.id.checkBox11);
        checkBox12 = (CheckBox)findViewById(R.id.checkBox12);
        checkBox13 = (CheckBox)findViewById(R.id.checkBox13);
        checkBox14 = (CheckBox)findViewById(R.id.checkBox14);
        checkBox15 = (CheckBox)findViewById(R.id.checkBox15);
        checkBox16 = (CheckBox)findViewById(R.id.checkBox16);
        checkBox17 = (CheckBox)findViewById(R.id.checkBox17);
        checkBox18 = (CheckBox)findViewById(R.id.checkBox18);
        checkBox19 = (CheckBox)findViewById(R.id.checkBox19);
        checkBox20 = (CheckBox)findViewById(R.id.checkBox20);
        checkBox21 = (CheckBox)findViewById(R.id.checkBox21);
        checkBox22 = (CheckBox)findViewById(R.id.checkBox22);
        checkBox23 = (CheckBox)findViewById(R.id.checkBox23);
        checkBox24 = (CheckBox)findViewById(R.id.checkBox24);

        save = (Button)findViewById(R.id.save);

        if (getIntent().getStringExtra("variant")!=null){
            variantName.setText(getIntent().getStringExtra("variant"));
        }

        final String product = getIntent().getStringExtra("product");
        productName.setText(product);

        database = openOrCreateDatabase(Constants.PRODUCT_DESCRIPTION_DATABASE_NAME, MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS "+product.replaceAll("\\s","")+"(variant VARCHAR, sizes VARCHAR);");
        cursor = database.rawQuery("SELECT * FROM "+product.replaceAll("\\s",""), null);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (variantName.getText().toString().trim().equalsIgnoreCase("")){
                    variantName.setError("Cannot be empty");
                    return;
                }
                if (!checkBoxCheck()){
                    Toast.makeText(AddVariant.this, "YOU HAVE TO SELECT ATLEAST ONE SIZE",Toast.LENGTH_SHORT).show();
                    return;
                }
                variant = variantName.getText().toString();
                sizes = getSizeString();

                database.execSQL(("INSERT INTO "+product.replaceAll("\\s","")+" VALUES('"+variant+"','"+sizes+"');"));
                Toast.makeText(AddVariant.this,"Variant added",Toast.LENGTH_SHORT).show();

                database.close();
                Intent intent = new Intent(AddVariant.this, EditOrDeleteProduct.class);
                intent.putExtra("product",productName.getText().toString());
                startActivity(intent);
            }
        });

    }

    public boolean checkBoxCheck(){

        if (checkBox6.isChecked()||checkBox7.isChecked()||checkBox8.isChecked()||checkBox9.isChecked()||checkBox10.isChecked()||checkBox11.isChecked()||
                checkBox12.isChecked()||checkBox13.isChecked()||checkBox14.isChecked()||checkBox15.isChecked()||checkBox16.isChecked()||checkBox17.isChecked()||
                checkBox18.isChecked()||checkBox19.isChecked()||checkBox20.isChecked()||checkBox21.isChecked()||checkBox22.isChecked()||checkBox23.isChecked()||checkBox24.isChecked()){
            return true;
        }

        return false;
    }

    public ArrayList<String> getSizeString(){
        ArrayList<String> size = new ArrayList<>();
        if (checkBox6.isChecked()){
            size.add("6");
        }
        if (checkBox7.isChecked()){
            size.add("7");
        }
        if (checkBox8.isChecked()){
            size.add("8");
        }if (checkBox9.isChecked()){
            size.add("9");
        }
        if (checkBox10.isChecked()){
            size.add("10");
        }
        if (checkBox11.isChecked()){
            size.add("11");
        }
        if (checkBox12.isChecked()){
            size.add("12");
        }if (checkBox13.isChecked()){
            size.add("13");
        }if (checkBox14.isChecked()){
            size.add("14");
        }if (checkBox15.isChecked()){
            size.add("15");
        }if (checkBox16.isChecked()){
            size.add("16");
        }if (checkBox17.isChecked()){
            size.add("17");
        }if (checkBox18.isChecked()){
            size.add("18");
        }if (checkBox19.isChecked()){
            size.add("19");
        }if (checkBox20.isChecked()){
            size.add("20");
        }if (checkBox21.isChecked()){
            size.add("21");
        }if (checkBox22.isChecked()){
            size.add("22");
        }if (checkBox23.isChecked()){
            size.add("23");
        }if (checkBox24.isChecked()){
            size.add("24");
        }

        return size;
    }

}
