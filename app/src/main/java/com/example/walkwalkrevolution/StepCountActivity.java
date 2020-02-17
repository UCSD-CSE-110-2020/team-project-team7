package com.example.walkwalkrevolution;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.fitness.GoogleFitAdapter;

/**
 * Updates HomePage information (steps + distance) in the background of the app.
 */
public class StepCountActivity extends AsyncTask<String, String, String> {


    private TextView textSteps;
    private GoogleFitAdapter gfa;
    private HomePage homePage;

    private static final String TAG = "StepCountActivity";


    public StepCountActivity(TextView tv, GoogleFitAdapter gfa, HomePage hp) {
        Log.d(TAG, "StepCountActivity Initialized");
        this.textSteps = tv;
        this.gfa = gfa;
        this.homePage = hp;
    }

    @Override
    protected void onPreExecute() {

        //fitnessService.setup();
        //gfa.setup();
    }

    /**
     * Update step count of GoogleFitAdapter in the background forever.
     * @param strings
     * @return
     */
    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground Started...");
        //always on the background...
        while(true) {
            publishProgress(String.valueOf(homePage.getStepCount()));
            try {
                //update progress every second
                Thread.sleep(1000);
                //fitnessService.updateStepCount();
                gfa.updateStepCount();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //strings[0] = String.valueOf(num);

        }
    }

    /**
     * Updates steps to HomePage every 1 second.
     * @param text
     */
    @Override
    protected void onProgressUpdate(String... text) {
        Log.d(TAG, "Steps Updated: " + text[0]);
        textSteps.setText(text[0]);
    }

}

