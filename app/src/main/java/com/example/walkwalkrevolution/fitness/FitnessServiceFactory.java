package com.example.walkwalkrevolution.fitness;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import com.example.walkwalkrevolution.HomePage;
import com.example.walkwalkrevolution.StepCountActivity;

public class FitnessServiceFactory {

    private static final String google_fit = "GOOGLE_FIT";
    private static final String test_fs = "TEST_SERVICE";
    private static final String TAG = "[FitnessServiceFactory]";

    private static Map<String, FitnessService> blueprints = new HashMap<>();

    public static void put(String key, FitnessService fs) {
        blueprints.put(key, fs);
    }

    public static FitnessService getFS(String key) {
        Log.i(TAG, String.format("getting FitnessService with key %s", key));
        return blueprints.get(key);
    }


    public static FitnessService create(String key, HomePage hp) {
        Log.d("IN FITNESS CREATE", key);
        if(key.equals(google_fit)) {
            return new GoogleFitAdapter(hp);
        } else if(key.equals(test_fs)) {
            return new TestFitnessServiceAdapter(hp);
        } else return null;
    }
/*
    public interface BluePrint {
        FitnessService create();
    }
     */
}
