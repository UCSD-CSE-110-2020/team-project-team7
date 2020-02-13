package com.example.walkwalkrevolution;

import android.os.AsyncTask;
import android.widget.TextView;

import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.fitness.GoogleFitAdapter;


public class StepCountActivity extends AsyncTask<String, String, String> {

    private FitnessService fs;
    private HomePage homePage;
    private boolean testStep;


    public StepCountActivity(FitnessService fs, HomePage hp, boolean testStep) {

        this.fs = fs;
        this.homePage = hp;
        this.testStep = testStep;
    }

    @Override
    protected void onPreExecute() {fs.setup();}

    @Override
    protected String doInBackground(String... strings) {
        do {
            // Don't update step count from publish if testing
            if(!testStep) {publishProgress(String.valueOf(homePage.getStepCount()));}

            try {
                Thread.sleep(1000);
                fs.updateStepCount();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while(!testStep);

        return null;
    }

    @Override
    protected void onProgressUpdate(String... text) {homePage.setStepCount(Long.parseLong(text[0]));}

}

