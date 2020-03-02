package com.example.walkwalkrevolution.custom_data_classes;

import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;


/**
 * Handles timing/mock timing.
 */
public class TimeData {

    // Constant for logging
    private static final String TAG = "TimeData";

    // Public constants for shared preferences xml
    public static final String TIME_DATA = "TimeData";
    public static final String TIME_DATA_FIELDS = "TimeDataFields";

    private long mockTime;


    /**
     * Called only once upon first login, builds a String for initialization of TimeData fields.
     * Puts the String into TimeData.TIME_DATA as the key TimeData.TIME_DATA_FIELDS.
     */
    public static void initTimeData(SharedPreferences pref) {
        // Put the time variables into a string to be put in shared prefs
        long mockTime = -1;

        Log.d(TAG, "Initializing TimeData instance variable to: " + mockTime);

        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(TIME_DATA_FIELDS, mockTime);
        editor.apply();
    }

    /**
     * Load data from TIME_DATA_FIELDS pref to update this instance of TimeData.
     */
    public void update(SharedPreferences prefs) {
        this.mockTime = prefs.getLong(TIME_DATA_FIELDS, -1);

        Log.d(TAG, "Initialized Timedata from sharedprefs fields.");
        Log.d(TAG, "mockTime is: " + mockTime);
    }

    /**
     * Constructor: Save the data from this instance of TimeData to SharedPreferences.
     */
    public void saveTimeData(SharedPreferences pref) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(TIME_DATA_FIELDS, mockTime);
        editor.apply();
    }

    /**
     * Starts mock timing.
     * @param mockTime - the new time to start at
     */
    public void setMockTime(long mockTime) {
        if (mockTime == -1) {
            Log.d(TAG, "Setting the current time to current time");
            this.mockTime = -1;
            return;
        }

        Log.d(TAG, "Setting the current time to " + mockTime);
        this.mockTime = mockTime;
    }

    /**
     * Returns the time or mocked time.
     */
    public long getTime() {
        if (mockTime != -1) {
            return mockTime;

        } else {
            return System.currentTimeMillis();
        }
    }

}
