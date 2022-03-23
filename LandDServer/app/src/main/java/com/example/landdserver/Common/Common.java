package com.example.landdserver.Common;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;

import com.example.landdserver.Model.Account;
import com.example.landdserver.Model.Request;
import com.example.landdserver.Remote.IGeoCoordinates;
import com.example.landdserver.Remote.RetrofitClient;

import java.util.Calendar;
import java.util.Locale;


public class Common {
    public static Account currentUser;
    public static Request currentRequest;
    //public static Request currentRequest;
    public final static String UPDATE = "Update";
    public final static String DELETE = "Delete";
    public final static String USER_PHONE = "UserPhone";
    public final static String USER_PASSWORD = "UserPassword";
    public final static String USER_NAME = "UserName";
    public final static String CLIENT = "client";
    public final static String SERVER = "server";


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
    public static final String baseUrl="https://maps.googleapis.com";
    public static IGeoCoordinates getGeoCodeServide(){
        return RetrofitClient.getClient(baseUrl).create(IGeoCoordinates.class);
    }
    public static Bitmap scaleBitmap(Bitmap bitmap,int newWidth,int newHeight)
    {
        Bitmap scaleBitmap=Bitmap.createBitmap(newWidth , newHeight, Bitmap.Config.ARGB_8888);
        float scaleX=newWidth/(float)bitmap.getWidth();
        float scaleY=newHeight/(float)bitmap.getHeight();
        float pivotX=0,pivotY=0;
        Matrix scaleMatrix=new Matrix();
        scaleMatrix.setScale(scaleX,scaleY,pivotX,pivotY);
        Canvas canvas=new Canvas(scaleBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap,0,0,new Paint(Paint.FILTER_BITMAP_FLAG));
        return scaleBitmap;
    }
    public static  String getdate(long time)
    {
        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        StringBuilder date=new StringBuilder(DateFormat.format("dd-MM-yyyy HH:mm",calendar).toString());
        return date.toString();
    }
}