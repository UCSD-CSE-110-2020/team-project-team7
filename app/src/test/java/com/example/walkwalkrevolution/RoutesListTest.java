package com.example.walkwalkrevolution;

import android.content.ComponentName;
import android.content.Intent;
import android.widget.Button;

import androidx.test.core.app.ActivityScenario;
import androidx.test.runner.AndroidJUnit4;

import com.example.walkwalkrevolution.forms.HeightForm;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

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
                assertThat(intent.getComponent()).isEqualTo(new ComponentName(activity, FireBaseMessagingService.RoutesForm.class));

            });
        }
    }


}