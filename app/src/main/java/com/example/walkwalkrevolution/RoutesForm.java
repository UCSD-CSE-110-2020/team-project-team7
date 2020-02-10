package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeSet;

public class RoutesForm extends AppCompatActivity {

    // Edit Texts and Buttons
    private Button saveButton;
    private Button cancelButton;
    private EditText routeNameEditText;
    private EditText startingPEditText;
    private EditText minutesEditText;
    private EditText secondsEditText;
    private TextView dateDisplayTextView;
    private TextView stepsView;
    private TextView distanceView;

    // Data obtained from Walk/Run session
    private int steps, minutes, seconds;
    private float distance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_form);

        // Get ids of EditTexts and TextViews
        routeNameEditText = (EditText) findViewById(R.id.routeNameEditText);
        startingPEditText = (EditText) findViewById(R.id.startingPEditText);
        minutesEditText = (EditText) findViewById(R.id.minutesEditText);
        secondsEditText = (EditText) findViewById(R.id.secondsEditText);
        dateDisplayTextView = (TextView) findViewById(R.id.dateDisplayTextView);
        stepsView = (TextView) findViewById(R.id.stepsNumView);
        distanceView = (TextView) findViewById(R.id.distanceNumView);

        // Setting the date, automatically set it to the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date currentTime = Calendar.getInstance().getTime();
        String date = dateFormat.format(currentTime);
        dateDisplayTextView.setText(date);

        // Check which intent we are from
        Intent fromIntent = this.getIntent();
        if (fromIntent != null) {

            try {
                // Try to get which intent we are from
                String fromIntentStr = fromIntent.getExtras().getString("From_Intent");

                // Fill up most EditText fields if we are from Walk/Run
                if(fromIntentStr != null && fromIntentStr.equals("From_Walk/Run"))
                {
                    // Get data from walk/run session
                    steps = fromIntent.getIntExtra("steps", 0);
                    distance = fromIntent.getFloatExtra("distance", 0);
                    minutes = fromIntent.getIntExtra("minutes", 0);
                    seconds = fromIntent.getIntExtra("seconds", 0);

                    // Display data from walk/run session
                    //String.format("%d:%02d", minutes, seconds);
                    minutesEditText.setText(String.format("%02d", minutes));
                    secondsEditText.setText(String.format("%02d", seconds));
                    stepsView.setText(steps + " s");
                    distanceView.setText(distance + " mi");

                    Route routeBeingWalked = TreeSetManipulation.getRouteBeingWalked();
                    if(routeBeingWalked != null){
                        routeNameEditText.setText(routeBeingWalked.name);
                        startingPEditText.setText(routeBeingWalked.startingPoint);
                    }

                } else {
                    // From plus sign, Route List instead, display defaults for steps and distance
                    // They will both be 0 by default
                    stepsView.setText(steps + " s");
                    distanceView.setText(distance + " mi");
                }

            } catch(NullPointerException e) {
            }

        }

        // Handle onClickListeners for Save and Cancel Buttons
        saveButton = (Button) findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                attemptToSave();
            }
        });
        cancelButton = (Button) findViewById(R.id.CancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancel();
            }
        });

    }

    /**
     * Save button behavior. If routeName or startingPoint are empty,
     * display error and return. Otherwise, create a Route instance
     * and save it in SharedPref. Then go to Routes List Activity.
     */
    private void attemptToSave() {
        // Get fields in EditTexts and TextViews
        String routeName = routeNameEditText.getText().toString();
        String startingPoint = startingPEditText.getText().toString();

        String inputMinutes = minutesEditText.getText().toString();
        if (!inputMinutes.equals(""))  {
            minutes = Integer.parseInt(inputMinutes);
        }
        String inputSeconds = secondsEditText.getText().toString();
        if (!inputSeconds.equals("")) {
            seconds = Integer.parseInt(inputSeconds);
        }

        // Handle error for required fields, route name and starting point
        boolean error = false;
        if (routeName.matches("")) {
            // Display error for required field
            routeNameEditText.setError("Required field");
            error = true;
        }
        if (startingPoint.matches("")) {
            // Display error for required field
            startingPEditText.setError("Required field");
            error = true;
        }
        if (seconds > 60) {
            // Display error for incorrectly filled field
            secondsEditText.setError("Specified seconds must be within range 0-60");
            seconds = 0;
            error = true;
        }
        if (error) {
            return;
        }

        Route savedRoute = new Route(routeName, startingPoint, steps, distance);
        savedRoute.setDate(dateDisplayTextView.getText().toString());
        savedRoute.setDuration(minutes, seconds);

        Route walkedRoute = TreeSetManipulation.getRouteBeingWalked();
        if(walkedRoute != null){
            Intent intent = new Intent(this, RoutesList.class);
            SharedPreferences sharedPreferences =
                    getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE);
            TreeSetManipulation.updateTreeSet(sharedPreferences, new TreeSetComparator(), walkedRoute, savedRoute);
            Toast.makeText(this,"Route Successfully Added" , Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
        else{
            // Save the route into the TreeSet routes list back in sharedPrefs
            SharedPreferences sharedPreferences =
                    getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE);

            boolean routeWasSaved = TreeSetManipulation.addTreeSet(sharedPreferences, new TreeSetComparator(), savedRoute);
            // TreeSaveManipulation.updateTreeSet(Route route, sharedPreferences);
            if(routeWasSaved) {
                Intent intent = new Intent(this, RoutesList.class);
                Toast.makeText(this,"Route Successfully Added" , Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Route Entry Already Exists", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * Cancel button behavior. Go to Routes List Activity without saving data.
     */
    private void cancel() {
        Intent intent = new Intent(this, RoutesList.class);
        startActivity(intent);
    }

    /**
     * Opens the Set Date Activity so the user can change the Route's date.
     */
    public void openSetDate(View view) {
        Intent intent = new Intent(this, SetDate.class);
        intent.putExtra("date", dateDisplayTextView.getText().toString());
        startActivityForResult(intent, 1);
    }

    /**
     * Handles coming back from the SetDate Activity (Saving the new date chosen on the DatePIcker).
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String dateStr = data.getStringExtra("newDate");
                dateDisplayTextView.setText(dateStr);
            }
        }
    }

}