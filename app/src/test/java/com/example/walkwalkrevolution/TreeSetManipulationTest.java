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

import com.example.walkwalkrevolution.custom_data_classes.Route;
import com.example.walkwalkrevolution.forms.HeightForm;

import static android.content.Context.MODE_PRIVATE;
import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
@Config(sdk = 27)

/**
 * Tests the TreeSetManipulation Class - initializing, loading, saving, updating, and adding
 */
public class TreeSetManipulationTest {

    Route r1;
    Route r2;
    Route r3;

    @Before
    public void setUp(){
        String sp = "SP";
        int steps = 10;
        int distance = 20;
        r1 = new Route("Amrit", sp, steps, distance);
        r2 = new Route("Ellie", sp, steps, distance);
        r3 = new Route("Amrit", sp, steps, distance);
    }

    /**
     * Checking if initialized list is empty.
     */
    @Test
    public void initializationTestForEmptyList(){
        try (ActivityScenario<HeightForm> scenario = ActivityScenario.launch(HeightForm.class)) {
            scenario.onActivity(activity -> {
                SharedPreferences sharedPreferences = activity.getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE);

                TreeSetManipulation.initializeTreeSet(sharedPreferences);
                List<Route> list = TreeSetManipulation.loadTreeSet(sharedPreferences);
                assertThat(list.isEmpty());
            });
        }
    }

    /**
     * Preloaded list with 2 hardcoded elements. Checking is saving, and then reloading list gives back same elements.
     */
    @Test
    public void testForLoadingAndSavingListWithPositiveElements(){
        try (ActivityScenario<HeightForm> scenario = ActivityScenario.launch(HeightForm.class)) {
            scenario.onActivity(activity -> {
                SharedPreferences sharedPreferences = activity.getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE);

                List<Route> list = new ArrayList<>();
                list.add(r1);
                list.add(r2);

                TreeSetManipulation.saveTreeSet(sharedPreferences, list);
                list = TreeSetManipulation.loadTreeSet(sharedPreferences);

                assert (list.size() == 2);
                assert (list.get(0).name.equals("Amrit"));
                assert (list.get(1).name.equals("Ellie"));
            });
        }
    }

    /**
     * Testing updateRouteInTreeSet where the updated element has a route name that already exists in list. Should not allow update.
     */
    @Test
    public void testForUpdatingElementToPreExistingElement(){
        try (ActivityScenario<HeightForm> scenario = ActivityScenario.launch(HeightForm.class)) {
            scenario.onActivity(activity -> {
                SharedPreferences sharedPreferences = activity.getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE);

                List<Route> list = new ArrayList<>();
                list.add(r1);
                list.add(r2);

                TreeSetManipulation.saveTreeSet(sharedPreferences, list);

                TreeSetManipulation.setSelectedRoute(r2);

                boolean result = TreeSetManipulation.updateRouteInTreeSet(sharedPreferences, r3);

                assert (result == false);

                list = TreeSetManipulation.loadTreeSet(sharedPreferences);
                assert (list.size() == 2);
                assert (list.get(0).name.equals("Amrit"));
                assert (list.get(1).name.equals("Ellie"));
            });
        }
    }


    /**
     * Testing updateRouteInTreeSet where the updated element has a route name that does not already exist in list. Should allow update.
     */
    @Test
    public void testForUpdatingElementToNonPreExistingElement(){
        try (ActivityScenario<HeightForm> scenario = ActivityScenario.launch(HeightForm.class)) {
            scenario.onActivity(activity -> {
                SharedPreferences sharedPreferences = activity.getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE);

                List<Route> list = new ArrayList<>();
                list.add(r1);
                list.add(r2);

                TreeSetManipulation.saveTreeSet(sharedPreferences, list);

                TreeSetManipulation.setSelectedRoute(r1);
                r3.setName("Kelly");

                boolean result = TreeSetManipulation.updateRouteInTreeSet(sharedPreferences, r3);

                assert (result == true);

                list = TreeSetManipulation.loadTreeSet(sharedPreferences);
                assertThat(list.size() == 2);

                assert (list.get(0).name.equals("Ellie"));
                assert (list.get(1).name.equals("Kelly"));
            });
        }
    }

    /**
     * Testing updateRouteInTreeSet where the updated element has no changes made to it. Should allow update.
     */
    @Test
    public void testForUpdatingElementWithNoChange(){
        try (ActivityScenario<HeightForm> scenario = ActivityScenario.launch(HeightForm.class)) {
            scenario.onActivity(activity -> {
                SharedPreferences sharedPreferences = activity.getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE);

                List<Route> list = new ArrayList<>();
                list.add(r1);
                list.add(r2);

                TreeSetManipulation.saveTreeSet(sharedPreferences, list);

                TreeSetManipulation.setSelectedRoute(r1);

                boolean result = TreeSetManipulation.updateRouteInTreeSet(sharedPreferences, r1);

                assert (result == true);

                list = TreeSetManipulation.loadTreeSet(sharedPreferences);
                assert (list.size() == 2);
                assert (list.get(0).name.equals("Amrit"));
                assert (list.get(1).name.equals("Ellie"));
            });
        }
    }

    /**
     * Testing addRouteInTreeSet where added element has a route name that already exists in list. Should not allow add.
     */
    @Test
    public void testForAddingElementDuplicate() {
        try (ActivityScenario<HeightForm> scenario = ActivityScenario.launch(HeightForm.class)) {
            scenario.onActivity(activity -> {
                SharedPreferences sharedPreferences = activity.getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE);

                List<Route> list = new ArrayList<>();
                list.add(r1);
                list.add(r2);

                TreeSetManipulation.saveTreeSet(sharedPreferences, list);

                boolean result = TreeSetManipulation.addRouteInTreeSet(sharedPreferences, r1);

                assert (result == false);

                list = TreeSetManipulation.loadTreeSet(sharedPreferences);
                assert (list.size() == 2);
                assert (list.get(0).name.equals("Amrit"));
                assert (list.get(1).name.equals("Ellie"));
            });
        }
    }


    /**
     * Testing addRouteInTreeSet where added element has a route name that does not already exist in list. Should allow add, and
     * element should be added to the end of the list.
     */
    @Test
    public void testForAddingElementNotDuplicateOrderUnChanged() {
        try (ActivityScenario<HeightForm> scenario = ActivityScenario.launch(HeightForm.class)) {
            scenario.onActivity(activity -> {
                SharedPreferences sharedPreferences = activity.getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE);

                List<Route> list = new ArrayList<>();
                list.add(r1);
                list.add(r2);

                TreeSetManipulation.saveTreeSet(sharedPreferences, list);
                r3.setName("Kelly");

                boolean result = TreeSetManipulation.addRouteInTreeSet(sharedPreferences, r3);

                assert (result == true);

                list = TreeSetManipulation.loadTreeSet(sharedPreferences);
                assert (list.size() == 3);
                assert (list.get(0).name.equals("Amrit"));
                assert (list.get(1).name.equals("Ellie"));
                assert (list.get(2).name.equals("Kelly"));
            });
        }
    }

    /**
     * Testing addRouteInTreeSet where added element has a route name that does not already exist in list. Should allow add, and
     * element should be added to the front of the list.
     */
    @Test
    public void testForAddingElementNotDuplicateOrderChanged() {
        try (ActivityScenario<HeightForm> scenario = ActivityScenario.launch(HeightForm.class)) {
            scenario.onActivity(activity -> {
                SharedPreferences sharedPreferences = activity.getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE);

                List<Route> list = new ArrayList<>();
                list.add(r1);
                list.add(r2);

                TreeSetManipulation.saveTreeSet(sharedPreferences, list);
                r3.setName("Amber");

                boolean result = TreeSetManipulation.addRouteInTreeSet(sharedPreferences, r3);

                assert (result == true);

                list = TreeSetManipulation.loadTreeSet(sharedPreferences);
                assert (list.size() == 3);
                assert (list.get(0).name.equals("Amber"));
                assert (list.get(1).name.equals("Amrit"));
                assert (list.get(2).name.equals("Ellie"));

            });
        }
    }
}
