package com.example.walkwalkrevolution;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SetDate extends AppCompatActivity {

    // Activity objects
    private Button saveButton;
    private Button cancelButton;
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_date);

        datePicker = (DatePicker)findViewById(R.id.datePicker);

        // Check which intent we are from
        Intent fromIntent = this.getIntent();
        if (fromIntent != null) {

            try {
                // Try to get the current saved date of the route
                String date = fromIntent.getExtras().getString("date");
                int month = Integer.parseInt(date.substring(0,2)) - 1;
                int day = Integer.parseInt(date.substring(3,5));
                int year = Integer.parseInt(date.substring(6, date.length()));

                // Set the date info on the calendar
                datePicker.init(year, month, day, null);;

            } catch(NullPointerException e) {
            }

        }

        // Handle onClickListeners for Save and Cancel Buttons
        saveButton = (Button) findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveDate();
            }
        });
        cancelButton = (Button) findViewById(R.id.CancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancel();
            }
        });

    }

    /**
     * Save button behavior. Saves the date chosen in the datePicker by passing it
     * back as a String to the RoutesForm.
     */
    private void saveDate() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();

        // Handle the case for single digit months/days
        String monthStr = month + "";
        if (monthStr.length() < 2) {
            monthStr = "0" + monthStr;
        }
        String dayStr = day + "";
        if (dayStr.length() < 2) {
            dayStr = "0" + dayStr;
        }

        String date = monthStr + "/" + dayStr + "/" + year;

        // Pass the new date back to the RoutesForm
        Intent intent = new Intent();
        intent.putExtra("newDate", date);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Cancel button behavior. Go to Routes Form Activity without saving data.
     */
    private void cancel() {
        finish();
    }

}
