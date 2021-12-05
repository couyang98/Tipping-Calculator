package com.example.tippingcalculator;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Class to display all transaction history saved to the SQLite local database.
 */
public class History extends AppCompatActivity {
    public SQLiteDatabase db = null;
    public DOH dbHelper = null;

    Cursor cursor;
    ArrayAdapter adapter;
    ListView list;
    final static String[] all_columns = {DOH.ID, DOH.DATE, DOH.PRICE, DOH.PERCENT, DOH.TAX, DOH.TIP, DOH.TOTAL};

    /**
     * Runs when activity first starts
     * Display all transactions saved in the database in order from most recent
     * Layout: view_activity.xml
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity);

        list = (ListView)findViewById(R.id.tipListView);
        adapter = new ArrayAdapter(this, R.layout.line_view);
        list.setAdapter(adapter);

        dbHelper = new DOH(this);
        db = dbHelper.getReadableDatabase();
        cursor = db.query(dbHelper.TIP_TABLE, all_columns, null, null, null, null, null);
        populateList(cursor);

        /**
         * Clicking item in list will display detail of transaction
         */
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewItem(position);
            }
        });

        /**
         * Pressing and holding on a item will delete it from the database
         */
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.remove(adapter.getItem(position));
                removeItem(position);
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * ################################################
     * # Functions created to assist the application #
     * ################################################
     */

    /**
     * When an item in list is clicked user can see more details related to that specific bill
     * Used in onItemClick()
     * @param position the index of item that was clicked
     */
    private void viewItem(int position) {
        cursor.moveToPosition(Math.abs(position-(cursor.getCount()-1)));
        int priceC = cursor.getColumnIndex(dbHelper.PRICE);
        int percentC = cursor.getColumnIndex(dbHelper.PERCENT);
        int taxC = cursor.getColumnIndex(dbHelper.TAX);
        int tipC = cursor.getColumnIndex(dbHelper.TIP);
        int totalC = cursor.getColumnIndex(dbHelper.TOTAL);
        int dateC = cursor.getColumnIndex(dbHelper.DATE);
        String priceStr = String.format("$%.2f", Float.parseFloat(cursor.getString(priceC)));
        String percentStr = String.format("%s%%", cursor.getString(percentC));
        String taxStr = String.format("$%.2f", Float.parseFloat(cursor.getString(taxC)));
        String tipStr = String.format("$%.2f", Float.parseFloat(cursor.getString(tipC)));
        String totalStr = String.format("$%.2f", Float.parseFloat(cursor.getString(totalC)));
        String dateStr = cursor.getString(dateC);

        Intent viewDetail = new Intent(this, TransactionDetail.class);
        viewDetail.putExtra("PRICE", priceStr);
        viewDetail.putExtra("TIP_RATE", percentStr);
        viewDetail.putExtra("TAX", taxStr);
        viewDetail.putExtra("TIP", tipStr);
        viewDetail.putExtra("TOTAL", totalStr);
        viewDetail.putExtra("DATE", dateStr);
        startActivity(viewDetail);
    }

    /**
     * Item that are pressed on held are deleted from list and database
     * Used in onItemLongClick
     * @param position index of item that needs to be deleted
     */
    private void removeItem(int position) {
        makeToast("A transaction was removed");
        cursor.moveToPosition(Math.abs(position-(cursor.getCount()-1)));
        db.delete(DOH.TIP_TABLE, dbHelper.ID + "=" + cursor.getString(cursor.getColumnIndex(dbHelper.ID)), null);
    }

    /**
     * Displays a toast message
     * Used in removeItem()
     * @param str the text to display in toast message
     */
    private void makeToast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

    /**
     * Populates the ListView of the current activity's interface
     * Minimal information is displayed to keep interface clean
     * When user clicks an item a more detailed information are shown
     */
    private void populateList(Cursor cursor) {
        cursor.moveToFirst();
        do {
            int totalC = cursor.getColumnIndex(dbHelper.TOTAL);
            int dateC = cursor.getColumnIndex(dbHelper.DATE);

            String totalStr = String.format("$%.2f", Float.parseFloat(cursor.getString(totalC)));
            String dateStr = cursor.getString(dateC);

            String text = "Time: " + dateStr + "\nTotal: " + totalStr;
            adapter.insert(text, 0);
        } while (cursor.moveToNext());
    }

}
