package com.taksh.android.stockmanagementsystem;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ajts.androidmads.library.SQLiteToExcel;

import java.io.File;
import java.util.Date;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import maes.tech.intentanim.CustomIntent;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ShowPreview extends AppCompatActivity {

    String date;

    TextView dateText;
    TextView partyName;
    TextView productName;
    TextView subProduct;
    TextView weight;
    TextView totalSets;
    Intent intent;
    Bundle bundle;
    QueryDetails queryDetails;

    Cursor cursor;

    Button saveButton;
    Button saveAndExportButton;
    int setSizes[];
    SQLiteDatabase database;
    String path = Environment.getExternalStorageDirectory().getPath();

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_preview);
        database = openOrCreateDatabase(Constants.MAIN_DATABASE_NAME,MODE_PRIVATE,null);
       // database.execSQL("DROP TABLE databaseTable;");
        database.execSQL("CREATE TABLE IF NOT EXISTS "+Constants.MAIN_DATABASE_TABLE+"(date VARCHAR, partyName VARCHAR, productName VARCHAR, subProduct VARCHAR, setSizes VARCHAR, totalsets VARCHAR, weight VARCHAR );");
        cursor = database.rawQuery("SELECT * FROM "+Constants.MAIN_DATABASE_TABLE,null);


        date = getDate();

        intent = getIntent();
        bundle = intent.getExtras();
        queryDetails=(QueryDetails)bundle.getSerializable(Constants.BUNDLE_KEY);

        dateText =(TextView)findViewById(R.id.date);
        partyName =(TextView)findViewById(R.id.partyName);
        productName =(TextView)findViewById(R.id.productName);
        subProduct =(TextView)findViewById(R.id.subProduct);
        totalSets = (TextView)findViewById(R.id.totalSets);
        weight = (TextView)findViewById(R.id.weight);

        saveButton = (Button) findViewById(R.id.save);
        saveAndExportButton = (Button)findViewById(R.id.saveExport);

        setSizes = queryDetails.setSizes;
        dateText.setText(date);
        partyName.setText(queryDetails.partyName);
        productName.setText(queryDetails.productName);
        subProduct.setText(queryDetails.subProduct);
        totalSets.setText(String.valueOf(queryDetails.totalNumberOfSets));
        weight.setText(String.valueOf(queryDetails.totalWeight));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add alert confirmation
                save();
                database.close();
                Intent intent = new Intent(ShowPreview.this, MainActivity.class);
                startActivity(intent);
                CustomIntent.customType(ShowPreview.this,"up-to-bottom");
            }
        });

        saveAndExportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add alert confirmation
                save();
                exportToExcel();
                database.close();
                Intent intent = new Intent(ShowPreview.this, MainActivity.class);
                startActivity(intent);
                CustomIntent.customType(ShowPreview.this,"up-to-bottom");
            }
        });

    }

    public void save(){
        String sizes="";
        int size = 0;
        for (int i=0;i<setSizes.length;i++){
            int p=setSizes[i];
            if (p==1){
                size++;
                sizes = sizes + String.valueOf(i+1) +",";
            }
        }
        String finalSizes = sizes.substring(0,sizes.length()-1);
        String insert = "INSERT INTO databaseTable VALUES('"+date+"','"+queryDetails.partyName+"','"+queryDetails.productName+"','"+queryDetails.subProduct+"','"+ finalSizes+"','"+String.valueOf(totalSets.getText())+"','"+String.valueOf(weight.getText())+"');";
        database.execSQL(insert);
    }

    public void exportToExcel(){
        File sd = Environment.getExternalStorageDirectory();
        String excelFileName = Constants.MAIN_EXCEL_FILENAME;

        File directory = new File(sd.getAbsolutePath());
        if (!directory.isDirectory()){
            directory.mkdirs();
        }
        try {

            if (!checkPermission()) {
                File excelFile = new File(directory, excelFileName);
                WorkbookSettings workbookSettings = new WorkbookSettings();
                workbookSettings.setLocale(new Locale("en", "EN"));
                WritableWorkbook workbook = Workbook.createWorkbook(excelFile, workbookSettings);
                WritableSheet sheet = workbook.createSheet("tempDatabase", 0);

                sheet.addCell(new Label(0, 0, "Date"));
                sheet.addCell(new Label(1, 0, "Party Name"));
                sheet.addCell(new Label(2, 0, "Product Name"));
                sheet.addCell(new Label(3, 0, "Variant"));
                sheet.addCell(new Label(4, 0, "Set Sizes"));
                sheet.addCell(new Label(5, 0, "Total Sets"));
                sheet.addCell(new Label(6, 0, "Total weight"));


                if (cursor.moveToFirst()) {
                    do {
                        String date = cursor.getString(0);
                        String partyName = cursor.getString(1);
                        String productName = cursor.getString(2);
                        String subProductName = cursor.getString(3);
                        String setSizes = cursor.getString(4);
                        String totalSets = cursor.getString(5);
                        String weight = cursor.getString(6);

                        int i = cursor.getPosition() + 1;
                        sheet.addCell(new Label(0, i, date));
                        sheet.addCell(new Label(1, i, partyName));
                        sheet.addCell(new Label(2, i, productName));
                        sheet.addCell(new Label(3, i, subProductName));
                        sheet.addCell(new Label(4, i, setSizes));
                        sheet.addCell(new Label(5, i, totalSets));
                        sheet.addCell(new Label(6,i,weight));

                    } while (cursor.moveToNext());
                }

                cursor.close();
                workbook.write();
                workbook.close();
                Toast.makeText(getApplication(),
                        "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
            }
            else {
                requestPermissionAndContinue();
            }
        }
        catch (Exception e){
            Toast.makeText(ShowPreview.this,"Some error occured while converting to excel",Toast.LENGTH_SHORT).show();
            Log.d("big Exception", e.getMessage());
        }
    }


    private boolean checkPermission() {

        return ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ;
    }

    private void requestPermissionAndContinue() {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle(getString(R.string.permission_necessary));
                alertBuilder.setMessage(R.string.storage_permission_is_encessary_to_wrote_event);
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(ShowPreview.this, new String[]{WRITE_EXTERNAL_STORAGE
                                , READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
                Log.e("", "permission denied, show dialog");
            } else {
                ActivityCompat.requestPermissions(ShowPreview.this, new String[]{WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }
        else{
            exportToExcel();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {

                boolean flag = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }
                if (flag) {
                    exportToExcel();
                } else {
                    finish();
                }

            } else {
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    String getDate(){
        Date newDate = new Date();
        Log.d("YOOOOOOOO",newDate.toString());
        String[] x = newDate.toString().split(" ");
        return x[2]+" "+x[1]+" "+x[5];
    }

}
