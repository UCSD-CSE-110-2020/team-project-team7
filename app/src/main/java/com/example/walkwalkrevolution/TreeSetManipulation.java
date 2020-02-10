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
    public static Route routeBeingWalked = null;

    public static void initalizeTreeSet(SharedPreferences sharedPreferences){
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
        editor.commit();
    }


    public static TreeSet<Route> loadTreeSet(SharedPreferences sharedPreferences, TreeSetComparator comparator ){
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SHARED_PREFS_TREE_SET, "");
        Type type = new TypeToken<List<Route>>() {}.getType();
        Log.d("create", json);
        List<Route> list = gson.fromJson(json, type);
        TreeSet<Route> treeSet = new TreeSet<Route>(comparator);
        treeSet.addAll(list);
        return treeSet;
    }

    public static void updateTreeSet(SharedPreferences sharedPreferences, TreeSetComparator comparator, Route prevRoute, Route updatedRoute){
        TreeSet<Route> treeSet = loadTreeSet(sharedPreferences, comparator);
        Log.d("updateTreeSet", "hello");
        treeSet.remove(prevRoute);
        treeSet.add(updatedRoute);
        saveTreeSet(sharedPreferences, new ArrayList<Route>(treeSet));
        setRouteBeingWalked(null);
    }

    public static boolean addTreeSet(SharedPreferences sharedPreferences, TreeSetComparator comparator, Route route){
        TreeSet<Route> treeSet = loadTreeSet(sharedPreferences, comparator);
        Log.d("addTreeSet", "hello");
        boolean wasAdded = treeSet.add(route);
        if(!wasAdded){
            return false;
        }
        saveTreeSet(sharedPreferences, new ArrayList<Route>(treeSet));
        return true;
    }

    public static void setRouteBeingWalked(Route routeBeingWalked) {
        TreeSetManipulation.routeBeingWalked = routeBeingWalked;
    }

    public static Route getRouteBeingWalked() {
        return routeBeingWalked;
    }
}