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

import org.honorato.multistatetogglebutton.MultiStateToggleButton;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;


/**
 * Handles interactions with editing details of and saving a
 * Routes entry on the Routes Form Activity.
 */
public class RoutesForm extends AppCompatActivity {

    // Constant for logging
    private static final String TAG = "RoutesForm";
    // Request code constants
    private final int REQUEST_DATE = 1;
    private final int REQUEST_NOTES = 2;

    // Edit Texts and Xml Objects
    private EditText routeNameEditText, startingPEditText, minutesEditText, secondsEditText;
    private TextView dateDisplayTextView, stepsView, distanceView;
    private MultiStateToggleButton[] optionalInfo = new MultiStateToggleButton[5];

    private int[] toggledButtons = new int[5];
    private String[] toggledButtonsStr = new String[5];

    // Data obtained from Walk/Run session
    private long steps;
    private int minutes, seconds;
    private double distance;

    // NOtes taken for the Route
    private String notes = "";


    // SETTING UP GENERAL UI ELEMENTS AND BEHAVIOR -------------------------------------------------
    private boolean intentFromWalkRunSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_form);

        Log.d(TAG, "Starting formSetUp");
        formSetUp();
        Log.d(TAG, "Finished formSetup");
    }

    /**
     * Initialize instance vars for most Activity objects. Set up the date. Find out which
     * intent called this Activity.
     */
    private void formSetUp(){
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

        // Get ids of EditTexts and TextViews
        routeNameEditText = (EditText) findViewById(R.id.routeNameEditText);
        startingPEditText = (EditText) findViewById(R.id.startingPEditText);
        minutesEditText = (EditText) findViewById(R.id.minutesEditText);
        secondsEditText = (EditText) findViewById(R.id.secondsEditText);
        dateDisplayTextView = (TextView) findViewById(R.id.dateDisplayTextView);
        stepsView = (TextView) findViewById(R.id.stepsNumView);
        distanceView = (TextView) findViewById(R.id.distanceNumView);

        // Set up ids for MultiState toggles in optional info list and display text
        optionalInfo[0] = (MultiStateToggleButton) this.findViewById(R.id.hillyToggle);
        optionalInfo[0].setElements(new CharSequence[]{"Hilly", "Flat"});
        optionalInfo[1] = (MultiStateToggleButton) this.findViewById(R.id.loopToggle);
        optionalInfo[1].setElements(new CharSequence[]{"Loop", "Out-Back"});
        optionalInfo[2] = (MultiStateToggleButton) this.findViewById(R.id.surfaceToggle);
        optionalInfo[2].setElements(new CharSequence[]{"Even", "Uneven"});
        optionalInfo[3] = (MultiStateToggleButton) this.findViewById(R.id.streetToggle);
        optionalInfo[3].setElements(new CharSequence[]{"Street", "Trails"});
        optionalInfo[4] = (MultiStateToggleButton) this.findViewById(R.id.difficultyToggle);
        optionalInfo[4].setElements(new CharSequence[]{"Easy", "Medium", "Hard"});

        // Set up listeners for all toggle buttons to update information in the toggleButtons array
        for(int i=0; i < optionalInfo.length; i++){
            // Only allow one choice per toggle button
            optionalInfo[i].enableMultipleChoice(false);

            final int j = i;
            optionalInfo[i].setOnValueChangedListener(new org.honorato.multistatetogglebutton.ToggleButton.OnValueChangedListener() {
                @Override
                public void onValueChanged(int value) {
                    Toast.makeText(getApplicationContext(), "ValueChanged: " + value, Toast.LENGTH_SHORT).show();
                    setToggledButtonInfo(j, value);
                }
            });
        }

        // Setting the date, automatically set it to the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date currentTime = Calendar.getInstance().getTime();
        String date = dateFormat.format(currentTime);
        dateDisplayTextView.setText(date);

        // Find out which intent we are from
        checkIntent();
    }

    /**
     * Set up one toggle button to update information in the toggleButtons array
     * every time it is toggled. The toggle button is specified by its index in the array.
     */
    private void setToggledButtonInfo(int toggleIndex, int value) {
        toggledButtons[toggleIndex] = value + 1;
        toggledButtonsStr[toggleIndex] = "" + optionalInfo[toggleIndex].getTexts()[value];
    }


    // HANDLING BEHAVIORS FOR COMING FROM INTENTS --------------------------------------------------

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
                    intentFromWalkRunSession = true;
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
     * Currently creating a brand new Route (from the plus button on Routes page).
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

        // Extract steps, distance, time into instance vars and display them
        steps = routeToDetail.steps;
        distance = routeToDetail.distance;
        minutes = routeToDetail.minutes;
        seconds = routeToDetail.seconds;
        displayStepsDistanceTime();

        // prefill route name, starting point, and date
        routeNameEditText.setText(routeToDetail.name);
        if (routeToDetail.startingPoint != null) {
            startingPEditText.setText(routeToDetail.startingPoint);
        }
        dateDisplayTextView.setText(routeToDetail.date);

        // Load notes
        notes = routeToDetail.notes;

        //disable editing for minutes, steps, and distance
        minutesEditText.setEnabled(false);
        secondsEditText.setEnabled(false);
        stepsView.setEnabled(false);
        distanceView.setEnabled(false);

        displayToggleInfo(routeToDetail);
    }

    /**
     * Display toggle button information.
     */
    private void displayToggleInfo(Route route){
        try {
            for (int i = 0; i < route.optionalFeatures.length; i++) {
                int value = route.optionalFeatures[i];

                // if button was toggled, set to toggled state
                if (value != 0) {
                    optionalInfo[i].setValue(value - 1);
                }
            }
        }
        catch(Exception e){}
    }

    /**
     * Based on instance vars, display steps, distance, and time.
     */
    private void displayStepsDistanceTime() {
        stepsView.setText(steps + " steps");
        distanceView.setText(distance + " miles");
        minutesEditText.setText(String.format("%02d", minutes));
        secondsEditText.setText(String.format("%02d", seconds));
        Log.d(TAG, "Updating the display for steps, distance, and time.");
    }

    /**
     * Stopped a walk Run Session.
     */
    private void intentFromWalkRunSession(Intent fromIntent){
        // Get data from walk/run session
        steps = fromIntent.getLongExtra("steps", 0);
        distance = fromIntent.getDoubleExtra("distance", 0);
        minutes = fromIntent.getIntExtra("minutes", 0);
        seconds = fromIntent.getIntExtra("seconds", 0);

        displayStepsDistanceTime();

        // Walk/Run Session started from Routes page, not from Home page
        if(TreeSetManipulation.getSelectedRoute() != null) {
            intentFromRoutesStartButton();
        }

    }

    /**
     * Prefilling some information for a session that is started from the Routes page.
     */
    private void intentFromRoutesStartButton(){
        Route routeBeingWalked = TreeSetManipulation.getSelectedRoute();

        // Prefill information for everything but Steps, Distance, and Time
        routeNameEditText.setText(routeBeingWalked.name);
        startingPEditText.setText(routeBeingWalked.startingPoint);

        // Load notes and display toggle info
        notes = routeBeingWalked.notes;
        displayToggleInfo(routeBeingWalked);
    }


    // BEHAVIOR FOR TRYING TO SAVE THE ROUTES ENTRY ------------------------------------------------

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
                lastIntentionalWalkUpdate();
                Toast.makeText(this, "Route Successfully Modified", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                return;
            }
        }
        //Start button from Home page was pressed & routes entry is not a duplicate
        else if(TreeSetManipulation.addRouteInTreeSet(sharedPreferences, savedRoute)){
            Log.d(TAG, "Entry Successfully Created - Not a duplicate");
            lastIntentionalWalkUpdate();
            Toast.makeText(this,"Route Successfully Added" , Toast.LENGTH_SHORT).show();
            startActivity(intent);
            return;
        }
        Log.d(TAG, "Entry Rejected - Duplicate");
        //Any instance of duplicates entries from either Start buttons
        Toast.makeText(this, "Route Entry Already Exists", Toast.LENGTH_SHORT).show();
    }

    private void lastIntentionalWalkUpdate(){
        Log.d(TAG, "Updating the last intentional walk.");
        if(intentFromWalkRunSession){
            SharedPreferences prefs = getSharedPreferences(LastIntentionalWalk.SHARED_PREFS_INTENTIONAL_WALK, MODE_PRIVATE);
            List<String> list = new ArrayList<>();
            list.add(""+steps);
            list.add(""+distance);
            if(seconds < 10){
                list.add(minutes + ":0" +  seconds);
            }
            else{
                list.add(minutes + ":" + seconds);
            }
            LastIntentionalWalk.saveLastWalk(prefs, list);
        }
    }

    /**
     * Get the filled in entries and returns a newly created Route object based on form entries.
     */
    private Route entriesAsRouteObject(){
        Log.d(TAG, "Successfully passed error checking. Now saving this Route.");

        String routeName = routeNameEditText.getText().toString();
        String startingPoint = startingPEditText.getText().toString();

        Route savedRoute = new Route(routeName, startingPoint, steps, distance);

        savedRoute.setDate(dateDisplayTextView.getText().toString());
        savedRoute.setDuration(minutes, seconds);
        savedRoute.setOptionalFeatures(toggledButtons);
        savedRoute.setOptionalFeaturesStr(toggledButtonsStr);
        savedRoute.setNotes(notes);

        return savedRoute;
    }

    /**
     * Error checking and formatting for required feels, such as routeName and startingPoint.
     */
    private boolean errorCheckingRequiredFields(){
        Log.d(TAG, "Checking entry fields for errors...");

        // Get fields in EditTexts and TextViews
        String routeName = routeNameEditText.getText().toString();
        String inputMinutes = minutesEditText.getText().toString();
        String inputSeconds = secondsEditText.getText().toString();

        // If minutes and seconds are not empty, parse them into integers
        if (!inputMinutes.equals(""))  {
            minutes = Integer.parseInt(inputMinutes);
        }
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
        if (seconds > 60) {
            // Display error for incorrectly filled field
            secondsEditText.setError("Specified seconds must be within range 0-60");
            seconds = 0;
            error = true;
        }
        return error;
    }


    // BEHAVIOR FOR STARTING NEW INTENTS FROM THE ROUTES FORM --------------------------------------

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
        Log.d(TAG, "Opening SetDate activity...");
        startActivityForResult(intent, REQUEST_DATE);
    }

    /**
     * Opens the Notes Activity so the user can write their own notes.
     */
    public void openNotesActivity(View view) {
        Intent intent = new Intent(this, NotesPage.class);
        intent.putExtra("notes", notes);
        Log.d(TAG, "Opening NotesPage activity...");
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
                    Log.d(TAG, "Saved data from the SetData activity");
                }
                break;
            case REQUEST_NOTES:
                if(resultCode == RESULT_OK) {
                    notes = data.getStringExtra("newNotes");
                    Log.d(TAG, "Saved data from the NotesPage activity");
                }
                break;
        }
    }
}