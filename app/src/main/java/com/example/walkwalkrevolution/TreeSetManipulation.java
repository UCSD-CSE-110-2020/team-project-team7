/**
 * Online Code:
 * Locally Saving ArrayLists: https://stackoverflow.com/questions/14981233/android-arraylist-of-custom-objects-save-to-sharedpreferences-serializable
 *
 */
package com.example.walkwalkrevolution;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.walkwalkrevolution.custom_data_classes.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Memory manages all route information of the app via calls to SharedPreferences.
 */
public class TreeSetManipulation {

    public final static String SHARED_PREFS_TREE_SET = "treeSet";
    //private static TreeSetComparator comparator = new TreeSetComparator();
    private static Route selectedRoute = null; //route from RoutesPage that is being in use

    private static final String TAG = "TreeSetManipulation";

    /**
     * Initializes TreeSet into SharedPreferences, putting in by default an empty list.
     * @param sharedPreferences
     */
    public static void initializeTreeSet(SharedPreferences sharedPreferences){
        List<Route> list = new ArrayList<>();
        saveTreeSet(sharedPreferences, list);
        Log.d(TAG, "TreeSet Initialized");
    }

    /**
     * Saves given list into SharedPreferences.
     * @param sharedPreferences
     * @param list
     */
    public static void saveTreeSet(SharedPreferences sharedPreferences, List<Route> list){
//        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        Log.d("json", json);
//        editor.putString(SHARED_PREFS_TREE_SET, json);
//        editor.apply();

        // ----------- TESTING ------------ //
        CloudDatabase.storeUserRoutes(json);
        CloudDatabase.currentUser.setRoutes(json);
        // ----------- TESTING ------------ //

        Log.d(TAG, "TreeSet Saved");
    }

    /**
     * Loads list from SharedPreferences and returns it.
     * @param sharedPreferences
     * @return
     */
    public static List<Route> loadTreeSet(SharedPreferences sharedPreferences){
        Log.d(TAG, "TreeSet Loaded");
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SHARED_PREFS_TREE_SET, "");

        // ----------- TESTING ------------ //
        //List<Route> userRoutes = MockFirestoreDatabase.getUserRoutes(UserDetailsFactory.get("mockUserOne@ucsd.edu"));
        List<Route> userRoutes = CloudDatabase.getUserRoutes();
        // ----------- TESTING ------------ //

        Type type = new TypeToken<List<Route>>() {}.getType();
        Log.d("create", json);
        //return gson.fromJson(json, type);
        return userRoutes;
    }

    /**
     * Updates an existing route entry in the list.
     * @param sharedPreferences
     * @param updatedRoute
     * @return False if route cannot be updated, True if route was updated
     */
    public static boolean updateRouteInTreeSet(SharedPreferences sharedPreferences, Route updatedRoute){
        Log.d(TAG, "Attempting to UPDATE treeset...");
        TreeSet<Route> treeSet = new TreeSet<Route>();
        treeSet.addAll(loadTreeSet(sharedPreferences));
        Log.d("updateTreeSet", "hello");
        //route's modified name already exists in the list (excluding route's original name)
        if(!selectedRoute.compareRoute(updatedRoute) && treeSet.contains(updatedRoute)){
            Log.d(TAG, "Update Unsuccessful");
            return false;
        }
        treeSet.remove(selectedRoute);
        treeSet.add(updatedRoute);
        saveTreeSet(sharedPreferences, new ArrayList<Route>(treeSet));
        setSelectedRoute(null);
        Log.d(TAG, "Update Successful");
        return true;
    }

    /**
     * Attempts to add given route to the list.
     * @param sharedPreferences
     * @param route
     * @return False if route cannot be added, True if route was added
     */
    public static boolean addRouteInTreeSet(SharedPreferences sharedPreferences, Route route){
        Log.d(TAG, "Attempting to ADD to treeset...");
        TreeSet<Route> treeSet = new TreeSet<Route>();
        treeSet.addAll(loadTreeSet(sharedPreferences));
        Log.d("addTreeSet", "hello");
        //false if route name already exists in TreeSet
        boolean wasAdded = treeSet.add(route);
        if(!wasAdded){
            Log.d(TAG, "Add Unsuccessful");
            return false;
        }
        Log.d(TAG, "Add Successful");
        saveTreeSet(sharedPreferences, new ArrayList<Route>(treeSet));
        return true;
    }

    /**
     * Returns the route currently being in use in the app.
     * @param routeBeingWalked
     */
    public static void setSelectedRoute(Route routeBeingWalked) {
        Log.d(TAG, "selectedRoute was modified");
        TreeSetManipulation.selectedRoute = routeBeingWalked;
    }

    /**
     * Sets the route currently being in use in the app.
     * @return
     */
    public static Route getSelectedRoute() {
        return selectedRoute;
    }
}