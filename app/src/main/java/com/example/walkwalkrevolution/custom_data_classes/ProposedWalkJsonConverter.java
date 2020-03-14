package com.example.walkwalkrevolution.custom_data_classes;

import android.util.Log;

import com.google.gson.Gson;

/**
 * This class provides static public methods for converting Proposed Walk objects to and from jsons.
 */
public class ProposedWalkJsonConverter {

    public static final String TAG = "ProposedWalkJsonConverter";

    /**
     * Takes in a ProposedWalk and returns its Json string.
     */
    public static String convertWalkToJson(ProposedWalk proposedWalk) {
        Gson gson = new Gson();

        // Convert Proposed Walk to JSON string
        String proposedWalkStr = gson.toJson(proposedWalk);

        if (proposedWalk != null) {
            Log.d(TAG, "Converted a ProposedWalk with the name: " + proposedWalk.getName() + " to Json str");
        }

        return proposedWalkStr;
    }

    /**
     * Takes in a ProposedWalk that was converted into a Json string and returns a ProposedWalk.
     */
    public static ProposedWalk convertJsonToWalk(String proposedWalkStr) {
        Gson gson = new Gson();

        ProposedWalk proposedWalk = gson.fromJson(proposedWalkStr, ProposedWalk.class);

        if (proposedWalk != null) {
            Log.d(TAG, "Converted Json string to a ProposedWalk with the name: " + proposedWalk.getName());
        }

        return proposedWalk;
    }

}
