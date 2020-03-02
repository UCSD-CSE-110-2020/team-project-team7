package com.example.walkwalkrevolution;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Responsible for memory storage of last intentional walk (steps, distance, time) via SharedPreferences.
 */

public class LastIntentionalWalk {

    public final static String SHARED_PREFS_INTENTIONAL_WALK = "lastWalk";
    private static final String TAG = "LastIntentionalWalk";

    /**
     * Initializes the List that will be saved into SharedPreferences. Default is 0 for all.
     *
     * @param sharedPreferences
     * @return
     */
    public static List<String> initializeLastWalk(SharedPreferences sharedPreferences){
        List<String> list = new ArrayList<>();
        list.add("0");//steps
        list.add("0.0");//distance
        list.add("0:00");//time
        saveLastWalk(sharedPreferences, list);
        Log.d(TAG, "Last Walk Initialized - Default Values");
        return list;
    }

    /**
     * Saves the given list into SharedPreferences entry for last walk. Assumes the list to have three entries.
     * @param sharedPreferences
     * @param list
     */
    public static void saveLastWalk(SharedPreferences sharedPreferences, List<String> list){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        //Log.d(TAG, "Last Walk Saved: Steps " + list.get(0) + " Distance " + list.get(1) + " Time " + list.get(2));
        Log.d(TAG, "Last Walk Saved");
        editor.putString(SHARED_PREFS_INTENTIONAL_WALK, json);
        editor.apply();
    }

    /**
     * Loads List for last walk from SharedPreferences, returning the list.
     * @param sharedPreferences
     * @return
     */

    public static List<String> loadLastWalk(SharedPreferences sharedPreferences){
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SHARED_PREFS_INTENTIONAL_WALK, "");
        Type type = new TypeToken<List<String>>() {}.getType();
        Log.d(TAG, "Last Walk Loaded");
        return gson.fromJson(json, type);
    }
}
