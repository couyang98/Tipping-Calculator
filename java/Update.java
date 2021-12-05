package com.example.tippingcalculator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity2 is meant to update the percentage rates used in the calculator
 */
public class Update extends AppCompatActivity {
    /**
     * Rates that are being updated
     */
    private int excellent_rate, average_rate, bad_rate;
    private float current_tax;

    private SQLiteDatabase db = null;
    private DOH dbHelper = null;
    Cursor cursor;
    /**
     * Runs when activity first starts
     * Read the data sent from the previous activity that initiated this current activity
     * Layout: activity_main2.xml
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_activity);

        dbHelper = new DOH(this);
        db = dbHelper.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + dbHelper.RATE_TABLE, null);

        Intent intent = getIntent();
        excellent_rate = intent.getIntExtra("EXCELLENT_RATE", 0);
        average_rate = intent.getIntExtra("AVERAGE_RATE", 0);
        bad_rate = intent.getIntExtra("BAD_RATE", 0);
        current_tax = intent.getFloatExtra("TAX_RATE", 0);

        EditText editExcellent = (EditText) findViewById(R.id.excellentTip);
        editExcellent.setHint(excellent_rate + "%");
        EditText editAverage = (EditText) findViewById(R.id.averageTip);
        editAverage.setHint(average_rate + "%");
        EditText editBad = (EditText) findViewById(R.id.lackingTip);
        editBad.setHint(bad_rate + "%");
        EditText editTax = (EditText) findViewById((R.id.taxInput));
        editTax.setHint(current_tax + "%");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(db != null) db.close();
    }

    /**
     * When update button is clicked
     * Unspecified rates will keep its old percentage rates, else new percentage rates are used
     */
    public void onUpdateClick(View v) {
        EditText editExcellent = (EditText) findViewById(R.id.excellentTip);
        EditText editAverage = (EditText) findViewById(R.id.averageTip);
        EditText editBad = (EditText) findViewById(R.id.lackingTip);
        EditText editTax = (EditText) findViewById((R.id.taxInput));


        cursor.moveToFirst();
        ContentValues cv = new ContentValues();
        if(editExcellent.getText().toString().length() != 0) {
            cv.put(DOH.EXCELLENT_RATE, editExcellent.getText().toString());
        } else {
            cv.put(DOH.EXCELLENT_RATE, Integer.toString(excellent_rate));
        }
        if(editAverage.getText().toString().length() != 0) {
            cv.put(DOH.AVERAGE_RATE, editAverage.getText().toString());
        } else {
            cv.put(DOH.AVERAGE_RATE, Integer.toString(average_rate));
        }
        if(editBad.getText().toString().length() != 0) {
            cv.put(DOH.BAD_RATE, editBad.getText().toString());
        } else {
            cv.put(DOH.BAD_RATE, Integer.toString(bad_rate));
        }
        if(editTax.getText().toString().length() != 0) {
            cv.put(DOH.TAX_RATE, editTax.getText().toString());
        } else {
            cv.put(DOH.TAX_RATE, Float.toString(current_tax));
        }
        db.delete(DOH.RATE_TABLE, dbHelper.ID + "=" + cursor.getString(cursor.getColumnIndex(dbHelper.ID)), null);
        db.insert(DOH.RATE_TABLE, null, cv);

        makeToast("Tip Rates Updated");
        finish();

    }

    /**
     * Back press exits out of update without changing the rates
     * AKA keep the old percentages
     */
    public void onBackPressed() {
        makeToast("Back button pressed");
        finish();
    }

    /**
     * ################################################
     * # Functions created to assist the application #
     * ################################################
     */

    private void makeToast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }
}
