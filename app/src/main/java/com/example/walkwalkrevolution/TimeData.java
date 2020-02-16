package com.example.walkwalkrevolution;

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
    private long mockStartTime;
    private boolean isMockTimeSet;


    /**
     * Called only once upon first login, builds a String for initialization of TimeData fields.
     * Puts the String into TimeData.TIME_DATA as the key TimeData.TIME_DATA_FIELDS.
     */
    public static void initTimeData(SharedPreferences pref) {
        // Put the time variables into a string to be put in shared prefs
        // String below is formatted as: mockTime = 0 + mockStartTime = 0 + isMockTimeSet = false
        String timeDataFieldsStr = 0 + " " + 0 + " " + "false";

        Log.d(TAG, "Initializing TimeData instance variables to: " + timeDataFieldsStr);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(TIME_DATA_FIELDS, timeDataFieldsStr);
        editor.apply();
    }

    /**
     * Load data from TIME_DATA_FIELDS pref to update this instance of TimeData.
     */
    public void update(SharedPreferences prefs) {
        String timeDataFieldsStr = prefs.getString(TIME_DATA_FIELDS, "");

        // Split the String into an array: [mockTime, mockStartTime, isMockTimeSet]
        String[] timeDataFields = timeDataFieldsStr.split(" ");
        mockTime = Long.parseLong(timeDataFields[0]);
        mockStartTime= Long.parseLong(timeDataFields[1]);
        isMockTimeSet = Boolean.parseBoolean(timeDataFields[2]);

        Log.d(TAG, "Initialized Timedata from sharedprefs fields. Fields are:");
        Log.d(TAG, "mockTime is: " + mockTime);
        Log.d(TAG, "mockStartTime is: " + mockStartTime);
        Log.d(TAG, "isMockTimeSet is: " + isMockTimeSet);
    }

    /**
     * Constructor: Save the data from this instance of TimeData to SharedPreferences.
     */
    public void saveTimeData(SharedPreferences pref) {
        String timeDataFieldsStr = mockTime + " " + mockStartTime + " " + isMockTimeSet;

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(TIME_DATA_FIELDS, timeDataFieldsStr);
        editor.apply();
    }

    /**
     * Starts mock timing.
     * @param mockTime - the new time to start at
     */
    public void setMockTime(long mockTime) {
        Log.d(TAG, "Setting the current time to " + mockTime);

        this.mockTime = mockTime;
        isMockTimeSet = true;
        mockStartTime = System.currentTimeMillis();
    }

    /**
     * Returns the time or mocked time.
     */
    public long getTime() {
        if (isMockTimeSet) {
            return mockTime + (System.currentTimeMillis() - mockStartTime);

        } else {
            return System.currentTimeMillis();
        }
    }

}
