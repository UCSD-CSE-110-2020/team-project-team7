package com.example.walkwalkrevolution;

import android.os.AsyncTask;
import android.widget.TextView;

import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.fitness.GoogleFitAdapter;


public class StepCountActivity extends AsyncTask<String, String, String> {


    private TextView textSteps;
    private FitnessService fs;
    private HomePage homePage;
    private boolean testStep;


    public StepCountActivity(TextView tv, FitnessService fs, HomePage hp, boolean testStep) {

        this.textSteps = tv;
        this.fs = fs;
        this.homePage = hp;
        this.testStep = testStep;
    }

    @Override
    protected void onPreExecute() {

        //fitnessService.setup();
        //gfa.setup();
        fs.setup();
    }

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
    protected void onProgressUpdate(String... text) {
        //textSteps.setText(text[0]);
        // Set step count text from home
        homePage.setStepCount(Long.parseLong(text[0]));
    }

}

