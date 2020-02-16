package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Timer;

public class WalkRunSession extends AppCompatActivity {

    // Constant for logging
    private static final String TAG = "WalkRunSession";

    public static final String WALK_RUN_INTENT = "From_Walk/Run";

    private boolean isCancelled = false;
    private long startTime;
    private int minutes;
    private int seconds;
    private TextView timerText;
    private TimeData timeData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_run_session);

        Log.d(TAG, "Creating time counter.");
        timerText = findViewById(R.id.timer_text);
        TimerCount runner = new TimerCount();
        String result = timerText.getText().toString();
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,result);


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

        Log.d(TAG, "Finished onCreate() for Walk Run Session");
    }


    // ONCLICK EVENTS FOR BUTTONS ------------------------------------------------------------------

     /**
     * launches routes form
     */
    public void launchRouteForm(){
        Intent intent = new Intent(this, RoutesForm.class);
        Log.d(TAG, "Stop button pressed. Launching the Route Form.");

        // Push data to RouteForm
        intent.putExtra("From_Intent", WALK_RUN_INTENT);
        intent.putExtra("minutes", minutes);
        intent.putExtra("minutes", minutes);
        intent.putExtra("seconds", seconds);
//        intent.putExtra("steps", steps);
//        intent.putExtra("distance", distance);
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
