package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.TimedMetaData;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;

public class WalkRunSession extends AppCompatActivity {

    public static final String WALK_RUN_INTENT = "From_Walk/Run";

    private boolean isCancelled = false;
    private long startTime = System.currentTimeMillis();
    private int minutes;
    private int seconds;
    private TextView timerText;
    private TimeData timeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_run_session);


        timerText = findViewById(R.id.timer_text);
        TimerCount runner = new TimerCount();
        String result = timerText.getText().toString();
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,result);


        // button that stops the activity
        Button stopActivity = (Button) findViewById(R.id.stop_btn);

        stopActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
     * launches to the routes form
     */
    public void launchRouteForm(){
        Intent intent = new Intent(this, RoutesForm.class);

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

    /**
     * Handles timing/mock timing.
     */
    private class TimeData  {

        public long mockTime;
        public long mockStartTime;

        private boolean mockTimeSet = false;

        /**
         * Starts mock timing.
         * @param mockTime - the new time to start at
         */
        private void setMockTime(long mockTime) {
            this.mockTime = mockTime;
            mockTimeSet = true;
            mockStartTime = System.currentTimeMillis();
        }

        /**
         * Returns the time or mocked time.
         */
        private long getTime() {
            if (mockTimeSet) {
                return mockTime + (System.currentTimeMillis() - mockStartTime);

            } else {
                return System.currentTimeMillis();
            }
        }
    }
}
