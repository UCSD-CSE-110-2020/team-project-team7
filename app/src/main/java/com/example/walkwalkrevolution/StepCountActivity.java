package com.example.walkwalkrevolution;


import android.os.AsyncTask;
import android.util.Log;

import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.GoogleFitAdapter;


public class StepCountActivity extends AsyncTask<String, String, String> {

    public UpdateStepTextView updateStep;
    private FitnessService gfa;

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
            try {
                Thread.sleep(1000);
                publishProgress(String.valueOf(updateStep.getStepCount()),
                                String.valueOf(updateStep.getMiles()));
                //gfa.updateStepCount();
                // TODO DELETE LATER HARD CODE STEPS
                updateStep.setStepCount(updateStep.getStepCount() + 10);
                updateStep.setMiles(Math.floor((updateStep.getStepCount()
                        / updateStep.getStepsPerMile()) * 100) / 100);
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

