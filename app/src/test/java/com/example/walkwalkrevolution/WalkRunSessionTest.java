package com.example.walkwalkrevolution;

import android.content.ComponentName;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowToast;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.*;


//@RunWith(AndroidJUnit4.class)
//@Config(sdk = 27)
public class WalkRunSessionTest {
    private ShadowActivity shadowActivity;

    @Test
    public void dummyTest(){}


    // checks to see if the stop button moves to the routes form page
    /*
    @Test
    public void testStopButton(){
        try(ActivityScenario<WalkRunSession> scenario = ActivityScenario.launch(WalkRunSession.class)) {
            scenario.onActivity(activity -> {
                Button stopBtn = activity.findViewById(R.id.stop_btn);
                stopBtn.performClick();
                assertThat(stopBtn.isPressed());
                Intent intent = Shadows.shadowOf(activity).getNextStartedActivity();
                assertThat(intent.getComponent()).isEqualTo(new ComponentName(activity, RoutesForm.class));

            });
        }
    }
    */


    /*
    // checks to see if the stop button moves to the routesform page
    @Test
    public void testBackPress(){
        try(ActivityScenario<WalkRunSession> scenario = ActivityScenario.launch(WalkRunSession.class)) {
            scenario.onActivity(activity -> {
                activity.onBackPressed();
                String latestToast = ShadowToast.getTextOfLatestToast();
                String example = "Please press the stop button to go stop";
                assertThat(latestToast.equals(example));

            });
        }
    }
    */

}