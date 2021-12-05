package com.example.tippingcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Displays bill detail based of the selected item in the list
 */
public class TransactionDetail extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_detail);

        Intent displayIntent = getIntent();
        displayInfo(displayIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onBackPressed() {
        finish();
    }

    /**
     * Sets all the transaction detail into their respective TextView
     * @param displayIntent intent has all the information to be used
     */
    private void displayInfo(Intent displayIntent) {

        TextView dateText = (TextView) findViewById(R.id.dateView);
        dateText.setText(displayIntent.getStringExtra("DATE"));

        TextView totalText = (TextView) findViewById(R.id.totalView);
        totalText.setText(displayIntent.getStringExtra("TOTAL"));

        TextView billText = (TextView) findViewById(R.id.billView);
        billText.setText(displayIntent.getStringExtra("PRICE"));

        TextView tipText = (TextView) findViewById(R.id.tipView);
        tipText.setText(displayIntent.getStringExtra("TIP") + " (" + displayIntent.getStringExtra("TIP_RATE") + ")");

        TextView taxText = (TextView) findViewById(R.id.taxView);
        taxText.setText(displayIntent.getStringExtra("TAX"));
    }


}
