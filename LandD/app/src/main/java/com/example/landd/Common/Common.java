package com.example.landd.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;

import com.example.landd.Model.Account;
import com.example.landd.Model.Request;

import java.util.Calendar;
import java.util.Locale;
//import com.example.salmangeforce.food_order.Model.Request;
//import com.example.salmangeforce.food_order.Model.User;

public class Common {
    public static Account currentUser;
    //public static Request currentRequest;
    public final static String UPDATE = "Update";
    public final static String DELETE = "Delete";
    public final static String USER_PHONE = "UserPhone";
    public final static String USER_PASSWORD = "UserPassword";
    public final static String USER_NAME = "UserName";
    public final static String PWD_Key = "Password";
    public final static String CLIENT = "client";
    public final static String SERVER = "server";
    public static Request currentRequest;


    public static String getStatus(String status) {
        switch (status) {
            case "0":
                return "Placed";
            case "1":
                return "Shipping";
            case "2":
                return "Shipped";
            default:
                return "Status Not Available";
        }
    }


    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static  String getdate(long time)
    {
        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        StringBuilder date=new StringBuilder(DateFormat.format("dd-MM-yyyy HH:mm",calendar).toString());
        return date.toString();
    }
}
