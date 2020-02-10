package com.example.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;

import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.fitness.GoogleFitAdapter;


public class StepCountActivity extends AsyncTask<String, String, String> {

    public UpdateStepTextView updateStep;
    private GoogleFitAdapter gfa;

    public StepCountActivity(GoogleFitAdapter gfa) {
        this.gfa = gfa;
    }

    @Override
    protected void onPreExecute() {

        //fitnessService.setup();
        //gfa.setup();
    }

    @Override
    protected String doInBackground(String... strings) {
        while(true) {
            publishProgress(String.valueOf(updateStep.getStepCount()));
            try {
                Thread.sleep(1000);
                gfa.updateStepCount();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onProgressUpdate(String... text) {
        //textSteps.setText(text[0]);
        updateStep.updateStepView(text[0]);
    }

}

