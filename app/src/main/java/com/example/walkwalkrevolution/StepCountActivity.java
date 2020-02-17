package com.example.walkwalkrevolution;


import android.os.AsyncTask;
import android.util.Log;

import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.GoogleFitAdapter;

import java.math.RoundingMode;
import java.text.DecimalFormat;


/**
 * Handles async for Google Fit API.
 */
public class StepCountActivity extends AsyncTask<String, String, String> {

    public UpdateStepTextView updateStep;
    public boolean turnOffAPI = false;
    private FitnessService fs;
    private  boolean testStep;
    private double miles;

    public StepCountActivity(FitnessService fs, boolean testStep) {
        this.fs = fs;
        this.testStep = testStep;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {

        do {
            // Don't update step count from publish if testing
            if(!testStep) {
                if(!turnOffAPI) {
                    fs.updateStepCount();
                    publishProgress(String.valueOf(updateStep.getStepCount()), String.valueOf(updateStep.getMiles()));
                    Log.d("steps tracker", String.valueOf(updateStep.getStepCount()));
                    // TODO comment below line out if you want to use google api
                    double stepCountdouble = (double) updateStep.getStepCount();
                    miles = (Math.floor((stepCountdouble / updateStep.getStepsPerMile()) * 100)) / 100;
                    updateStep.setMiles(miles);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        } while(!testStep);

        return null;
    }

    @Override
    protected void onProgressUpdate(String... text) {
        updateStep.updateStepView(text[0]);
        updateStep.updatesMilesView(text[1]);
    }

}

