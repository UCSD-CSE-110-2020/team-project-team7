package com.example.walkwalkrevolution;

import android.os.AsyncTask;
import android.util.Log;

import com.example.walkwalkrevolution.fitness.FitnessService;

import java.util.Observable;

public class StepCountObservable extends Observable {

    public boolean turnOffAPI = false;
    public boolean homePageSession = true;
    public UpdateStepTextView updateStep;

    private FitnessService fs;
    private StepCountActivity sc;
    private String stepCountStr, mileStr, timerStr;

    public StepCountObservable(FitnessService fs, boolean testStep) {
        Log.d("CHECKING", "stopped called");
        sc = new StepCountActivity(fs, testStep);
        sc.execute();
    }

    public String getStepCountStr() {
        return stepCountStr;
    }

    public String getMileStr() {
        return mileStr;
    }

  //  public String getTimerStr() {
//        return timerStr;
   // }

    public void cancelStepCount() {
        sc.cancel(true);
    }

    public boolean checkSCCancelled() {
        return sc.isCancelled();
    }

    private class StepCountActivity extends AsyncTask<String, String, String> {

        public WalkRunSession wrs;
        public boolean runSession = false;
        private static final String TAG = "WalkRunSession";

       // public UpdateStepTextView updateStep;
       // public boolean turnOffAPI = false;
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

        /**
         * Update step count of GoogleFitAdapter in the background forever.
         * @param strings
         * @return
         */
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

            // Setting up string for Timer in Walk/Run Session
           // String toReturn = "0";
           // publishProgress(String.valueOf(updateStep.getStepCount()),
                  //  String.valueOf(updateStep.getMiles()), toReturn);

            do {
                // Don't update step count from publish if testing
                if(!testStep) {
                    if(!turnOffAPI) {
                        // Create string for Timer
                        //if(!homePageSession) {
                            //toReturn = wrs.makeTimeString();
                            //Log.d(TAG , "DISPLAYED TIME IS: " + toReturn);
                        //}

                        fs.updateStepCount();
                        double stepCountdouble = (double) fs.getStepCount();
                        miles = (Math.floor((stepCountdouble / updateStep.getStepsPerMile()) * 100)) / 100;
                        publishProgress(String.valueOf(fs.getStepCount()), String.valueOf(miles));

                        /*
                        publishProgress(String.valueOf(updateStep.getStepCount()),
                                String.valueOf(updateStep.getMiles()));
                        Log.d("steps tracker", String.valueOf(updateStep.getStepCount()));
                        // TODO comment below line out if you want to use google api
                        double stepCountdouble = (double) updateStep.getStepCount();
                        miles = (Math.floor((stepCountdouble / updateStep.getStepsPerMile()) * 100)) / 100;
                        updateStep.setMiles(miles);
                         */


                    }
                }
                try {
                    //update progress every second
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            } while(!testStep);

            return null;
        }

        /**
         * Updates steps to HomePage every 1 second.
         * @param text
         */
        @Override
        protected void onProgressUpdate(String... text) {
            //updateStep.updateStepView(text[0]);
            //updateStep.updatesMilesView(text[1]);

            // Sets timer text if Walk/Run Session
            //if(runSession) wrs.setTimerText(text[2]);

            // Set count values to Observable
            stepCountStr = text[0];
            mileStr = text[1];
            setChanged();
            notifyObservers();
        }

        /**
         * Adds support for Walk/Run Session to Async activity
         * @param wrs: Walk/Run Session activity
         */
        public void setUpWalkRun(WalkRunSession wrs) {
            this.wrs = wrs;
            runSession = true;
        }
    }
}
