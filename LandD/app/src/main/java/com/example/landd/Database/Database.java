package com.example.landd.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;


import com.example.landd.Model.Mon;
import com.example.landd.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "LandD.db";
    private static final int DB_VERSION = 2;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public List<Order> getCarts()
    {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();

        String sqlTable = "OrderDetail";
        String[] sqlColumns = {"ID", "MonId", "MonName", "Quantity", "Price", "Discount","Image"};

        sqLiteQueryBuilder.setTables(sqlTable);
        Cursor cursor = sqLiteQueryBuilder.query(sqLiteDatabase, sqlColumns, null, null, null, null, null);

        final List<Order> result = new ArrayList<>();
        if(cursor.moveToFirst())
        {
            do {
                result.add(new Order(cursor.getInt(cursor.getColumnIndex("ID")),
                    cursor.getString(cursor.getColumnIndex("MonId")),
                    cursor.getString(cursor.getColumnIndex("MonName")),
                    cursor.getString(cursor.getColumnIndex("Price")),
                    cursor.getString(cursor.getColumnIndex("Quantity")),
                    cursor.getString(cursor.getColumnIndex("Discount")),
                        cursor.getString(cursor.getColumnIndex("Image"))
                ));
            }
            while (cursor.moveToNext());
        }
        return result;
    }


    public void addToCart(Order order)
    {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = String.format("INSERT INTO OrderDetail('MonId', 'MonName', 'Quantity', 'Price', 'Discount','Image')" +
                        "VALUES('%s', '%s', '%s', '%s', '%s','%s');",
                        order.getMonId(), order.getMonName(), order.getQuantity(), order.getPrice(), order.getDiscount(),order.getImage());
        sqLiteDatabase.execSQL(query);
    }


    public void cleanCart()
    {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        sqLiteDatabase.execSQL(query);
    }

//
    public void addToFavorite(String FaId, String userPhone)
    {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = String.format("Insert INTO Favorites" +
                " VALUES('%s','%s');",FaId,userPhone);

        sqLiteDatabase.execSQL(query);
    }
//
//



    public void removeFromFavorite(String FaId,String userPhone)
    {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = String.format("DELETE FROM Favorites WHERE FaId = '%s' and UserPhone='%s';", FaId,userPhone);
        sqLiteDatabase.execSQL(query);
    }

//
    public boolean isFavorite(String FaId,String userPhone)
    {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("SELECT * FROM Favorites WHERE FaId = '%s' and UserPhone='%s';", FaId,userPhone);
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if(cursor.getCount() <= 0)
        {
            cursor.close();
            return false;
        }

        cursor.close();
        return true;
    }



    public int getOrderCount() {
        int count = 0;

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM OrderDetail");
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if(cursor.moveToFirst())
        {
            do{
                count = cursor.getInt(0);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return count;
    }
//
    public void updateCart(Order order) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        @SuppressLint("DefaultLocale")
        String query = String.format("UPDATE OrderDetail SET Quantity = %s WHERE ID = %d", order.getQuantity(), order.getID());
        sqLiteDatabase.execSQL(query);
    }
}//class ends
