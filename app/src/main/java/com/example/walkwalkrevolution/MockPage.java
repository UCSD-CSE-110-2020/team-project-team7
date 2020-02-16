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
public class MockPage extends AppCompatActivity implements UpdateStepTextView {

    // Constant for logging
    private static final String TAG = "MockPage";

    // UI elements
    private EditText mockTimeEditText;
    private TextView stepCountText;
    private TextView milesText;
    // Mock data
    private TimeData timeData;
    //private StepCountActivity sc;
    private double stepsPerMile;
    private long stepCount;
    private double milesCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_page);

        // button that leaves the activity
        Button doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSteps();
            }
        });

        stepCountText = findViewById(R.id.steps);
        stepCount = getIntent().getLongExtra("stepCountFromHome", 0);
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

        // Button that increments steps by 500
        Button mockStepsButton = (Button) findViewById(R.id.addStepsButton);
        mockStepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    stepCount += stepCount + 500;
                    stepCountText.setText(String.valueOf(stepCount));
                } catch (Exception e) {
                    Log.d(TAG, "step count button failed");
                }
            }
        });

    }


    /**
     * updateStepView, setStepCount, getStepCount implement UpdateStepInterface
     * setStepCount is called within GoogleFitAdapter.java --> updates stepCount to amount of steps
     * getStepCount is called within StepCountActivity.java --> get stepCount
     * updateStepView is called within StepCountActivity.java --> update TextView to stepCount
     */
    public void updateStepView(String str) { stepCountText.setText(str); }

    public void setStepCount(long sc) { stepCount = sc; }

    public long getStepCount() { return stepCount; }

    public void updatesMilesView(String str) { milesText.setText(str); }

    public void setMiles(double mi) { milesCount = mi; }

    public double getMiles() { return milesCount; }

    public double getStepsPerMile() { return this.stepsPerMile; }
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

    private void saveSteps() {
        /*Intent intent = new Intent(this, HomePage.class);
        intent.putExtra("StepsFromMock", stepCount);
        startActivity(intent);*/
        SharedPreferences sf = getSharedPreferences("MockSteps", 0);
        SharedPreferences.Editor editor = sf.edit();
        editor.putLong("getsteps", stepCount);
        editor.apply();
        finish();
    }

}
