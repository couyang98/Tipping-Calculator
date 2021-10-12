package com.example.tippingcalculator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class History extends AppCompatActivity {
    public SQLiteDatabase db = null;
    public DOH dbHelper = null;

    Cursor mC;
    ArrayAdapter adapter;
    ListView list;

    final static String[] all_columns = { DOH.ID, DOH.DATE, DOH.PRICE, DOH.PERCENT, DOH.TIP, DOH.TOTAL};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity);

        list = (ListView)findViewById(R.id.tipListView);
        adapter = new ArrayAdapter(this, R.layout.line_view);
        list.setAdapter(adapter);

        dbHelper = new DOH(this);
        db = dbHelper.getReadableDatabase();


        mC = db.query(dbHelper.DBNAME, all_columns, null, null, null, null, null);

        mC.moveToFirst();

        do {
            int priceC = mC.getColumnIndex(dbHelper.PRICE);
            int percentC = mC.getColumnIndex(dbHelper.PERCENT);
            int tipC = mC.getColumnIndex(dbHelper.TIP);
            int totalC = mC.getColumnIndex(dbHelper.TOTAL);
            int dateC = mC.getColumnIndex(dbHelper.DATE);

            String priceStr = String.format("$%.2f", Float.parseFloat(mC.getString(priceC)));
            String percentStr = String.format("%s%%", mC.getString(percentC));
            String tipStr = String.format("$%.2f", Float.parseFloat(mC.getString(tipC)));
            String totalStr = String.format("$%.2f", Float.parseFloat(mC.getString(totalC)));
            String dateStr = mC.getString(dateC);

            String text = "Time: " + dateStr + "\nPrice: " + priceStr + " | Percent: " + percentStr + " | Tip: " + tipStr + " | Total: " + totalStr;
            adapter.insert(text, 0);
        } while (mC.moveToNext());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
