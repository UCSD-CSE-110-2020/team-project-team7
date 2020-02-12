package com.example.walkwalkrevolution;


import android.os.AsyncTask;
import android.util.Log;

import com.example.walkwalkrevolution.fitness.GoogleFitAdapter;


public class StepCountActivity extends AsyncTask<String, String, String> {

    public UpdateStepTextView updateStep;
    private GoogleFitAdapter gfa;
    double miles;

    public StepCountActivity(GoogleFitAdapter gfa) {
        this.gfa = gfa;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        miles = 0;
    }

    @Override
    protected String doInBackground(String... strings) {
        while(true) {
            publishProgress(String.valueOf(updateStep.getStepCount()),
                            String.valueOf(miles));
            try {
                Thread.sleep(5000);
                gfa.updateStepCount();
                miles = Math.floor((updateStep.getStepCount() / updateStep.getStepsPerMile()) * 100) / 100;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onProgressUpdate(String... text) {
        updateStep.updateStepView(text[0]);
        updateStep.updatesMilesView(text[1]);
    }

    //public double calculatedMiles(long steps, )

}

