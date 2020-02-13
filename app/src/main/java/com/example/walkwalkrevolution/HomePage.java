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

public class HomePage extends AppCompatActivity {

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    private TextView stepCountText;
    private StepCountActivity sc;

    private FitnessService fitnessService;
    public String fitnessServiceKey = "GOOGLE_FIT";
    private long stepCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Create Fitness Service
        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(HomePage hp) {
                return new GoogleFitAdapter(hp);
            }
        });

        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        //fitnessService.setup();

        //GoogleFitAdapter g = new GoogleFitAdapter(this);
        //g.setup();

        // Starts AsyncTask for step counter
        stepCountText = findViewById(R.id.stepCountText);
        System.out.println("");
        sc = new StepCountActivity(stepCountText, fitnessService, this);

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

        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        firstLogin(settings);
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
        //pref.edit().putBoolean("my_first_time", true).commit();
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

}

