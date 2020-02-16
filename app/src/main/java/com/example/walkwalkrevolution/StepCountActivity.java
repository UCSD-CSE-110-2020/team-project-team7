package com.example.walkwalkrevolution;


import android.os.AsyncTask;
import android.util.Log;

import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.GoogleFitAdapter;

import java.math.RoundingMode;
import java.text.DecimalFormat;


public class StepCountActivity extends AsyncTask<String, String, String> {

    public UpdateStepTextView updateStep;
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
        // TODO commented this out bc we dont want to setup a new google API
        //fs.setup();
    }

    @Override
    protected String doInBackground(String... strings) {
        /*
        while(true) {
            publishProgress(String.valueOf(updateStep.getStepCount()),
                    String.valueOf(updateStep.getMiles()));
            // TODO actual line used for googefit api
            //gfa.updateStepCount();
            // TODO DELETE LATER HARD CODE STEPS
            updateStep.setStepCount(updateStep.getStepCount() + 100);
            double stepCountdouble = (double)updateStep.getStepCount();
            miles = (Math.floor((stepCountdouble / updateStep.getStepsPerMile()) * 100)) / 100;
            updateStep.setMiles(miles);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        */

        do {
            // Don't update step count from publish if testing
            if(!testStep) {
                //fs.updateStepCount();
                publishProgress(String.valueOf(updateStep.getStepCount()), String.valueOf(updateStep.getMiles()));
                Log.d("steps tracker", String.valueOf(updateStep.getStepCount()));
                updateStep.setStepCount(updateStep.getStepCount() + 100);
                double stepCountdouble = (double)updateStep.getStepCount();
                miles = (Math.floor((stepCountdouble / updateStep.getStepsPerMile()) * 100)) / 100;
                updateStep.setMiles(miles);
            }

            try {
                Thread.sleep(1000);
                //fs.updateStepCount();
            } catch (InterruptedException e) {
                e.printStackTrace();
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

