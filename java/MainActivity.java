package com.example.tippingcalculator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * Main page of the application.
 * User input their bill
 */
public class MainActivity extends AppCompatActivity {
    //Activity 1 will be updating percentage
    //All other activity doesn't need a result code because they don't return anything
    public final int ACTIVITY_RESULT = 1;

    private SQLiteDatabase db = null;
    private DOH dbHelper = null;

    /**
     * Tip percentage
     */
    int excellent_tip = 20;
    int average_tip = 18;
    int bad_tip = 14;
    int current_tip;

    /**
     * Tax
     */
    float current_tax = (float) 2.5;

    /**
     * Runs when activity first starts
     * Layout: activity_main.xml
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DOH(this);


    }

    /**
     * This activity is in foreground
     */
    @Override
    protected void onStart() { super.onStart(); }

    /**
     * When this activity is back in use
     */
    @Override
    protected void onResume() {
        super.onResume();
        db = dbHelper.getWritableDatabase();
        Cursor rate_cursor = db.rawQuery("SELECT * FROM " + dbHelper.RATE_TABLE, null);
        rate_cursor.moveToFirst();

        excellent_tip = Integer.valueOf(rate_cursor.getString(rate_cursor.getColumnIndex(dbHelper.EXCELLENT_RATE)));
        average_tip = Integer.valueOf(rate_cursor.getString(rate_cursor.getColumnIndex(dbHelper.AVERAGE_RATE)));
        bad_tip = Integer.valueOf(rate_cursor.getString(rate_cursor.getColumnIndex(dbHelper.BAD_RATE)));
        current_tax = Float.valueOf(rate_cursor.getString(rate_cursor.getColumnIndex(dbHelper.TAX_RATE)));
        onRadioButtonClicked(findViewById(R.id.excellent_button));
        onRadioButtonClicked(findViewById(R.id.average_button));
        onRadioButtonClicked(findViewById(R.id.lacking_button));
    }

    /**
     * When this activity goes to the background
     * Database is closed to allow other activity to access
     */
    @Override
    public void onPause() {
        super.onPause();
        if(db != null) db.close();
    }

