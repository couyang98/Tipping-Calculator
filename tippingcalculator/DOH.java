package com.example.tippingcalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DOH extends SQLiteOpenHelper {
    final static String DBNAME = "tipTable";
    final static String ID = "_id";
    final static String DATE = "pe";
    final static String PRICE = "price";
    final static String PERCENT = "percentage";
    final static String TIP = "tip";
    final static String TOTAL = "total";
    final private static String CREATE_CMD = "CREATE TABLE " + DBNAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATE + " TEXT NOT NULL, " + PRICE + " TEXT NOT NULL, " + PERCENT + " TEXT NOT NULL, " + TIP + " TEXT NOT NULL, " + TOTAL + " TEXT NOT NULL)";

    final private Context context;

    public DOH(Context context) {
        super(context, "todo_db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CMD);
        ContentValues cv = new ContentValues();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // TODO Auto-generated method stub
    }
}
