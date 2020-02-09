package com.example.walkwalkrevolution;

import android.os.AsyncTask;
import android.widget.TextView;

import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.fitness.GoogleFitAdapter;


public class StepCountActivity extends AsyncTask<String, String, String> {


    private TextView textSteps;
    private GoogleFitAdapter gfa;
    private HomePage homePage;


    public StepCountActivity(TextView tv, GoogleFitAdapter gfa, HomePage hp) {

        this.textSteps = tv;
        this.gfa = gfa;
        this.homePage = hp;
    }

    @Override
    protected void onPreExecute() {

        //fitnessService.setup();
        //gfa.setup();
    }

    @Override
    protected String doInBackground(String... strings) {
        while(true) {
            publishProgress(String.valueOf(homePage.getStepCount()));
            try {
                Thread.sleep(1000);
                //fitnessService.updateStepCount();
                gfa.updateStepCount();
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

}