    /**
     * Function when radio button is pressed
     * Picks a tipping percentage based on the service level chosen
     * Calculates the bill total with the given tip
     */
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        float bill;
        EditText billInput = (EditText)findViewById(R.id.bill);
        //Check which radio button was clicked
        switch (view.getId()) {
            case R.id.excellent_button:
                if (checked) current_tip = excellent_tip;
                break;
            case R.id.average_button:
                if (checked) current_tip = average_tip;
                break;
            case R.id.lacking_button:
                if (checked) current_tip = bad_tip;
                break;
        }
        if (!billInput.getText().toString().equals("")) {
            bill = Float.parseFloat(billInput.getText().toString());
            compute_tip(bill);
        }
    }

    /**
     * Event when the "Confirm Tip" button is pressed
     * Will add the current inputs and calculations to database
     * Clears all inputs
     */
    public void addToHistory(View view) {
        boolean checked = checkRadioInput();
        EditText billInput = (EditText)findViewById(R.id.bill);
        float bill;
        // If all inputs are valid then add the numbers and data to database
        // Afterwards clear the inputs
        if ((!billInput.getText().toString().equals("")) && checked) {
            bill = Float.parseFloat(billInput.getText().toString());
            float pct = (float)current_tip/100;

            float tax = roundFloatTwoDigits(bill * current_tax/100);
            float tip = roundFloatTwoDigits(bill * pct);
            float total = roundFloatTwoDigits(bill + tip + tax);

            addToDB(bill, tax, tip, total);
            clearInputs();
        } else {
            if(billInput.getText().toString().equals("") && !checked) {
                Toast.makeText(getApplicationContext(), "Please input bill and rate service", Toast.LENGTH_SHORT).show();
            } else if (billInput.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please input bill", Toast.LENGTH_SHORT).show();
            } else if (!checked) {
                Toast.makeText(getApplicationContext(), "Please rate service", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Views the saved tipping history
     */
    public void historyClick(View view) {
        if(!isDBEmpty()) {
            Intent viewHistory = new Intent(this, History.class);
            startActivity(viewHistory);
        } else {
            Toast.makeText(getApplicationContext(),"History is currently empty", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Clears all inputs and outputs
     * Create intent that updates the percentages used for calculation
     */
    public void updateClick(View view) {
        Intent update = new Intent(this, Update.class);
        update.putExtra("EXCELLENT_RATE", excellent_tip);
        update.putExtra("AVERAGE_RATE", average_tip);
        update.putExtra("BAD_RATE", bad_tip);
        update.putExtra("TAX_RATE", current_tax);
//        startActivityForResult(update, ACTIVITY_RESULT);
        startActivity(update);
    }

    /**
     * ################################################
     * # Functions created to assist the application #
     * ################################################
     */

    /**
     * Makes sure all dollar numbers are in recognizable format
     * Used in compute_tip()
     * @param paramFloat the number that needs to be formatted
     * @return correctly formatted number as a String
     */
    private String roundStringTwoDigits(float paramFloat) {
        return String.format("%.2f%n", paramFloat);
    }

    /**
     * Makes sure number used in calculation will be consistent with what is being displayed
     * Used in addToHistory() and compute_tip()
     * @param paramFloat the number that needs to be formatted
     * @return correctly formatted number
     */
    private float roundFloatTwoDigits(float paramFloat) {
        return Float.valueOf(String.format("%.2f%n", paramFloat));
    }

    /**
     * Calculates the bill's tax, tip, and total
     * Displays calculation to user
     * Used in onRadioButtonClicked()
     * @param bill to be calculated
     */
    private void compute_tip(float bill) {
        // Calculations of the bill
        float pct = (float)current_tip/100;
        float tax = roundFloatTwoDigits(bill * current_tax/100);
        float tip = roundFloatTwoDigits(bill * pct);
        float total = roundFloatTwoDigits(bill + tip + tax);

        //Display the calculated bill
        TextView taxView = (TextView)findViewById(R.id.computed_tax);
        taxView.setText(roundStringTwoDigits(tax));
        TextView tipView = (TextView)findViewById(R.id.computed_tip);
        tipView.setText(roundStringTwoDigits(tip));
        TextView totalView = (TextView)findViewById(R.id.bill_total);
        totalView.setText(roundStringTwoDigits(total));

    }

    /**
     * Adds information to database
     * Used in addToHistory()
     */
    private void addToDB(float bill, float tax, float tip, float total) {
        ContentValues cv = new ContentValues();
        cv.put(dbHelper.PRICE, Float.toString(bill));
        cv.put(dbHelper.PERCENT, Integer.toString(current_tip));
        cv.put(dbHelper.TAX, Float.toString(tax));
        cv.put(dbHelper.TIP, Float.toString(tip));
        cv.put(dbHelper.TOTAL, Float.toString(total));
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        day.setTimeZone(TimeZone.getTimeZone("EST"));
        cv.put(dbHelper.DATE, day.format(date));
        db.insert(dbHelper.TIP_TABLE, null, cv);
        cv.clear();
        Toast.makeText(getApplicationContext(),"Bill added to history", Toast.LENGTH_SHORT).show();
    }

    /**
     * checks if one of the radio buttons are clicked
     * Used in addToHistory()
     * @return true if user input their dining experience
     */
    private boolean checkRadioInput() {
        return ((RadioButton)findViewById(R.id.excellent_button)).isChecked() || ((RadioButton)findViewById(R.id.average_button)).isChecked() || ((RadioButton)findViewById(R.id.lacking_button)).isChecked();
    }

    /**
     * Clears the screen of all inputs and outputs
     * Used in addToHistory()
     */
    private void clearInputs() {
        EditText billInput = (EditText)findViewById(R.id.bill);
        TextView taxView = (TextView)findViewById(R.id.computed_tax);
        TextView tipView = (TextView)findViewById(R.id.computed_tip);
        TextView totalView = (TextView)findViewById(R.id.bill_total);
        RadioGroup radioView = (RadioGroup)findViewById(R.id.group1);
        billInput.setText("");
        taxView.setText("");
        tipView.setText("");
        totalView.setText("");
        radioView.clearCheck();
    }

    /**
     * Checks if database is empty by checking if the cursor has a head
     * Used in historyClick()
     * @return true if database has items, false otherwise so program doesn't attempt to open a
     *  empty database
     */
    private boolean isDBEmpty() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + dbHelper.TIP_TABLE, null);
        if(!cursor.moveToFirst()){
            return true;
        }
        return false;
    }
}