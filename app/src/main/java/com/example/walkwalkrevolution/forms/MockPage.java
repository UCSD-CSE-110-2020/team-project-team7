package com.example.walkwalkrevolution.forms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.UpdateStepTextView;
import com.example.walkwalkrevolution.custom_data_classes.TimeData;

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

    private boolean isFromHome = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_page);

        stepCountText = findViewById(R.id.steps);
        // mock page called from home
        stepCount = getIntent().getLongExtra("stepCountFromHome", -1);
        // mock page called from walk run session
        if (stepCount == -1) {
            isFromHome = false;
            stepCount = getIntent().getLongExtra("stepCountFromSession", 0);
        }
        stepCountText.setText(String.valueOf(stepCount));


        // Make a new TimeData object based on data in shared prefs
        timeData = new TimeData();
        timeData.update(getSharedPreferences(TimeData.TIME_DATA, MODE_PRIVATE));

        // EditText to input time in milliseconds
        mockTimeEditText = (EditText) findViewById(R.id.mockTimeEditText);
        // Set the text to be the current time or the mock time
        mockTimeEditText.setText(timeData.getTime() + "");

        // BUTTONS ---------------------------------------------------------------------------------

        // Button that submits mockTime
        Button mockTimeButton = (Button) findViewById(R.id.submitMockTimeButton);
        mockTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveMockTime();
                    Log.d(TAG, "Successfully saved mock time.");
                } catch (Exception e) {
                    Log.d(TAG, "Saving mock time failed.");
                }
            }
        });
        // Button that resets mockTime to current time
        Button resetTimeButton = (Button) findViewById(R.id.resetTimeButton);
        resetTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // save the mock time
                timeData.setMockTime(-1);
                timeData.saveTimeData(getSharedPreferences(TimeData.TIME_DATA, MODE_PRIVATE));
                mockTimeEditText.setText(timeData.getTime() + "");
                Log.d(TAG, "Mock time reset.");
            }
        });
        // Button that increments steps by 500
        Button mockStepsButton = (Button) findViewById(R.id.addStepsButton);
        mockStepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    stepCount += 500;
                    stepCountText.setText(String.valueOf(stepCount));

                } catch (Exception e) {
                    Log.d(TAG, "step count button failed");
                }
            }
        });
        // button that leaves the activity
        Button doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFromHome) {
                    saveSteps();
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("stepCount", stepCount);
                    setResult(RESULT_OK, intent);
                    finish();
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


    // SAVING STEPS/TIME ---------------------------------------------------------------------------

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
        SharedPreferences prefs = getSharedPreferences("MockSteps", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("getsteps", stepCount);
        editor.apply();
    }

    public static void saveInputtedSteps(SharedPreferences prefs, long stepCount) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("getsteps", stepCount);
        editor.apply();
    }

}
