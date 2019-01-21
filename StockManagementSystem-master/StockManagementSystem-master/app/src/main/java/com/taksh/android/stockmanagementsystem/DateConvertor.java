package com.taksh.android.stockmanagementsystem;

import android.widget.Switch;

public class DateConvertor {

    public static String monthConvertor(String month){

        switch(month){
            case "jan":
                return "1";
            case "feb":
                return "2";
            case "mar":
                return "3";
            case "apr":
                return "4";
            case "may":
                return "5";
            case "jun":
                return "6";
            case "jul":
                return "7";
            case "aug":
                return "8";
            case "sep":
                return "9";
            case "oct":
                return "10";
            case "nov":
                return "11";
            case "dec":
                return "12";
        }
        return "0";
    }

    public static String convertIntoDoubleDigit(String x){
        if(Integer.parseInt(x)<10){
            return "0"+x;
        }
        else return x;
    }
}
