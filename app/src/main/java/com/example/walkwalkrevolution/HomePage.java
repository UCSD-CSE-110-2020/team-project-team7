package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.GoogleFitAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity implements UpdateStepTextView {

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    public final String TAG = "Home Page";

    public TextView milesText;
    public long stepCount;
    public double milesCount;
    public double stepsPerMile;

    public FitnessService fitnessService;
    private String fitnessServiceKey = "GOOGLE_FIT";
    public boolean testStep = true;
    public StepCountActivity sc;
    public TextView stepCountText;
    // TODO TEST BUTTON


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Log.d("HOMEPAGE ONCREATE", "creating homepage");
        //launchFirstSession();

        // retrieve height;
        final SharedPreferences getHeight = getSharedPreferences("height", 0);
        int feet = getHeight.getInt("height_ft", 5);
        int inches = getHeight.getInt("height_in", 7);
        int heightInInches = (feet * 12) + inches;
        stepsPerMile = calculateStepsPerMile(heightInInches);

        // Check from String extra if a test FitnessService is being passed
        fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        if(fitnessServiceKey == null) {
            fitnessServiceKey = "GOOGLE_FIT";
            testStep = false;
        }

        // Creates specified FitnessService adapter depending on key
        FitnessServiceFactory.put(fitnessServiceKey, FitnessServiceFactory.create(fitnessServiceKey, this));
        System.out.println("created fitnessservice");
        // Get specified FitnessService using fitnessServiceKey from Blueprint
        fitnessService = FitnessServiceFactory.getFS(fitnessServiceKey);
        fitnessService.setup();

        // Starts AsyncTask for step counter
        stepCountText = findViewById(R.id.stepCountText);
        milesText = findViewById(R.id.distanceCountText);


        // used to start the walk/run activity
        Button launchActivity = (Button) findViewById(R.id.startButt);
        launchActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchSession();
            }
        });

        // used to go to the routes page
        Button routesPage = findViewById(R.id.routesButt);
        routesPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                routesSession();
            }
        });

        // Button that opens mockPage
        Button mockPageButton = (Button) findViewById(R.id.mockPageButton);
        mockPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMockPage();
            }
        });
    }

    @Override
    protected void onStop() {
        Log.d("HOMEPAGE ON STOP", "stopped called");
        super.onStop();
        // stop async task
        sc.cancel(true);
    }

    @Override
    protected void onStart() {
        Log.d("HOMEPAGE ON START", "start called");
        super.onStart();
        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        firstLogin(settings);

        displayLastWalk();
        Log.d("HOMEPAGE ON RESUME", "resume called");

        // Create async task
        sc = new StepCountActivity(fitnessService, testStep);
        sc.updateStep = this;
        SharedPreferences sf = getSharedPreferences("MockSteps" , MODE_PRIVATE);
        long stepsFromMock = sf.getLong("getsteps", -1);
        // if steps were modified from mock page change steps to static mock steps
        if(stepsFromMock != -1) {
            Log.d("INSIDE STEPS FROM WALK != -1", String.valueOf(stepsFromMock));
            setStepCount(stepsFromMock);
            setMiles((Math.floor((stepsFromMock / stepsPerMile) * 100)) / 100);
            updateStepView(String.valueOf(getStepCount()));
            updatesMilesView(String.valueOf(getMiles()));
            sc.turnOffAPI = true;
        }
        sc.execute();
    }

    @Override
    protected void onDestroy() {
        Log.d("HOMEPAGE ON DESTROY", "being destroy");
        super.onDestroy();
        if(!sc.isCancelled()) {
            sc.cancel(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //If authentication was required during google fit setup,
        // this will be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }

    private void displayLastWalk(){
        TextView stepValue = (TextView) findViewById(R.id.stepsValue);
        TextView distanceValue = (TextView) findViewById(R.id.distanceValue);
        TextView timeValue = (TextView) findViewById(R.id.timeValue);
        List<String> list = LastIntentionalWalk.loadLastWalk(getSharedPreferences(LastIntentionalWalk.SHARED_PREFS_INTENTIONAL_WALK, MODE_PRIVATE));
        if(list == null) {
            stepValue.setText("0");
            distanceValue.setText("0");
            timeValue.setText("0");
        } else {
            stepValue.setText(list.get(0));
            distanceValue.setText(list.get(1));
            timeValue.setText(list.get(2));
        }
    }


    /**
     * updateStepView, setStepCount, getStepCount implement UpdateStepInterface
     * setStepCount is called within GoogleFitAdapter.java --> updates stepCount to amount of steps
     * getStepCount is called within StepCountActivity.java --> get stepCount
     * updateStepView is called within StepCountActivity.java --> update TextView to stepCount
     */
    public void updateStepView(String str) { stepCountText.setText(str); }

    public void setStepCount(long sc) { stepCount = sc; }

    public long getStepCount() { return stepCount; }

    public void updatesMilesView(String str) { milesText.setText(str); }

    public void setMiles(double mi) { milesCount = mi; }

    public double getMiles() { return milesCount; }

    public double getStepsPerMile() { return this.stepsPerMile; }

    /**
     * used to launch the walk/run session
     */
    public void routesSession(){
        Intent intent = new Intent(this, RoutesList.class);
        startActivity(intent);
    }

    /**
     * used to launch the walk/run session
     */
    public void launchSession(){
        Log.d("HOMEPAGE LAUNCH WALK SESSION", "launching walkrunsession");
        Intent intent = new Intent(this, WalkRunSession.class);
        intent.putExtra("stepsPerMileFromHome", stepsPerMile);
        intent.putExtra(FITNESS_SERVICE_KEY, fitnessServiceKey);
        SharedPreferences sf = getSharedPreferences("MockSteps" , 0);
        //sf.edit().putLong("getsteps", -1).apply();
        startActivity(intent);
    }

    /**
     * launched only once, when the app is opened for the first time
     */
    public void launchFirstSession(){
        Intent intent = new Intent(this, HeightForm.class);
        startActivity(intent);
    }

    /**
     * first time the user opens the app
     */
    public void firstLogin(SharedPreferences pref){
        if (pref.getBoolean("my_first_time", true)) {
            //the app is being launched for first time
            launchFirstSession();
            // record the fact that the app has been started at least once
            pref.edit().putBoolean("my_first_time", false).commit();
        }
    }

    public int calculateStepsPerMile(int heightInInches) {
        double strideLengthFeet = (heightInInches * 0.413) / 12;
        return (int)(5280 / strideLengthFeet);
    }

    /**
     * launches the mock page
     */
    private void launchMockPage() {
        Log.d("IN LAUNCH MOCKPAGE", String.valueOf(stepCount));
        Intent intent = new Intent(this, MockPage.class);
        intent.putExtra("stepCountFromHome", stepCount);
        startActivity(intent);
    }
}

