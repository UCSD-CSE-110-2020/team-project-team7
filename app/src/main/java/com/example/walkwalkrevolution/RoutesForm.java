package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeSet;

public class RoutesForm extends AppCompatActivity {

    private static final String TAG = "RoutesForm";

    // Edit Texts
    private EditText routeNameEditText, startingPEditText, minutesEditText, secondsEditText;
    private TextView dateDisplayTextView, stepsView, distanceView;

    // Booleans for toggle Buttons for setting extra features and Toggle Buttons themselves
    private boolean isFlat, isStreet, isLoop, isEven;
    private ToggleButton flatOrHillyButton, streetOrTrailButton, loopOrOutBackButton,
            evenOrUnevenButton;
    private Spinner difficultySpinner;

    // Data obtained from Walk/Run session
    private int steps, minutes, seconds;
    private float distance;

    // NOtes taken for the Route
    private String notes = "";

    // Request code constants
    private final int REQUEST_DATE = 1;
    private final int REQUEST_NOTES = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_form);

        Log.d(TAG, "Starting Form SetUp");
        formSetUp();

        // Handle onClickListeners for Buttons
        Button saveButton = (Button) findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                attemptToSave();
            }
        });
        Button cancelButton = (Button) findViewById(R.id.CancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancel();
            }
        });
        Button notesButton = (Button) findViewById(R.id.notesButton);
        notesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openNotesActivity(v);
            }
        });

    }

    /**
     * Initalize instance vars for most Activity objects. Set up the date. Find out which
     * intent called this Activity.
     */
    private void formSetUp(){
        // Get ids of EditTexts and TextViews
        routeNameEditText = (EditText) findViewById(R.id.routeNameEditText);
        startingPEditText = (EditText) findViewById(R.id.startingPEditText);
        minutesEditText = (EditText) findViewById(R.id.minutesEditText);
        secondsEditText = (EditText) findViewById(R.id.secondsEditText);
        dateDisplayTextView = (TextView) findViewById(R.id.dateDisplayTextView);
        stepsView = (TextView) findViewById(R.id.stepsNumView);
        distanceView = (TextView) findViewById(R.id.distanceNumView);

        // Set up ToggleButton listeners
        setUpToggleFeatures();

        // Setting the date, automatically set it to the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date currentTime = Calendar.getInstance().getTime();
        String date = dateFormat.format(currentTime);
        dateDisplayTextView.setText(date);

        checkIntent();
    }

    /**
     * Find on which intent opened this Activity and fill up the page correspondingly.
     */
    private void checkIntent(){
        Log.d(TAG, "Checking for Intents");

        // Check which intent we are from
        Intent fromIntent = this.getIntent();
        if (fromIntent != null) {
            String fromIntentStr;
            try {

                fromIntentStr = fromIntent.getExtras().getString("From_Intent");
                fromIntentStr.length();

            }catch(NullPointerException e) {
                Log.d(TAG, "Null Pointer Exception Caught");
                return;
            }

            switch (fromIntentStr) {
                case WalkRunSession.WALK_RUN_INTENT:
                    intentFromWalkRunSession(fromIntent);
                    Log.d(TAG, "Intent Found: Walk/Run Session");
                    break;
                case RecyclerViewAdapter.PREVIEW_DETAILS_INTENT:
                    intentFromRoutesDetails();
                    Log.d(TAG, "Intent Found: Details Preview from Routes Page");
                    break;
                case RoutesList.ROUTE_CREATE_INTENT:
                    intentFromRoutesCreation();
                    Log.d(TAG, "Intent Found: Route Creation from Routes Page");
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Currently creating a new Route.
     */
    private void intentFromRoutesCreation(){
        stepsView.setText(steps + " s");
        distanceView.setText(distance + " mi");
    }

    /**
     * Viewing a previously made Route from Routes page.
     */
    private void intentFromRoutesDetails(){
        Route routeToDetail = TreeSetManipulation.getSelectedRoute();

        //prefill all required information
        routeNameEditText.setText(routeToDetail.name);
        startingPEditText.setText(routeToDetail.startingPoint);
        minutesEditText.setText(String.format("%02d", routeToDetail.minutes));
        secondsEditText.setText(String.format("%02d", routeToDetail.seconds));
        stepsView.setText(routeToDetail.steps + " s");
        distanceView.setText(routeToDetail.distance + " mi");
        dateDisplayTextView.setText(routeToDetail.date);

        //disable editing for minutes, steps, and distance
        minutesEditText.setEnabled(false);
        secondsEditText.setEnabled(false);
        stepsView.setEnabled(false);
        distanceView.setEnabled(false);

        // Load extra features info and update buttons accordingly
        isFlat = routeToDetail.isFlat;
        isLoop = routeToDetail.isLoop;
        isStreet = routeToDetail.isStreet;
        isEven = routeToDetail.isEven;
        updateToggleFeatures();

        // Load notes
        notes = routeToDetail.notes;
        // Load difficulty
        loadDifficulty(routeToDetail);
    }

    /**
     * Stopped a walk Run Session.
     */
    private void intentFromWalkRunSession(Intent fromIntent){
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

        //Walk/Run Session started from Routes page
       if(TreeSetManipulation.getSelectedRoute() != null){
           intentFromRoutesStartButton();
       }

    }

    private void intentFromRoutesStartButton(){
        Route routeBeingWalked = TreeSetManipulation.getSelectedRoute();

        //prefill information for everything but Steps, Distance, and Time
        routeNameEditText.setText(routeBeingWalked.name);
        startingPEditText.setText(routeBeingWalked.startingPoint);

        // Load extra features info and update buttons accordingly
        isFlat = routeBeingWalked.isFlat;
        isLoop = routeBeingWalked.isLoop;
        isStreet = routeBeingWalked.isStreet;
        isEven = routeBeingWalked.isEven;
        updateToggleFeatures();

        // Load notes
        notes = routeBeingWalked.notes;
        // Load difficulty
        loadDifficulty(routeBeingWalked);
    }

    /**
     * Save button behavior. If routeName or startingPoint are empty,
     * display error and return. Otherwise, create a Route instance
     * and save it in SharedPref. Then go to Routes List Activity.
     */
    private void attemptToSave() {
        Log.d(TAG, "Save Button Clicked --> attempting to save");

        if(errorCheckingRequiredFields()){
            Log.d(TAG, "Errors Found");
            return;
        }

        Log.d(TAG, "No Errors Found");
        Route savedRoute = entriesAsRouteObject();

        Intent intent = new Intent(this, RoutesList.class);
        SharedPreferences sharedPreferences =
                getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE);


        //Anything involving the Routes Page executes here
        if(TreeSetManipulation.getSelectedRoute() != null){
            boolean wasUpdated = TreeSetManipulation.updateRouteInTreeSet(sharedPreferences,  savedRoute);
            //updatedEntry is not a duplicate entry (other than the one it was modifying)
            if(wasUpdated) {
                Log.d(TAG, "Entry Successfully Updated - Not a duplicate");
                Toast.makeText(this, "Route Successfully Modified", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                return;
            }
        }
        //Start button from Home page was pressed & routes entry is not a duplicate
        else if(TreeSetManipulation.addRouteInTreeSet(sharedPreferences, savedRoute)){
            Log.d(TAG, "Entry Successfully Created - Not a duplicate");
            Toast.makeText(this,"Route Successfully Added" , Toast.LENGTH_SHORT).show();
            startActivity(intent);
            return;
        }
        Log.d(TAG, "Entry Rejected - Duplicate");
        //Any instance of duplicates entries from either Start buttons
        Toast.makeText(this, "Route Entry Already Exists", Toast.LENGTH_SHORT).show();
    }

    /**
     * Get the filled in entries and returns a newly created Route object bsaed on entries.
     */
    private Route entriesAsRouteObject(){
        String routeName = routeNameEditText.getText().toString();
        String startingPoint = startingPEditText.getText().toString();

        Route savedRoute = new Route(routeName, startingPoint, steps, distance);
        savedRoute.setDate(dateDisplayTextView.getText().toString());
        savedRoute.setDuration(minutes, seconds);

        // Save extra features
        savedRoute.setIsFlat(isFlat);
        savedRoute.setIsLoop(isLoop);
        savedRoute.setIsStreet(isStreet);
        savedRoute.setIsEven(isEven);
        savedRoute.setNotes(notes);
        savedRoute.setDifficulty(difficultySpinner.getSelectedItem().toString());
        return savedRoute;
    }

    /**
     * Error checking for required feels, such as routeName and startingPoint.
     */
    private boolean errorCheckingRequiredFields(){
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
        return error;
    }

    /**
     * Cancel button behavior. Go to Routes List Activity without saving data.
     */
    private void cancel() {
        Log.d(TAG, "Cancel Button clicked --> Redirected to Routes Page");
        Intent intent = new Intent(this, RoutesList.class);
        startActivity(intent);
    }

    /**
     * Opens the Set Date Activity so the user can change the Route's date.
     */
    public void openSetDate(View view) {
        Intent intent = new Intent(this, SetDate.class);
        intent.putExtra("date", dateDisplayTextView.getText().toString());
        startActivityForResult(intent, REQUEST_DATE);
    }

    /**
     * Opens the Notes Activity so the user can write their own notes.
     */
    public void openNotesActivity(View view) {
        Intent intent = new Intent(this, NotesPage.class);
        intent.putExtra("notes", notes);
        startActivityForResult(intent, REQUEST_NOTES);
    }

    /**
     * Handles coming back from the SetDate Activity (Saving the new date chosen on the DatePIcker).
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_DATE:
                if(resultCode == RESULT_OK) {
                    String dateStr = data.getStringExtra("newDate");
                    dateDisplayTextView.setText(dateStr);
                }
                break;
            case REQUEST_NOTES:
                if(resultCode == RESULT_OK) {
                    notes = data.getStringExtra("newNotes");
                }
                break;
        }
    }

    /**
     * Set up the ToggleButtons and their onClickListeners.
     */
    private void setUpToggleFeatures() {
        // Get ids of ToggleButtons
        flatOrHillyButton = (ToggleButton) findViewById(R.id.flatOrHillyButton);
        streetOrTrailButton = (ToggleButton) findViewById(R.id.streetOrTrailButton);
        loopOrOutBackButton = (ToggleButton) findViewById(R.id.loopOrOutBackButton);
        evenOrUnevenButton = (ToggleButton) findViewById(R.id.evenOrUnevenButton);

        difficultySpinner = (Spinner) findViewById(R.id.difficultySpinner);

        flatOrHillyButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isFlat = isChecked;
            }
        });
        streetOrTrailButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isStreet = isChecked;
            }
        });
        loopOrOutBackButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isLoop = isChecked;
            }
        });
        evenOrUnevenButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isEven = isChecked;
            }
        });

    }

    /**
     * Update ToggleButtons based on received data (current values of booleans).
     */
    private void updateToggleFeatures() {
        flatOrHillyButton.setChecked(isFlat);
        streetOrTrailButton.setChecked(isStreet);
        loopOrOutBackButton.setChecked(isLoop);
        evenOrUnevenButton.setChecked(isEven);
    }

    /**
     * Updates the difficulty spinner based on the Route.
     */
    private void loadDifficulty(Route route) {
        String difficulty = route.difficulty;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.difficulties, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(difficulty);
        difficultySpinner.setSelection(spinnerPosition);
    }
}