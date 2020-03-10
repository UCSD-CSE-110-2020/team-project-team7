package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walkwalkrevolution.custom_data_classes.TimeData;
import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.forms.MockPage;

/**
 * Displays Walk/Run session screen, updating steps/distance user has taken and ticking timer.
 */
public class WalkRunSession extends AppCompatActivity implements UpdateStepTextView {

    // Constant for logging
    private static final String TAG = "WalkRunSession";

    public static final String WALK_RUN_INTENT = "From_Walk/Run";
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    public static final int REQUEST_MOCK_SESSION = 3;

    private boolean isCancelled;
    private long startTime;
    private int minutes, seconds;
    private long stepCount, stepsFromMock;
    private double stepsPerMile, milesCount;
    private TextView timerText, stepCountText, milesText;
    private TimeData timeData;
    private FitnessService fitnessService;
    private StepCountActivity sc;
    private TimerCount runner;
    private String resultTime;
    public boolean testStep = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_run_session);
        Log.d("in walk/run", "in walk run session");

        // timer text initialize
        Log.d(TAG, "Creating time counter.");
        timerText = findViewById(R.id.timer_text);

        // timer counter initialize
        runner = new TimerCount();
        resultTime = timerText.getText().toString();
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,resultTime);

        // steps and miles views initialize
        stepCountText = findViewById(R.id.activity_miles_number2);
        milesText = findViewById(R.id.activity_miles_number);

        // get stepsPerMile from sharedPrefs for calculating distance
        SharedPreferences sharedPreferences = getSharedPreferences("stepsPerMileFromHome", MODE_PRIVATE);
        String stepsPerMileStr = sharedPreferences.getString("stepsPerMileFromHome", "1");
        stepsPerMile = Double.parseDouble(stepsPerMileStr);

        // Check from String extra if a test FitnessService is being passed
        String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        if(fitnessServiceKey == null) {
            fitnessServiceKey = "GOOGLE_FIT";
            testStep = false;
        }
        fitnessService = FitnessServiceFactory.getFS(fitnessServiceKey);


        // Make a new TimeData object based on what's in shared prefs
        timeData = new TimeData();
        timeData.update(getSharedPreferences(TimeData.TIME_DATA, MODE_PRIVATE));
        Log.d(TAG, "Get time: " + timeData.getTime());
        // Initialize startTime, the time we started the session
        startTime = timeData.getTime();
        Log.d(TAG, "Session START TIME: " + startTime);


        // Button that stops the activity
        Button stopActivity = (Button) findViewById(R.id.stop_btn);
        stopActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sc.cancel(true);
                isCancelled = true;
                finish();
                launchRouteForm();
            }
        });
        // Button that opens mockPage
        Button mockPageButton = (Button) findViewById(R.id.mockPageButton);
        mockPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMockPage();
            }
        });

        // Check if steps were mocked before this activity
        SharedPreferences sf = getSharedPreferences("MockSteps" , MODE_PRIVATE);
        long stepsFromMock = sf.getLong("getsteps", -1);
        if(stepsFromMock != -1) {
            this.stepsFromMock = stepsFromMock;
            stepCount = 0;
        }
    }


    // INTERFACE METHODS FOR STEPS/DISTANCE --------------------------------------------------------
    /**
     * updateStepView, setStepCount, getStepCount implement UpdateStepInterface
     * setStepCount is called within GoogleFitAdapter.java --> updates stepCount to amount of steps
     * getStepCount is called within StepCountActivity.java --> get stepCount
     * updateStepView is called within StepCountActivity.java --> update TextView to stepCount
     */
    public void updateStepView(String str) { stepCountText.setText(str + " steps"); }

    public void setStepCount(long sc) { stepCount = sc; }

    public long getStepCount() { return stepCount; }

    public void updatesMilesView(String str) { milesText.setText(str + " miles"); }

    public void setMiles(double mi) { milesCount = mi; }

    public double getMiles() { return milesCount; }

    public double getStepsPerMile() { return this.stepsPerMile; }


    // METHODS FOR HANDLING INFORMATION FROM MOVING BETWEEN ACTIVITIES -----------------------------

    /**
     * Run both timer and step/miles update in different threads
     */
    @Override
    protected void onResume() {
        Log.d("WALK RUN SESSION ONRESUME", "in onresume");
        super.onResume();

        // Update timeData and timerText in case mock time set
        timeData.update(getSharedPreferences(TimeData.TIME_DATA, MODE_PRIVATE));
        timerText.setText(makeTimeString());
        Log.d(TAG, "Get time: " + timeData.getTime());

        sc = new StepCountActivity(fitnessService, testStep);
        sc.updateStep = this;

        // Mock time is set
        if(stepsFromMock != 0) {
            mockStepsSet();
        }

        sc.execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("WALK RUN SESSION ONSTOP", "in onstop");
        sc.cancel(true);

        // Save into shared prefs
        long savedSteps = stepsFromMock + stepCount;
        // Mock was set, save this into prefs
        if (stepsFromMock != 0) {
            SharedPreferences prefs = getSharedPreferences("MockSteps", MODE_PRIVATE);
            MockPage.saveInputtedSteps(prefs, savedSteps);
        }
    }

    // Helper method for handling display when a mock is set
    private void mockStepsSet() {
        setMiles((Math.floor((stepCount / stepsPerMile) * 100)) / 100);
        updateStepView(String.valueOf(getStepCount()));
        updatesMilesView(String.valueOf(getMiles()));
        sc.turnOffAPI = true;
    }


    // ONCLICK EVENTS FOR BUTTONS ------------------------------------------------------------------

     /**
     * launches routes form
     */
    public void launchRouteForm(){
        Intent intent = new Intent(this, RoutesForm.class);
        Log.d(TAG, "Stop button pressed. Launching the Route Form.");

        Log.d("passed steps", String.valueOf(stepCount));
        Log.d("passed miles", String.valueOf(milesCount));
        // Push data to RouteForm
        intent.putExtra("From_Intent", WALK_RUN_INTENT);
        intent.putExtra("minutes", minutes);
        intent.putExtra("minutes", minutes);
        intent.putExtra("seconds", seconds);
        intent.putExtra("steps", stepCount);
        intent.putExtra("distance", milesCount);
        startActivity(intent);
    }

    /**
     * launches the mock page
     */
    private void launchMockPage() {
        Intent intent = new Intent(this, MockPage.class);
        intent.putExtra("stepCountFromSession", stepCount);
        Log.d(TAG, "Mock button pressed. Launching the Mock Page.");

        startActivityForResult(intent, REQUEST_MOCK_SESSION);

        // Get the new time data
        Log.d(TAG, "New current time: " + timeData.getTime());
    }

    /**
     * Handles coming back from the Mock Page activity.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_MOCK_SESSION:
                if(resultCode == RESULT_OK) {
                    stepCount = data.getLongExtra("stepCount", 0);
                    Log.d(TAG, "Saved data from the mock activity");
                }
        }
    }


    // ASYNC TASK, TIMER ---------------------------------------------------------------------------

    private String makeTimeString() {
        if (timeData == null) {
            timeData = new TimeData();
        }
        timeData.update(getSharedPreferences(TimeData.TIME_DATA, MODE_PRIVATE));
        long millis = timeData.getTime() - startTime;
        seconds = (int)(millis/1000);
        minutes = seconds/60;
        seconds %= 60;
        return String.format("%d:%02d", minutes, seconds);
    }


    /**
     * timer that the runner uses
     */
    private class TimerCount extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params){
            isCancelled = false;
            String toReturn = "0";
            publishProgress(toReturn);


            while(true){
                toReturn = makeTimeString();
                Log.d(TAG , "DISPLAYED TIME IS: " + toReturn);
                publishProgress(toReturn);
                if (!isCancelled) {
                    Log.d(TAG , "NOT CANCELLED");
                }

                if(isCancelled) {
                    this.cancel(true);
                    break;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return toReturn;
        }

        @Override
        protected void onProgressUpdate(String... text){
            timerText.setText(text[0]);
        }
    }

    /**
     * prevents the user from pressing the back button to go back to the home screen
     */
    @Override
    public void onBackPressed(){
        Toast.makeText(this, "Please press the stop button to go stop", Toast.LENGTH_SHORT).show();
    }

}
