package com.example.walkwalkrevolution;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.walkwalkrevolution.custom_data_classes.DateTimeFormatter;
import com.example.walkwalkrevolution.custom_data_classes.ProposedWalk;
import com.example.walkwalkrevolution.custom_data_classes.ProposedWalkJsonConverter;
import com.example.walkwalkrevolution.forms.SetDate;

/**
 * Activity for creating and sending a Proposed walk to a team.
 */
public class SendProposedWalk extends AppCompatActivity {

    // Constant for logging
    private static final String TAG = "SendProposedWalk";

    // Activity objects
    private Button sendButton, cancelButton;
    private TextView dateDisplayTextView;
    private TimePicker timePicker;

    // Route name, saved from RoutesForm
    private String routeName, startingPoint;
    private TeamMember creator;


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


    // SAVING AND CANCELING  --------------------------------------------------

    /**
     * Save button behavior. Send the proposed walk to the cloud.
     */
    private void sendProposedWalk() {
        // Get date and time fields
        String date = dateDisplayTextView.getText().toString();
        String time = DateTimeFormatter.formatTime(timePicker.getHour(), timePicker.getMinute());

        Log.d(TAG, "Creating a ProposedWalk with date: " + date + "and time: " + time);

        // Create the proposed walk use entered data and what was on the Route Form
        ProposedWalk proposedWalk = new ProposedWalk(routeName, date, time, creator);
        if (startingPoint != null) {
            proposedWalk.setLocation(startingPoint);
        }

        // TODO, UPLOAD PROPOSED WALK TO CLOUD (USERID HARDCODED FOR NOW UNTIL GOOGLE AUTH WORKS)
//        MockFirestoreDatabase.storeProposedWalk(proposedWalk, TeamMemberFactory.get("CalvinID"));

        Log.d(TAG, "Proposed walk sent..");
        finish(); // TODO, WHICH ACTIVITY DO WE GO TO AFTER SENDING??
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

}
