package com.example.walkwalkrevolution.forms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.walkwalkrevolution.LastIntentionalWalk;
import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.TreeSetManipulation;
import com.example.walkwalkrevolution.custom_data_classes.TimeData;

/**
 * Displays the Height form at FIRST time app launch (once). Also initializes all SharedPreferencess entries.
 */
public class HeightForm extends AppCompatActivity {

    public static final String HEIGHT_FORM_INTENT = "From_Height_Form";
    private static final String TAG = "HeightForm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height_form);

        initializationSetUp();

        /*SharedPreferences sharedPreferences = getSharedPreferences("height", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("height", "175");
        editor.apply();*/

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
     * Initializes all entries in SharedPreferences at first time launch.
     */
    private void initializationSetUp(){
        //initialize TreeSet for all Routes created (RoutesPage)
        TreeSetManipulation.initializeTreeSet(getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE));
        //initialize entry for Last Intentional Walk (HomePage)
        LastIntentionalWalk.initializeLastWalk(getSharedPreferences(LastIntentionalWalk.SHARED_PREFS_INTENTIONAL_WALK, MODE_PRIVATE));

        // initialize time data fields
        TimeData.initTimeData(getSharedPreferences(TimeData.TIME_DATA, MODE_PRIVATE));
        Log.d(TAG, "Initialization Setup Finished ");
    }

    /**
     * prevents the user from pressing the back button to go back to the home screen
     */
    @Override
    public void onBackPressed(){
        Toast.makeText(HeightForm.this, "Please press the save button to go back", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Back Button Pressed");
    }

}
