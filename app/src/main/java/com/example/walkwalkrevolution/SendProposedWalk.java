package com.example.walkwalkrevolution;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.walkwalkrevolution.custom_data_classes.DateTimeFormatter;
import com.example.walkwalkrevolution.custom_data_classes.ProposedWalk;
import com.example.walkwalkrevolution.custom_data_classes.ProposedWalkJsonConverter;
import com.example.walkwalkrevolution.forms.SetDate;
import com.example.walkwalkrevolution.proposed_walk_observer_pattern.ProposedWalkFetcherService;
import com.example.walkwalkrevolution.proposed_walk_observer_pattern.ProposedWalkObservable;
import com.example.walkwalkrevolution.proposed_walk_observer_pattern.ProposedWalkObserver;

/**
 * Activity for creating and sending a Proposed walk to a team.
 */
public class SendProposedWalk extends AppCompatActivity implements ProposedWalkObserver {

    // Constant for logging
    private static final String TAG = "SendProposedWalk";

    // Activity objects
    private Button sendButton, cancelButton;
    private TextView dateDisplayTextView;
    private TimePicker timePicker;

    // Route name, saved from RoutesForm
    private String routeName, startingPoint;

    // Saves a proposed walk from the cloud if there is one
    private ProposedWalk cloudProposedWalk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_proposed_walk);

        setUpUI();

        // Get route name element from route form
        Intent fromIntent = this.getIntent();
        if (fromIntent != null) {
            routeName = fromIntent.getExtras().getString("routeName");
            startingPoint = fromIntent.getExtras().getString("startingPoint");
            Log.d(TAG, "Received a Route from the Routes Form with the name: " + routeName);
        }

        if (!HomePage.MOCK_TESTING) {
            // Start the fetcher intent service
            Intent intent = new Intent(SendProposedWalk.this, ProposedWalkFetcherService.class);
            startService(intent);

            ProposedWalkObservable.register(this);
        }

    }


    /**
     * Set up UI elements.
     */
    private void setUpUI() {
        // Handle onClickListeners for Save and Cancel Buttons
        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendProposedWalk();
            }
        });
        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancel();
            }
        });

        // Set up date and time fields
        dateDisplayTextView = (TextView) findViewById(R.id.dateDisplayTextView);
        dateDisplayTextView.setText(DateTimeFormatter.getCurrentDate());
        timePicker = (TimePicker) findViewById(R.id.timePicker);
    }

    /**
     * Upon quitting this intent, stop the fetcher intent service. Remove this Activity as
     * an observer.
     */
    @Override
    protected void onStop(){
        super.onStop();

        if (!HomePage.MOCK_TESTING) {
            // Stop the fetcher intent service
            Intent intent = new Intent(SendProposedWalk.this, ProposedWalkFetcherService.class);
            stopService(intent);

            ProposedWalkObservable.removeObserver(this);
        }

    }


    // SAVING AND CANCELING  --------------------------------------------------

    /**
     * Save button behavior. Send the proposed walk to the cloud.
     */
    private void sendProposedWalk() {
        if (HomePage.MOCK_TESTING) {
            Log.d(TAG, "MOCKING!");
            Intent intent = new Intent(this, TeammatesPage.class);
            startActivity(intent);
            finish();
            return;
        }

        // Check if a proposed walk was already made, this happens if someone else made a proposed
        // walk while we were on this page
        if (cloudProposedWalk != null) {
            Toast.makeText(this, "A Proposed Walk already exists",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, TeammatesPage.class);
            startActivity(intent);
        }

        // Get date and time fields
        String date = dateDisplayTextView.getText().toString();
        String time = DateTimeFormatter.formatTime(timePicker.getHour(), timePicker.getMinute());

        Log.d(TAG, "Creating a ProposedWalk with date: " + date + "and time: " + time);

        // Create the proposed walk use entered data and what was on the Route Form
        ProposedWalk proposedWalk = new ProposedWalk(routeName, date, time, CloudDatabase.currentUserMember);
        if (startingPoint != null) {
            proposedWalk.setLocation(startingPoint);
        }

        // Store the team proposedwalk
        ProposedWalkObservable.storeProposedWalk(proposedWalk);

        Log.d(TAG, "Proposed walk sent..");
        Toast.makeText(this, "Proposed walk sent!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, TeammatesPage.class);
        startActivity(intent);
        finish();
    }

    /**
     * Cancel button behavior. Go back to the Team page without saving data.
     */
    private void cancel() {
        Log.d(TAG, "No proposed walk sent.");
        finish();
    }

    // HELPER METHODS -------------------------------------------------------------

    private final int REQUEST_DATE = 1;

    /**
     * Opens the Set Date Activity so the user can change the Proposed Walk's date.
     */
    public void openSetDate(View view) {
        Intent intent = new Intent(this, SetDate.class);
        intent.putExtra("date", dateDisplayTextView.getText().toString());
        Log.d(TAG, "Opening SetDate activity...");
        startActivityForResult(intent, REQUEST_DATE);
    }

    /**
     * Handles coming back from the SetDate Activity (Saving the new date chosen on the DatePicker).
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_DATE:
                if(resultCode == RESULT_OK) {
                    String dateStr = data.getStringExtra("newDate");
                    dateDisplayTextView.setText(dateStr);
                    Log.d(TAG, "Saved data from the SetDate activity");
                }
                break;
        }
    }


    /**
     * ProposedWalkObserver update method. Saves a proposed walk from the cloud into
     * the instance var cloudProposedWalk.
     */
    @Override
    public void update(ProposedWalkObserver o, Object arg) {
        Log.d(TAG, "Called Send Proposed Walk observer update()");
        this.cloudProposedWalk = (ProposedWalk) arg;
    }

}
