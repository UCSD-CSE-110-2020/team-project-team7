/**
 * Online Code:
 * Locally Saving ArrayLists: https://stackoverflow.com/questions/14981233/android-arraylist-of-custom-objects-save-to-sharedpreferences-serializable
 *
 */
package com.example.walkwalkrevolution;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class TreeSetManipulation {

    public final static String SHARED_PREFS_TREE_SET = "treeSet";
    private static TreeSetComparator comparator = new TreeSetComparator();
    private static Route selectedRoute = null;

    public static void initializeTreeSet(SharedPreferences sharedPreferences){
        List<Route> list = new ArrayList<>();
        saveTreeSet(sharedPreferences, list);
        Log.d("updateTreeSet", "123");
    }

    public static void saveTreeSet(SharedPreferences sharedPreferences, List<Route> list){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        Log.d("json", json);
        editor.putString(SHARED_PREFS_TREE_SET, json);
        editor.apply();
    }

    public static List<Route> loadTreeSet(SharedPreferences sharedPreferences){
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SHARED_PREFS_TREE_SET, "");
        Type type = new TypeToken<List<Route>>() {}.getType();
        Log.d("create", json);
        return gson.fromJson(json, type);
    }

    public static boolean updateRouteInTreeSet(SharedPreferences sharedPreferences, Route updatedRoute){
        TreeSet<Route> treeSet = new TreeSet<Route>(comparator);
        treeSet.addAll(loadTreeSet(sharedPreferences));
        Log.d("updateTreeSet", "hello");
        //route's modified name updated to something already in the list (other than original name)
        if(!selectedRoute.compareRoute(updatedRoute) && treeSet.contains(updatedRoute)){
            return false;
        }
        treeSet.remove(selectedRoute);
        treeSet.add(updatedRoute);
        saveTreeSet(sharedPreferences, new ArrayList<Route>(treeSet));
        setSelectedRoute(null);
        return true;
    }

    public static boolean addRouteInTreeSet(SharedPreferences sharedPreferences, Route route){
        TreeSet<Route> treeSet = new TreeSet<Route>(comparator);
        treeSet.addAll(loadTreeSet(sharedPreferences));
        Log.d("addTreeSet", "hello");
        boolean wasAdded = treeSet.add(route);
        if(!wasAdded){
            return false;
        }
        saveTreeSet(sharedPreferences, new ArrayList<Route>(treeSet));
        return true;
    }

    public static void setSelectedRoute(Route routeBeingWalked) {
        TreeSetManipulation.selectedRoute = routeBeingWalked;
    }

    public static Route getSelectedRoute() {
        return selectedRoute;
    }
}