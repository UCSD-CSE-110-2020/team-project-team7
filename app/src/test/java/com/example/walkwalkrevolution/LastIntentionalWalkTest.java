package com.example.walkwalkrevolution;

import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import androidx.test.core.app.ActivityScenario;
import androidx.test.runner.AndroidJUnit4;

import static android.content.Context.MODE_PRIVATE;
import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
@Config(sdk = 27)

/**
 * Testing class for LastIntentionalWalk- load, save, and initialize.
 */
public class LastIntentionalWalkTest {
    private List<String> list;

    @Before
    public void setUp(){
       list = new ArrayList<>();
        list.add("20");//steps
        list.add("40.0");//distance
        list.add("19:50");//time
    }

    /**
     * Checking if initialized list is empty, and default values match up when loaded.
     */
    @Test
    public void initializationTestForDefaultList(){
        try (ActivityScenario<HeightForm> scenario = ActivityScenario.launch(HeightForm.class)) {
            scenario.onActivity(activity -> {
                SharedPreferences sharedPreferences = activity.getSharedPreferences(LastIntentionalWalk.SHARED_PREFS_INTENTIONAL_WALK, MODE_PRIVATE);

                list = LastIntentionalWalk.initializeLastWalk(sharedPreferences);
                assert (list.size() == 3);
                assert(list.get(0).equals("0"));
                assert(list.get(1).equals("0.0"));
                assert(list.get(2).equals("0:00"));
            });
        }
    }

    /**
     * Saving and loading an list with pre-set values. Values should be the same after loading.
     */
    @Test
    public void TestForLoadingAndSavingPositiveElements(){
        try (ActivityScenario<HeightForm> scenario = ActivityScenario.launch(HeightForm.class)) {
            scenario.onActivity(activity -> {
                SharedPreferences sharedPreferences = activity.getSharedPreferences(LastIntentionalWalk.SHARED_PREFS_INTENTIONAL_WALK, MODE_PRIVATE);

                LastIntentionalWalk.saveLastWalk(sharedPreferences, list);

                list = LastIntentionalWalk.loadLastWalk(sharedPreferences);
                assert (list.size() == 3);
                assert(list.get(0).equals("20"));
                assert(list.get(1).equals("40.0"));
                assert(list.get(2).equals("19:50"));
            });
        }
    }

    /**
     * Saving and loading an empty list. Loading should also yield an empty list.
     */
    @Test
    public void TestForLoadingAndSavingForEmptyList(){
        try (ActivityScenario<HeightForm> scenario = ActivityScenario.launch(HeightForm.class)) {
            scenario.onActivity(activity -> {
                SharedPreferences sharedPreferences = activity.getSharedPreferences(LastIntentionalWalk.SHARED_PREFS_INTENTIONAL_WALK, MODE_PRIVATE);

                LastIntentionalWalk.saveLastWalk(sharedPreferences, new ArrayList<>());

                list = LastIntentionalWalk.loadLastWalk(sharedPreferences);
                assert (list.isEmpty());
            });
        }
    }

}
