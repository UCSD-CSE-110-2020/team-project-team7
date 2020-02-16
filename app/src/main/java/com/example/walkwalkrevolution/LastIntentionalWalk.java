package com.example.walkwalkrevolution;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.walkwalkrevolution.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LastIntentionalWalk {

    public final static String SHARED_PREFS_INTENTIONAL_WALK = "lastWalk";
    private static final String TAG = "LastIntentionalWalk";

    public static List<String> initializeLastWalk(SharedPreferences sharedPreferences){
        List<String> list = new ArrayList<>();
        list.add("0");
        list.add("0.0");
        list.add("0:00");
        saveLastWalk(sharedPreferences, list);
        Log.d(TAG, "Last Walk Initialized");
        return list;
    }

    public static void saveLastWalk(SharedPreferences sharedPreferences, List<String> list){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        Log.d(TAG, "Last Walk Saved");
        editor.putString(SHARED_PREFS_INTENTIONAL_WALK, json);
        editor.apply();
    }

    public static List<String> loadLastWalk(SharedPreferences sharedPreferences){
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SHARED_PREFS_INTENTIONAL_WALK, "");
        Type type = new TypeToken<List<String>>() {}.getType();
        Log.d(TAG, "Last Walk Loaded");
        return gson.fromJson(json, type);
    }
}
