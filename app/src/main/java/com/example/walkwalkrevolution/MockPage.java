package com.example.walkwalkrevolution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Class for mocking steps and time.
 */
public class MockPage extends AppCompatActivity {

    // Constant for logging
    private static final String TAG = "MockPage";

    // UI elements
    EditText mockTimeEditText;

    // Mock data
    TimeData timeData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_page);

        // button that leaves the activity
        Button doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Make a new TimeData object based on data in shared prefs
        timeData = new TimeData();
        timeData.update(getSharedPreferences(TimeData.TIME_DATA, MODE_PRIVATE));

        // EditText to input time in milliseconds
        mockTimeEditText = (EditText) findViewById(R.id.mockTimeEditText);
        // Set the text to be the current time or the mock time
        mockTimeEditText.setText(timeData.getTime() + "");

        // Button that submits mockTime
        Button mockTimeButton = (Button) findViewById(R.id.submitMockTimeButton);
        mockTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveMockTime();
                    Log.d(TAG, "Successfully saved mock time failed.");
                } catch (Exception e) {
                    Log.d(TAG, "Saving mock time failed.");
                }
            }
        });

    }

    /**
     * Saves the mock time in the Edit Text to shared prefs.
     */
    private void saveMockTime() {
        // Get the new mock time from edit text
        long mockTime = Long.parseLong(mockTimeEditText.getText().toString());
        Log.d(TAG, "Mock time to save: " + mockTime);

        // save the mock time
        timeData.setMockTime(mockTime);
        timeData.saveTimeData(getSharedPreferences(TimeData.TIME_DATA, MODE_PRIVATE));
    }

}
