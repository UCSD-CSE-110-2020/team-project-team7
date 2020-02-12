package com.example.walkwalkrevolution;

import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import static android.content.Context.MODE_PRIVATE;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@Config(sdk = 27)
public class HeightFormTest {


    // checks to see if default height is 175
    @Test
    public void testDefaultHeight(){
        try(ActivityScenario<HeightForm> scenario = ActivityScenario.launch(HeightForm.class)) {
            scenario.onActivity(activity -> {

                EditText height_btn = activity.findViewById(R.id.height_input);
                assertThat(height_btn.getText().toString().equals("175"));
            });
        }
    }

    // checks to see if the start height is set to 175 as height
    @Test
    public void testStartHeight() {
        try (ActivityScenario<HeightForm> scenario = ActivityScenario.launch(HeightForm.class)) {
            scenario.onActivity(activity -> {
                SharedPreferences sharedPreferences = activity.getSharedPreferences("height",MODE_PRIVATE);
                String startingHeight = sharedPreferences.getString("height","");
                assertThat(startingHeight.equals("175"));
            });
        }
    }

    // checks to see if backpress brings up the right toast method
    @Test
    public void testBackPress() {
        try (ActivityScenario<HeightForm> scenario = ActivityScenario.launch(HeightForm.class)) {
            scenario.onActivity(activity -> {

                activity.onBackPressed();
                String latestToast = ShadowToast.getTextOfLatestToast();
                String example = "Please press the save button to go back";
                assertThat(latestToast.equals(example));
            });
        }
    }

    // make sure the default save height is 175
    @Test
    public void testSaveButton() {
        try (ActivityScenario<HeightForm> scenario = ActivityScenario.launch(HeightForm.class)) {
            scenario.onActivity(activity -> {

                Button saveBtn = activity.findViewById(R.id.height_save_btn);
                saveBtn.performClick();
                String latestToast = ShadowToast.getTextOfLatestToast();
                String example = "Saved Height:175";
                assertThat(latestToast.equals(example));
            });
        }
    }

}