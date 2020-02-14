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

public class HomePage extends AppCompatActivity implements UpdateStepTextView {

    public final String FITNESS_SERVICE_KEY = "GOOGLE_API";
    public final String TAG = "Home Page";
    public StepCountActivity sc;
    public TextView stepCountText;
    public TextView milesText;
    public long stepCount = 0;
    public double milesCount;
    public double stepsPerMile;
    public FitnessService fitnessService;
    // TODO TEST BUTTON
    public Button incrementSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        firstLogin(settings);

        // retrieve height;
        final SharedPreferences getHeight = getSharedPreferences("height", 0);
        int feet = getHeight.getInt("height_ft", 5);
        int inches = getHeight.getInt("height_in", 7);
        int heightInInches = (feet * 12) + inches;
        stepsPerMile = calculateStepsPerMile(heightInInches);

        // Add --> {key: "GOOGLE_FIT", value: new GoogleFitAdapter}
        fitnessService = new GoogleFitAdapter(this);
        FitnessServiceFactory.put(FITNESS_SERVICE_KEY, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create() {
                return fitnessService;
            }
        });

        fitnessService.setup();

        // Starts AsyncTask for step counter
        stepCountText = findViewById(R.id.stepCountText);
        milesText = findViewById(R.id.distanceCountText);
        sc = new StepCountActivity(fitnessService);
        sc.updateStep = this;
        sc.execute();

        // TODO TEST BUTTON FUNCTION
        incrementSteps = findViewById(R.id.testbutton);
        incrementSteps.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setStepCount(getStepCount() + 500);
            }
        });

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
        Intent intent = new Intent(this, WalkRunSession.class);
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
        //pref.edit().putBoolean("my_first_time", true).commit();
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

}

