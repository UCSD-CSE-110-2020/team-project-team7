package com.example.walkwalkrevolution;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.TreeSet;

public class TreeSetManipulation {

    private final static String SHARED_PREFS_TREE_SET = "treeSet";

    public static void initializeTreeSet(SharedPreferences sharedPreferences, TreeSetComparator comparator){
        TreeSet<Route> treeSet = new TreeSet<>(comparator);
        saveTreeSet(sharedPreferences, treeSet);
    }

    public static TreeSet<Route> loadTreeSet(SharedPreferences sharedPreferences){
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SHARED_PREFS_TREE_SET, null);
        Type type = new TypeToken<TreeSet<Route>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private static void saveTreeSet(SharedPreferences sharedPreferences, TreeSet<Route> treeSet){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(treeSet);
        editor.putString(SHARED_PREFS_TREE_SET, json);
        editor.apply();
    }

    public static boolean updateTreeSet(SharedPreferences sharedPreferences, Route route){
        TreeSet<Route> treeSet = loadTreeSet(sharedPreferences);
        boolean wasAdded = treeSet.add(route);
        if(!wasAdded){
            return false;
        }
        saveTreeSet(sharedPreferences, treeSet);
        return true;
    }
}