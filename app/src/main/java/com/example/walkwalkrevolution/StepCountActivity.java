package com.example.walkwalkrevolution;

import android.os.AsyncTask;
import android.widget.TextView;

import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;


public class StepCountActivity extends AsyncTask<String, String, String> {

    public static final String FITNESS_SERVICE_KEY = "GOOGLE_FIT";
    private static final String TAG = "StepCountActivity";
    private TextView textSteps;
    public FitnessService fitnessService;
    private long stepCount;
    //private String fitnessServiceKey;

    public StepCountActivity(TextView tv) {

        this.textSteps = tv;
        //this.fitnessServiceKey = getStringExtra(FITNESS_SERVICE_KEY);
        this.fitnessService = FitnessServiceFactory.create(FITNESS_SERVICE_KEY, this);
    }

    @Override
    protected void onPreExecute() {
        fitnessService.setup();
    }

    @Override
    protected String doInBackground(String... strings) {
        while(true) {
            publishProgress(String.valueOf(stepCount));
            try {
                Thread.sleep(5000);
                fitnessService.updateStepCount();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //strings[0] = String.valueOf(num);

        }
    }

    @Override
    protected void onProgressUpdate(String... text) {
        textSteps.setText(text[0]);
    }

    public void setStepCount(long sc) {
        stepCount = sc;
    }
}

