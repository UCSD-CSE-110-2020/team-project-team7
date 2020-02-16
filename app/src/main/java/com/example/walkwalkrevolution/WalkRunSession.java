package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.TimedMetaData;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.fitness.GoogleFitAdapter;

import java.util.Timer;

public class WalkRunSession extends AppCompatActivity implements UpdateStepTextView {

    // Constant for logging
    private static final String TAG = "WalkRunSession";

    public static final String WALK_RUN_INTENT = "From_Walk/Run";
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private boolean isCancelled = false;
    private long startTime;
    private int minutes;
    private int seconds;
    private long stepCount;
    private double stepsPerMile;
    private double milesCount;
    private TextView timerText;
    private TextView stepCountText;
    private TextView milesText;
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

        // timer initialize
        Log.d(TAG, "Creating time counter.");
        timerText = findViewById(R.id.timer_text);

        // steps and miles initialize
        stepCountText = findViewById(R.id.activity_miles_number2);
        milesText = findViewById(R.id.activity_miles_number);
        stepsPerMile = getIntent().getDoubleExtra("stepsPerMileFromHome", 1);

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
        // Initialize timeData
        startTime = timeData.getTime();


        // Button that stops the activity
        Button stopActivity = (Button) findViewById(R.id.stop_btn);
        stopActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sc.cancel(true);
                isCancelled = true;
                //finish();
                launchRouteForm();
                finish();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Run both timer and step/miles update in different threads
     */
    @Override
    protected void onResume() {
        Log.d("WALK RUN SESSION ONRESUME", "in onresume");
        super.onResume();
        runner = new TimerCount();
        resultTime = timerText.getText().toString();
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,resultTime);
        sc = new StepCountActivity(fitnessService, testStep);
        sc.updateStep = this;
        sc.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("WALK RUN SESSION ONSTOP", "in onstop");
        sc.cancel(true);
        runner.cancel(true);
    }

    /**
     * updateStepView, setStepCount, getStepCount implement UpdateStepInterface
     * setStepCount is called within GoogleFitAdapter.java --> updates stepCount to amount of steps
     * getStepCount is called within StepCountActivity.java --> get stepCount
     * updateStepView is called within StepCountActivity.java --> update TextView to stepCount
     */
    /*@Override
    public void updateStepView(String str) { stepCountText.setText(str); }
    @Override
    public void setStepCount(long sc) { stepCount = sc; }
    @Override
    public long getStepCount() { return this.stepCount; }*/


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
        Log.d(TAG, "Mock button pressed. Launching the Mock Page.");
        startActivity(intent);

        // Get the new time data
        Log.d(TAG, "New current time: " + timeData.getTime());
    }


    // ASYNC TASK, TIMER ---------------------------------------------------------------------------

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
                timeData.update(getSharedPreferences(TimeData.TIME_DATA, MODE_PRIVATE));
                long millis = timeData.getTime() - startTime;
                seconds = (int)(millis/1000);
                minutes = seconds/60;
                seconds %= 60;
                toReturn = String.format("%d:%02d", minutes, seconds);
                publishProgress(toReturn);
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
