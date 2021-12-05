package com.example.tippingcalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class to utilize the local storage as a SQLite database
 */
public class DOH extends SQLiteOpenHelper {
    /**
     * Table Name: tipTable
     * __________________________________
     * ID | Date | Bill | Tip Percentage | Tip Amount | Tax |Total
     */
    final static String TIP_TABLE = "tipTable";
    final static String ID = "_id";
    final static String DATE = "currentTimeframe";
    final static String PRICE = "price";
    final static String PERCENT = "percentage";
    final static String TAX = "tax";
    final static String TIP = "tip";
    final static String TOTAL = "total";
    /** Create Table command */
    final private static String CREATE_CMD = "CREATE TABLE " + TIP_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATE + " TEXT NOT NULL, " + PRICE + " TEXT NOT NULL, " + PERCENT + " TEXT NOT NULL, " + TAX + " TEXT NOT NULL, " + TIP + " TEXT NOT NULL, " + TOTAL + " TEXT NOT NULL)";

    /**
     * Table Name: rateTable
     * __________________________________
     * ID | Excellent % | Average % | Bad % | Tax %
     */
    final static String RATE_TABLE = "rateTable";
    final static String EXCELLENT_RATE= "excellentRate";
    final static String AVERAGE_RATE = "averageRate";
    final static String BAD_RATE = "badRate";
    final static String TAX_RATE = "taxRate";
    /** Create Table command */
    final private static String CREATE_RATE_TABLE = "CREATE TABLE " + RATE_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EXCELLENT_RATE + " TEXT NOT NULL, " + AVERAGE_RATE + " TEXT NOT NULL, "+ BAD_RATE + " TEXT NOT NULL, " + TAX_RATE + " TEXT NOT NULL)";

    final private Context context;

    public DOH(Context context) {
        super(context, "tipping_db", null, 1);
        this.context = context;
    }

    /**
     * Creates the database the first time app runs
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CMD);
        sqLiteDatabase.execSQL(CREATE_RATE_TABLE);

        ContentValues values = new ContentValues();

        values.put(EXCELLENT_RATE, "20");
        values.put(AVERAGE_RATE, "15");
        values.put(BAD_RATE, "10");
        values.put(TAX_RATE, "2.5");
        sqLiteDatabase.insert(RATE_TABLE, null, values);
        values.clear();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // TODO Auto-generated method stub
    }
}
