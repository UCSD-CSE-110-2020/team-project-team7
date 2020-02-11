package com.example.walkwalkrevolution;


import android.os.AsyncTask;
import com.example.walkwalkrevolution.fitness.GoogleFitAdapter;


public class StepCountActivity extends AsyncTask<String, String, String> {

    public UpdateStepTextView updateStep;
    private GoogleFitAdapter gfa;

    public StepCountActivity(GoogleFitAdapter gfa) {
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
                            String.valueOf(updateStep.getStepCount() / updateStep.getStepsPerMile()));
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

        updateStep.updateStepView(text[0]);
        updateStep.updatesMilesView(text[1]);
    }

    //public double calculatedMiles(long steps, )

}

