package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class WalkRunSession extends HomePage implements UpdateStepTextView {

    public static final String WALK_RUN_INTENT = "From_Walk/Run";

    private boolean isCancelled = false;
    private long startTime = System.currentTimeMillis();
    private int minutes;
    private int seconds;
    private TextView timerText;
    private TimeData timeData;
    private FitnessService googleApi;
    private TimerCount runner;
    private String resultTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_run_session);
        Log.d("in walk/run", "in walk run session");

        // timer initialize
        timerText = findViewById(R.id.timer_text);

        // steps and miles initialize
        stepCountText = findViewById(R.id.activity_miles_number2);
        milesText = findViewById(R.id.activity_miles_number);

        googleApi = FitnessServiceFactory.getFS("GOOGLE_FIT");

        // button that stops the activity
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


        // Set up the time/mocktime
        timeData = new TimeData();

        // EditText to input time in milliseconds
        EditText mockTimeEditText = (EditText) findViewById(R.id.mockTimeEditText);

        // Button that submits mockTime
        Button mockTimeButton = (Button) findViewById(R.id.submitMockTimeButton);
        mockTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    long mockTime =  Long.parseLong(mockTimeEditText.getText().toString());
                    timeData.setMockTime(mockTime);
                } catch (Exception e) {

                }
            }
        });
    }

    /**
     * Run both timer and step/miles update in different threads
     */
    @Override
    protected void onResume() {
        super.onResume();
        runner = new TimerCount();
        resultTime = timerText.getText().toString();
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,resultTime);
    }

    @Override
    protected void onStop() {
        super.onStop();
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

    /**
     * launches to the routes form
     */
    public void launchRouteForm(){
        Intent intent = new Intent(this, RoutesForm.class);

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
     * timer that the runner uses
     */
    private class TimerCount extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params){
            isCancelled = false;
            String toReturn = "0";
            publishProgress(toReturn);


            while(true){
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
