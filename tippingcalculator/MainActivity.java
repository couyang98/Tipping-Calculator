package com.example.tippingcalculator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    public final int ACTIVITY_RESULT = 1;

    int excellent_tip = 20;
    int average_tip = 18;
    int bad_tip = 14;

    private SQLiteDatabase db = null;
    private DOH dbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //System.out.println("CREATE HERE");
        setContentView(R.layout.activity_main);
        dbHelper = new DOH(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        if(db != null) db.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        float bill;
        //Check which radio button was clicked
        switch (view.getId()) {
            case R.id.excellent_button:
                if (checked) {
                    EditText b = (EditText)findViewById(R.id.bill);
                    if (b.getText().toString().equals(""))
                        bill = 0;
                    else bill = Float.parseFloat(b.getText().toString());
                    compute_tip(bill, excellent_tip);
                }
                break;
            case R.id.average_button:
                if (checked) {
                    EditText b = (EditText)findViewById(R.id.bill);
                    if (b.getText().toString().equals(""))
                        bill = 0;
                    else bill = Float.parseFloat(b.getText().toString());
                    compute_tip(bill, average_tip);
                }
                break;
            case R.id.lacking_button:
                if (checked) {
                    EditText b = (EditText)findViewById(R.id.bill);
                    if (b.getText().toString().equals(""))
                        bill = 0;
                    else bill = Float.parseFloat(b.getText().toString());
                    compute_tip(bill, bad_tip);
                }
                break;
        }
    }
    public static String roundToTwoDigit(float paramFloat) {
        return String.format("%.2f%n", paramFloat);
    }
    void compute_tip(float bill, int percent) {
        float pct = (float)percent/100;
        float tip = bill * pct;
        float total = bill + tip;
        TextView t = (TextView)findViewById(R.id.computed_tip);
        String s = roundToTwoDigit(tip);
        t.setText(s);
        t = (TextView)findViewById(R.id.bill_total);
        s = roundToTwoDigit(total);
        t.setText(s);

        ContentValues cv = new ContentValues();
        cv.put(DOH.PRICE, Float.toString(bill));
        cv.put(DOH.PERCENT, Integer.toString(percent));
        cv.put(DOH.TIP, Float.toString(tip));
        cv.put(DOH.TOTAL, Float.toString(total));
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        day.setTimeZone(TimeZone.getTimeZone("EST"));
        cv.put(DOH.DATE, day.format(date));
        db.insert(DOH.DBNAME, null, cv);
        cv.clear();
    }


    public void updateClick(View view) {
        // Create an intent for updating tip
        RadioGroup radio = findViewById(R.id.group1);
        radio.clearCheck();
        EditText bill = findViewById(R.id.bill);
        bill.setText("");
        TextView tipView = findViewById(R.id.computed_tip);
        tipView.setText("");
        TextView totalView = findViewById(R.id.bill_total);
        totalView.setText("");

        Intent update = new Intent(this, MainActivity2.class);
        update.putExtra("EXCELLENT_RATE", excellent_tip);
        update.putExtra("AVERAGE_RATE", average_tip);
        update.putExtra("BAD_RATE", bad_tip);

        startActivityForResult(update, ACTIVITY_RESULT);
    }


    public void historyClick(View view) {
        Intent viewHistory = new Intent(this, History.class);
        startActivity(viewHistory);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_RESULT) {
            excellent_tip = data.getIntExtra("NEW_EXCELLENT", 0);
            average_tip = data.getIntExtra("NEW_AVERAGE", 0);
            bad_tip = data.getIntExtra("NEW_POOR", 0);
        }
        onRadioButtonClicked(findViewById(R.id.excellent_button));
        onRadioButtonClicked(findViewById(R.id.average_button));
        onRadioButtonClicked(findViewById(R.id.lacking_button));

    }
}