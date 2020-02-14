package com.example.walkwalkrevolution;


import android.os.AsyncTask;
import android.util.Log;

import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.GoogleFitAdapter;

import java.math.RoundingMode;
import java.text.DecimalFormat;


public class StepCountActivity extends AsyncTask<String, String, String> {

    public UpdateStepTextView updateStep;
    private FitnessService gfa;
    double miles;

    public StepCountActivity(FitnessService gfa) {
        this.gfa = gfa;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        while(true) {
            publishProgress(String.valueOf(updateStep.getStepCount()),
                    String.valueOf(updateStep.getMiles()));
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
    }

    @Override
    protected void onProgressUpdate(String... text) {
        updateStep.updateStepView(text[0]);
        updateStep.updatesMilesView(text[1]);
    }

}

