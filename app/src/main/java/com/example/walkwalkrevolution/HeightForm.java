package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Handles the Activity for inputting the user's height. Evoked only once from the Home Page
 * upon first time starting up the app.
 */
public class HeightForm extends AppCompatActivity {

    // Constant for logging
    private static final String TAG = "HeightForm";
    // Constant for tracking intents
    public static final String HEIGHT_FORM_INTENT = "From_Height_Form";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height_form);

        initializationSetUp();

        // Button for saving the height
        Button saveBtn = (Button) findViewById(R.id.height_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("height", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                EditText heightInfoFeet = findViewById(R.id.height_input_ft);
                EditText heightInfoInches = findViewById(R.id.height_input_in);
                editor.putInt("height_ft", Integer.parseInt(heightInfoFeet.getText().toString()));
                editor.putInt("height_in", Integer.parseInt(heightInfoInches.getText().toString()));
                editor.apply();

                Toast.makeText(HeightForm.this, "Saved Height:" +
                        heightInfoFeet.getText().toString() + "'" +
                        heightInfoInches.getText().toString() + "''", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    /**
     * Method called only once in the entire running of the app (upon first startup).
     * Used to set up data structure for Routes and Last Intentional Walk as well as time/mock time.
     */
    private void initializationSetUp(){
        TreeSetManipulation.initializeTreeSet(getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE));
        LastIntentionalWalk.initializeLastWalk(getSharedPreferences(LastIntentionalWalk.SHARED_PREFS_INTENTIONAL_WALK, MODE_PRIVATE));

        // initialize time data fields
        TimeData.initTimeData(getSharedPreferences(TimeData.TIME_DATA, MODE_PRIVATE));

        Log.d(TAG, "Finished initalizationSetUp()");
    }

    /**
     * prevents the user from pressing the back button to go back to the home screen
     */
    @Override
    public void onBackPressed(){
        Toast.makeText(HeightForm.this, "Please press the save button to go back", Toast.LENGTH_SHORT).show();
    }

}
