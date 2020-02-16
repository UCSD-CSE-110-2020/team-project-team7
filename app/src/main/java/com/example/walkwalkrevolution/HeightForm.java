package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HeightForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height_form);

        initializationSetUp();

        SharedPreferences sharedPreferences = getSharedPreferences("height", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("height", "175");
        editor.apply();

        Button saveBtn = (Button) findViewById(R.id.height_save_btn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("height", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                EditText heightInfo = findViewById(R.id.height_input);
                editor.putString("height", heightInfo.getText().toString());
                editor.apply();

                Toast.makeText(HeightForm.this, "Saved Height:" + heightInfo.getText().toString(), Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }

    private void initializationSetUp(){
        TreeSetManipulation.initializeTreeSet(getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE));
        LastIntentionalWalk.initializeLastWalk(getSharedPreferences(LastIntentionalWalk.SHARED_PREFS_INTENTIONAL_WALK, MODE_PRIVATE));
    }

    /**
     * prevents the user from pressing the back button to go back to the home screen
     */
    @Override
    public void onBackPressed(){
        Toast.makeText(HeightForm.this, "Please press the save button to go back", Toast.LENGTH_SHORT).show();
    }

}
