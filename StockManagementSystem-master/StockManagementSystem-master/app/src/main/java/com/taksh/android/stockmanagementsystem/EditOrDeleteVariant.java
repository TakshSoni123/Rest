package com.taksh.android.stockmanagementsystem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Blob;
import java.util.ArrayList;

public class EditOrDeleteVariant extends AppCompatActivity {

    TextView productName;

    EditText variantName;

    ArrayList<String> size;

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
    Button delete;

    String position;
    SQLiteDatabase database;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_delete_variant);

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
        delete = (Button)findViewById(R.id.delete);

        size = new ArrayList<>();

        position = getIntent().getStringExtra("position");

        database = openOrCreateDatabase(Constants.PRODUCT_DESCRIPTION_DATABASE_NAME,MODE_PRIVATE, null);
        //database.delete(productName.getText().toString().replaceAll("\\s",""), null, null);
        init();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeSizes();

                database.execSQL("DELETE FROM "+productName.getText().toString().replaceAll("\\s","")+" WHERE name = '"+getIntent().getStringExtra("variant")+"';");
                database.execSQL(("INSERT INTO "+productName.getText().toString().replaceAll("\\s","")+" VALUES('"+variantName.getText().toString()+"','"+size.toString()+"');"));
                Toast.makeText(EditOrDeleteVariant.this,"Variant Saved "+size ,Toast.LENGTH_SHORT).show();
                database.close();
                Intent intent = new Intent(EditOrDeleteVariant.this,EditOrDeleteProduct.class);
                intent.putExtra("product",productName.getText().toString());
                startActivity(intent);

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.execSQL("DELETE FROM "+productName.getText().toString().replaceAll("\\s","")+" WHERE name = '"+getIntent().getStringExtra("variant")+"';");
                Toast.makeText(EditOrDeleteVariant.this,"Variant Deleted",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditOrDeleteVariant.this,EditOrDeleteProduct.class);
                database.close();
                intent.putExtra("product",productName.getText().toString());
                startActivity(intent);
            }
        });

    }

    public void init(){
        String variant = getIntent().getStringExtra("variant");
        String product = getIntent().getStringExtra("product");

        variantName.setText(variant);
        productName.setText(product);

        database.execSQL("CREATE TABLE IF NOT EXISTS "+product.replaceAll("\\s","")+"(name VARCHAR, sizes VARCHAR);");
        cursor = database.rawQuery("SELECT * FROM "+product.replaceAll("\\s",""),null);
        if (cursor.moveToPosition(Integer.parseInt(position))) {
            String sizes = cursor.getString(1);
            selectCheckBoxes(sizes);

        }



    }

    void selectCheckBoxes(String sizes){

        for (int i=0;i<sizes.length();i++){
            if (sizes.charAt(i)>=48 && sizes.charAt(i)<=57){
                if (sizes.charAt(i+1)>=48 && sizes.charAt(i+1)<=57){
                    String s = sizes.charAt(i)+""+sizes.charAt(i+1);
                    size.add(s);
                    i++;
                }
                else {
                    String s1= sizes.charAt(i)+"";
                    size.add(s1);
                }
            }
        }
        Toast.makeText(EditOrDeleteVariant.this,sizes,Toast.LENGTH_SHORT).show();


        if (size.contains("6")){
            checkBox6.setChecked(true);
        }
        if (size.contains("7")){
            checkBox7.setChecked(true);
        }
        if (size.contains("8")){
            checkBox8.setChecked(true);
        }
        if (size.contains("9")){
            checkBox9.setChecked(true);
        }
        if (size.contains("10")){
            checkBox10.setChecked(true);
        }if (size.contains("11")){
            checkBox11.setChecked(true);
        }if (size.contains("12")){
            checkBox12.setChecked(true);
        }if (size.contains("13")){
            checkBox13.setChecked(true);
        }if (size.contains("14")){
            checkBox14.setChecked(true);
        }if (size.contains("15")){
            checkBox15.setChecked(true);
        }if (size.contains("16")){
            checkBox16.setChecked(true);
        }if (size.contains("17")){
            checkBox17.setChecked(true);
        }if (size.contains("18")){
            checkBox18.setChecked(true);
        }if (size.contains("19")){
            checkBox19.setChecked(true);
        }if (size.contains("20")){
            checkBox20.setChecked(true);
        }if (size.contains("21")){
            checkBox21.setChecked(true);
        }if (size.contains("22")){
            checkBox22.setChecked(true);
        }if (size.contains("23")){
            checkBox23.setChecked(true);
        }if (size.contains("24")){
            checkBox24.setChecked(true);
        }
    }

    void changeSizes(){
        size.clear();
        if (checkBox6.isChecked()){
            size.add("6");
        }if (checkBox7.isChecked()){
            size.add("7");
        }if (checkBox8.isChecked()){
            size.add("8");
        }if (checkBox9.isChecked()){
            size.add("9");
        }if (checkBox10.isChecked()){
            size.add("10");
        }if (checkBox11.isChecked()){
            size.add("11");
        }if (checkBox12.isChecked()){
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
    }
}
