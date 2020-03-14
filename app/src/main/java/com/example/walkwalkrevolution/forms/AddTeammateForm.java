package com.example.walkwalkrevolution.forms;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.walkwalkrevolution.CloudDatabase;
import com.example.walkwalkrevolution.HomePage;
import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.TeamMember;
import com.example.walkwalkrevolution.TeamMemberFactory;
import com.example.walkwalkrevolution.custom_data_classes.Route;


/**
 * Activity for allowing user to add new teammates.
 */
public class AddTeammateForm extends AppCompatActivity {

    // Constant for logging
    private static final String TAG = "AddTeammateForm";

    // Activity objects
    private Button saveButton;
    private Button cancelButton;
    private EditText nameEditText, emailEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_teammate);

        // Handle onClickListeners for Save and Cancel Buttons
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveTeammate();
            }
        });
        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancel();
            }
        });

        // Set up edit texts
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
    }


    // ON CLICK METHODS FOR SAVING AND CANCELING  --------------------------------------------------

    /**
     * Save button behavior.
     */
    private void saveTeammate() {

        // Make sure none of the fields are empty, else raise an error
        if (checkRequiredFieldsError()) {
            return;
        }

        if(HomePage.MOCK_TESTING){
            TeamMember mockedMember = new TeamMember("Titan Ngo", "ttngo@ucsd.edu", false);
            Route mockedRoute = Route.RouteBuilder.newInstance()
                    .setName("Route 1")
                    .setStartingPoint("start1")
                    .setSteps(100)
                    .setDistance(18.9)
                    .setDate("3/4/20")
                    .setDuration(2, 21)
                    .setOptionalFeatures(null)
                    .setOptionalFeaturesStr(null)
                    .setNotes("notes")
                    .setUserHasWalkedRoute(false)
                    .setCreator(mockedMember)
                    .buildRoute();
            TeamMemberFactory.put(emailEditText.getText().toString(),mockedMember );
            TeamMemberFactory.addRoute(new Pair<>(emailEditText.getText().toString(),mockedRoute));

        }else{
            // she will be rendering Team page based on whats in the cloud
            CloudDatabase.inviteToTeam(emailEditText.getText().toString());
        }

        Log.d(TAG, "Teammate saved.");
        finish();
    }

    /**
     * Cancel button behavior. Go back to the Team page without saving data.
     */
    private void cancel() {
        Log.d(TAG, "No teammate saved");
        finish();
    }


    // HELPER METHODS -------------------------------------------------------------

    /**
     * Helper method for saving. Checks if any of the edit texts are empty and if email has an '@'.
     */
    private boolean checkRequiredFieldsError() {
        boolean error = false;

        // Get fields in EditTexts
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();

        // If edit text empty, raise an error
        if (name.equals(""))  {
            // Display error for required field
            nameEditText.setError("Required field");
            error = true;
        }

        if (email.equals("")) {
            // Display error for required field
            emailEditText.setError("Required field");
            error = true;
        } else if (!email.contains("@")) {
            // Display error for required field
            emailEditText.setError("Gmail address must have '@'");
            error = true;
        }

        return error;
    }

}
