package com.example.walkwalkrevolution.forms;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.custom_data_classes.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Opens up Date Feature on RoutesForm, allowing user to choose a date.
 */
public class SetDate extends AppCompatActivity {

    // Activity objects
    private Button saveButton, cancelButton;
    private DatePicker datePicker;

    private static final String TAG = "SetDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_date);

        Log.d(TAG, "Date Setter Called...");

        datePicker = (DatePicker)findViewById(R.id.datePicker);

        // Check which intent we are from
        Intent fromIntent = this.getIntent();
        if (fromIntent != null) {

            try {
                // Try to get the current saved date of the route
                String date = fromIntent.getExtras().getString("date");
                int[] mdy = DateTimeFormatter.extractMonthDayYear(date);

                // Set the date info on the calendar
                datePicker.init(mdy[DateTimeFormatter.YEAR_INDEX],
                        mdy[DateTimeFormatter.MONTH_INDEX],
                        mdy[DateTimeFormatter.DAY_INDEX], null);;

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
        int month = datePicker.getMonth() + 1;
        int day = datePicker.getDayOfMonth();
        int year = datePicker.getYear();

        String date = DateTimeFormatter.formatMonthDayYear(month, day, year);

        // Pass the new date back to the RoutesForm
        Intent intent = new Intent();
        intent.putExtra("newDate", date);
        setResult(RESULT_OK, intent);
        Log.d(TAG, "Date Saved: " + date);
        finish();
    }

    /**
     * Cancel button behavior. Go to Routes Form Activity without saving data.
     */
    private void cancel() {
        Log.d(TAG, "Date Not Saved");
        finish();
    }

}
