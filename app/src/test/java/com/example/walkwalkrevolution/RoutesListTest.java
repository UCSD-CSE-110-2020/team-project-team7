package com.example.walkwalkrevolution;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Button;

import androidx.test.core.app.ActivityScenario;
import androidx.test.runner.AndroidJUnit4;

import android.content.SharedPreferences;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@Config(sdk = 27)
public class RoutesListTest {

    @Before
    public void setUpAddButton(){
        ActivityScenario<HeightForm> scenario = ActivityScenario.launch(HeightForm.class);
    }


    // checks to see if the "+" button moves to the routes form page
    @Test
    public void testAddButton(){
        try(ActivityScenario<RoutesList> scenario = ActivityScenario.launch(RoutesList.class)) {
            scenario.onActivity(activity -> {
                Button addButton = activity.findViewById(R.id.addRouteButton);
                addButton.performClick();
                assertThat(addButton.isPressed());
                Intent intent = Shadows.shadowOf(activity).getNextStartedActivity();
                assertThat(intent.getComponent()).isEqualTo(new ComponentName(activity, RoutesForm.class));

            });
        }
    }

    @Test
    public void testHomeButton(){
        try(ActivityScenario<RoutesList> scenario = ActivityScenario.launch(RoutesList.class)) {
            scenario.onActivity(activity -> {
                Button homeButton = activity.findViewById(R.id.goToHomePage);
                homeButton.performClick();
                assertThat(homeButton.isPressed());
                Intent intent = Shadows.shadowOf(activity).getNextStartedActivity();
                assertThat(intent.getComponent()).isEqualTo(new ComponentName(activity, HomePage.class));
            });
        }
    }


}