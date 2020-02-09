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

public class HomePage extends AppCompatActivity {

    private TextView stepCountText;
    private StepCountActivity sc;
    public static final String TAG = "StepCountActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Starts AsyncTask for step counter
        stepCountText = findViewById(R.id.stepCountText);
        String test = "10";
        sc = new StepCountActivity(stepCountText);
        sc.execute(test);

        Button launchActivity = (Button) findViewById(R.id.startButt);
        // used to start the walk/run activity
        launchActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchSession();
            }
        });

        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        firstLogin(settings);

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
        pref.edit().putBoolean("my_first_time", true).commit();
        if (pref.getBoolean("my_first_time", true)) {
            //the app is being launched for first time
            launchFirstSession();

            // record the fact that the app has been started at least once
            pref.edit().putBoolean("my_first_time", false).commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If authentication was required during google fit setup, this will be called after the user authenticates
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == sc.fitnessService.getRequestCode()) {
                sc.fitnessService.updateStepCount();
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }

}

