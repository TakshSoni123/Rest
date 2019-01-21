package com.taksh.android.stockmanagementsystem;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MakeCustomExcel extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText startDate;
    EditText endDate;
    Button confirm;
    String start;
    String end;

    DatePickerDialog startDateDialog;

    int selectEditText=0;

    SQLiteDatabase database;
    String path = Environment.getExternalStorageDirectory().getPath();
    Cursor cursor;


    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_custom_excel);

        confirm = (Button)findViewById(R.id.confirm);
        startDate = (EditText)findViewById(R.id.startDate);
        endDate = (EditText)findViewById(R.id.endDate);
        startDateDialog = new DatePickerDialog(MakeCustomExcel.this, MakeCustomExcel.this, Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DATE));

        database = openOrCreateDatabase(Constants.MAIN_DATABASE_NAME,MODE_PRIVATE,null);
        cursor = database.rawQuery("SELECT * FROM "+Constants.MAIN_DATABASE_TABLE, null);


        //automatically move to next editText
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) MakeCustomExcel.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(MakeCustomExcel.this.getCurrentFocus().getWindowToken(), 0);
                selectEditText = 0;
                startDateDialog.show();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) MakeCustomExcel.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(MakeCustomExcel.this.getCurrentFocus().getWindowToken(), 0);
                selectEditText = 1;
                startDateDialog.show();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startDate.getText().toString().trim().equalsIgnoreCase("")){
                   startDate.setError("Select a date");
                   return;
                }
                if (endDate.getText().toString().trim().equalsIgnoreCase("")){
                    endDate.setError("Select a date");
                    return;
                }

                try {
                    createCustomExcel();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Toast.makeText(MakeCustomExcel.this,view.toString(),Toast.LENGTH_SHORT).show();
        if (selectEditText==0) {
            startDate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
            start = startDate.getText().toString();
        }

        //put error if end date is before start date
        else if (selectEditText==1){
            endDate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
            end = endDate.getText().toString();
        }

    }

    public void createCustomExcel() throws ParseException, WriteException, IOException {

        File sd = Environment.getExternalStorageDirectory();
        String fileName = start+"-TO-"+end+".xls";
        Log.d("FILENAMEEE", fileName);

        File directory = new File(sd.getAbsolutePath());
        if (!directory.isDirectory()){
            directory.mkdirs();
        }
        try {
            if (!checkPermission()) {
                File excelFile = new File(directory, fileName);
                if (excelFile.exists()){
                    excelFile.delete();
                    excelFile.createNewFile();
                }
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
                Log.d("TESTTTT","WORKBOOK MADE");
                int i=1;
                if (cursor.moveToFirst()) {
                    Log.d("TESTTTTTT","CURSOR TO FIRST");
                    do {
                        Log.d("TESTTTT","INSIDE DO WHILE");
                        String date = cursor.getString(0);
                        if (compareDate(date, start, end)) {
                            Log.d("TESTTTT","DATE CHECK DONE");
                            String partyName = cursor.getString(1);
                            String productName = cursor.getString(2);
                            String subProductName = cursor.getString(3);
                            String setSizes = cursor.getString(4);
                            String totalSets = cursor.getString(5);
                            String weight = cursor.getString(6);

                            Log.d("INSIDEEE", weight);

                            sheet.addCell(new Label(0, i, date));
                            sheet.addCell(new Label(1, i, partyName));
                            sheet.addCell(new Label(2, i, productName));
                            sheet.addCell(new Label(3, i, subProductName));
                            sheet.addCell(new Label(4, i, setSizes));
                            sheet.addCell(new Label(5, i, totalSets));
                            sheet.addCell(new Label(6, i, weight));

                            i++;
                        }


                    } while (cursor.moveToNext());

                }
                    cursor.close();
                    workbook.write();
                    workbook.close();
                    Toast.makeText(MakeCustomExcel.this,"Custom excel file named "+fileName+ " successfully created",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MakeCustomExcel.this, MainActivity.class);
                database.close();
                startActivity(intent);
            }
            else {
                requestPermissionAndContinue();
            }
        }
                 catch(Exception e){
                Toast.makeText(MakeCustomExcel.this, "Some error occured while converting to excel", Toast.LENGTH_SHORT).show();
                e.printStackTrace();

        }

    }

    public boolean compareDate(String tableDate, String enteredStartDate, String enteredEndDate) throws ParseException {
        String[] splitTable = tableDate.split(" ");
        String[] splitStart = enteredStartDate.split("-");
        String[] splitEnd = enteredEndDate.split("-");
        Log.d("STARTANDEND","Inside compareDate");
        String dateTable = DateConvertor.convertIntoDoubleDigit(splitTable[0]);
        String dateStart = DateConvertor.convertIntoDoubleDigit(String.valueOf(Integer.parseInt(splitStart[0])-1));
        String dateEnd = DateConvertor.convertIntoDoubleDigit(splitEnd[0]);
        String monthTable = DateConvertor.convertIntoDoubleDigit(DateConvertor.monthConvertor(splitTable[1]));
        String monthStart = DateConvertor.convertIntoDoubleDigit(splitStart[1]);
        String monthEnd = DateConvertor.convertIntoDoubleDigit(splitEnd[1]);
        String yearTable = splitTable[2];
        String yearStart = splitStart[2];
        String yearEnd = splitEnd[2];
        Log.d("STARTANDEND","string made");
        Log.d("StartDateeeee",dateStart+" "+monthStart+" "+yearStart);
        Log.d("EndDateeeee", dateEnd+" "+monthEnd+" "+yearEnd);
        Log.d("TableDateee",dateTable+" "+monthTable+" "+yearTable);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date startDate = sdf.parse(yearStart+"-"+monthStart+"-"+dateStart);
        Date date = sdf.parse(yearTable+"-"+monthTable+"-"+dateTable);
        Date endDate = sdf.parse(yearEnd+"-"+monthEnd+"-"+dateEnd);

        Log.d("STARTANDEND",startDate+" "+ date);
        int startTable = startDate.compareTo(date);
        int endTable = endDate.compareTo(date);

        Log.d("STARTANDEND",startTable+" "+endTable);

        if((startDate.equals(date)||startDate.before(date))&&(endDate.after(date)||endDate.equals(date))){
            return true;
        }
        else return false;

    }

    private boolean checkPermission() {

        return ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ;
    }

    private void requestPermissionAndContinue() throws ParseException, IOException, WriteException {
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
                        ActivityCompat.requestPermissions(MakeCustomExcel.this, new String[]{WRITE_EXTERNAL_STORAGE
                                , READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
                Log.e("", "permission denied, show dialog");
            } else {
                ActivityCompat.requestPermissions(MakeCustomExcel.this, new String[]{WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }
        else{
            createCustomExcel();
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
                    try {
                        try {
                            createCustomExcel();
                        } catch (WriteException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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

}
