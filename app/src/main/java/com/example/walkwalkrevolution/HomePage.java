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

public class HomePage extends AppCompatActivity {

    private TextView stepCountText;
    private StepCountActivity sc;

    private FitnessService fitnessService;
    private String fitnessServiceKey = "GOOGLE_FIT";
    private long stepCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);

        //settings.edit().putBoolean("my_first_time", true).commit();

        firstLogin(settings);

        //launchFirstSession();

        displayLastWalk();

        // Create Fitness Service
        stepCount = 0;
        GoogleFitAdapter g = new GoogleFitAdapter(this);
        g.setup();

        // Starts AsyncTask for step counter
        stepCountText = findViewById(R.id.stepCountText);
        sc = new StepCountActivity(stepCountText, g, this);
        sc.execute();

        Button launchActivity = (Button) findViewById(R.id.startButt);
        // used to start the walk/run activity
        launchActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchSession();
            }
        });

        Button routesPage = findViewById(R.id.routesButt);

        routesPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                routesSession();
            }
        });


        //firstLogin(settings);

        // Button that opens mockPage
        Button mockPageButton = (Button) findViewById(R.id.mockPageButton);
        mockPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMockPage();
            }
        });
    }

    private void displayLastWalk(){
        TextView stepValue = (TextView) findViewById(R.id.stepsValue);
        TextView distanceValue = (TextView) findViewById(R.id.distanceValue);
        TextView timeValue = (TextView) findViewById(R.id.timeValue);
        List<String> list = LastIntentionalWalk.loadLastWalk(getSharedPreferences(LastIntentionalWalk.SHARED_PREFS_INTENTIONAL_WALK, MODE_PRIVATE));
        if(list == null){
            list = LastIntentionalWalk.initializeLastWalk(getSharedPreferences(LastIntentionalWalk.SHARED_PREFS_INTENTIONAL_WALK, MODE_PRIVATE));
        }
        stepValue.setText(list.get(0));
        distanceValue.setText(list.get(1));
        timeValue.setText(list.get(2));
    }


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

    public void setStepCount(long sc) {
        stepCount = sc;
    }

    public long getStepCount() {
        return this.stepCount;
    }

    /**
     * launches the mock page
     */
    private void launchMockPage() {
        Intent intent = new Intent(this, MockPage.class);
        startActivity(intent);
    }
}

