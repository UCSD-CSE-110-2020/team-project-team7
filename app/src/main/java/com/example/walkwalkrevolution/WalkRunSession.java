package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.walkwalkrevolution.fitness.GoogleFitAdapter;

import java.util.Timer;

public class WalkRunSession extends HomePage {

    private boolean isCancelled = false;
    private long startTime = System.currentTimeMillis();
    private int minutes;
    private int seconds;
    private TextView timerText;
    //private TextView stepCountText;
    //private long stepCount;
    private StepCountActivity sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_run_session);

        // timer
        timerText = findViewById(R.id.timer_text);
        TimerCount runner = new TimerCount();
        String result = timerText.getText().toString();
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,result);

        // steps
        //stepCountText = findViewById(R.id.)
        //stepCount = 0;
        //GoogleFitAdapter googleApi = new GoogleFitAdapter(this);
        //sc = new StepCountActivity(googleApi);
        //sc.updateStep = this;

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

    }

    /**
     * launches to the routes form
     */
    public void launchRouteForm(){
        Intent intent = new Intent(this, RoutesForm.class);

        // Push data to RouteForm
        intent.putExtra("From_Intent", "From_Walk/Run");
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
                long millis = System.currentTimeMillis() - startTime;
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
}
