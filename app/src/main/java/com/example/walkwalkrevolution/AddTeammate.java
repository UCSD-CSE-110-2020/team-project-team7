package com.example.walkwalkrevolution;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity for allowing user to add new teammates.
 */
public class AddTeammate extends AppCompatActivity {

    // Constant for logging
    private static final String TAG = "AddTeammate";

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

        // TODO, ERROR CHECKING, CHECK IF NAME/EMAIL IS VALID

        // TODO, Amrit says to upload teammate to the cloud, not pass as an intent extra,
        // she will be rendering Team page based on whats in the cloud

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
