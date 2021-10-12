package com.example.tippingcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    public Integer excellent_rate, average_rate, bad_rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        excellent_rate = intent.getIntExtra("EXCELLENT_RATE", 0);
        average_rate = intent.getIntExtra("AVERAGE_RATE", 0);
        bad_rate = intent.getIntExtra("BAD_RATE", 0);

        EditText editExcellent = (EditText) findViewById(R.id.excellentTip);
        editExcellent.setHint(excellent_rate + "%");
        EditText editAverage = (EditText) findViewById(R.id.averageTip);
        editAverage.setHint(average_rate + "%");
        EditText editBad = (EditText) findViewById(R.id.lackingTip);
        editBad.setHint(bad_rate + "%");
    }

    public void onUpdateClick(View v) {
        Intent finalizeUpdate = new Intent(this, MainActivity.class);
        EditText excellentInput = (EditText)findViewById(R.id.excellentTip);
        EditText averageInput = (EditText)findViewById(R.id.averageTip);
        EditText badInput = (EditText)findViewById(R.id.lackingTip);

        if(excellentInput.getText().toString().length() != 0) {
            excellent_rate = Integer.parseInt(excellentInput.getText().toString());
        }
        if(averageInput.getText().toString().length() != 0) {
            average_rate = Integer.parseInt(averageInput.getText().toString());
        }
        if(badInput.getText().toString().length() != 0) {
            bad_rate = Integer.parseInt(badInput.getText().toString());
        }

        finalizeUpdate.putExtra("NEW_EXCELLENT", excellent_rate);
        finalizeUpdate.putExtra("NEW_AVERAGE", average_rate);
        finalizeUpdate.putExtra("NEW_POOR", bad_rate);

        setResult(1, finalizeUpdate);
        Toast.makeText(getApplicationContext(), "Tip Rates Updated", Toast.LENGTH_SHORT).show();
        finish();

    }

    public void onBackPressed() {
        Intent finalizeUpdate = new Intent(this, MainActivity.class);
        finalizeUpdate.putExtra("NEW_EXCELLENT", excellent_rate);
        finalizeUpdate.putExtra("NEW_AVERAGE", average_rate);
        finalizeUpdate.putExtra("NEW_POOR", bad_rate);
        setResult(1, finalizeUpdate);
        Toast.makeText(getApplicationContext(), "Back Button Pressed", Toast.LENGTH_SHORT).show();
        finish();
    }
}
