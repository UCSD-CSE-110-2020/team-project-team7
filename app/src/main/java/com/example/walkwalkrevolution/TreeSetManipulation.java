package com.example.walkwalkrevolution;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

//import br.com.kots.mob.complex.preferences.ComplexPreferences;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class TreeSetManipulation {

    public final static String SHARED_PREFS_TREE_SET = "treeSet";


/*
    public static void initializeTreeSet(SharedPreferences sharedPreferences, TreeSetComparator comparator){
        TreeSet<Route> treeSet = new TreeSet<Route>(comparator);
        treeSet.add(new Route("Ark Walk", "SP", 2000, 10));
        //treeSet.add(new Route("Ark Wal23k", "SP", 2000, 10));
        saveTreeSet(sharedPreferences, treeSet);
        Log.d("updateTreeSet", "123");
    }

    public static TreeSet<Route> loadTreeSet(SharedPreferences sharedPreferences){
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SHARED_PREFS_TREE_SET, "");
        Type type = new TypeToken<TreeSet<Route>>() {}.getType();
        Log.d("create", json);
        TreeSet<Route> treeSet = gson.fromJson(json, type);
        return new TreeSet<Route>();
    }

    private static void saveTreeSet(SharedPreferences sharedPreferences, TreeSet<Route> treeSet){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(treeSet);
        Log.d("json", json);
        editor.putString(SHARED_PREFS_TREE_SET, json);
        editor.apply();
    }

    public static boolean updateTreeSet(SharedPreferences sharedPreferences, Route route){
        TreeSet<Route> treeSet = loadTreeSet(sharedPreferences);
        Log.d("updateTreeSet", "hello");
        boolean wasAdded = treeSet.add(route);
        if(!wasAdded){
            return false;
        }
        saveTreeSet(sharedPreferences, treeSet);
        return true;
    }

 */

    public static void initalizeTreeSet(SharedPreferences sharedPreferences){
        List<Route> list = new ArrayList<>();
        saveTreeSet(sharedPreferences, list);
        Log.d("updateTreeSet", "123");
    }

    private static void saveTreeSet(SharedPreferences sharedPreferences, List<Route> list){
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

    public static boolean updateTreeSet(SharedPreferences sharedPreferences, TreeSetComparator comparator, Route route){
        TreeSet<Route> treeSet = loadTreeSet(sharedPreferences, comparator);
        Log.d("updateTreeSet", "hello");
        boolean wasAdded = treeSet.add(route);
        if(!wasAdded){
            return false;
        }
        saveTreeSet(sharedPreferences, new ArrayList<Route>(treeSet));
        return true;
    }


}