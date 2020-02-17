package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

/**
 * Creates a Route object, and wraps up all information associated with it.
 */
public class Route {

    // Constant for logging
    private static final String TAG = "Route.class";

    // Public instance variables
    // Required vars
    public String name;
    public String startingPoint;
    public long steps;
    public double distance;
    public String date;
    public int minutes;
    public int seconds;
    public boolean isFavorited;
    public String notes;

    // Array saving optional vars (Flat/Hilly, etc.)
    public String[] optionalFeaturesStr = new String[5]; //Strings chosen from optionals
    public int[] optionalFeatures = new int[5];//States of optionals chosen


    /**
     * Constructor for a Route object.
     *
     * @param name          - String labeling the object
     * @param startingPoint - String of the chosen starting point
     * @param steps         - int, num of steps
     * @param distance      - float, distance traveled
     */
    public Route(String name, String startingPoint, long steps, double distance) {
        this.name = name.trim();
        setStartingPoint(startingPoint);
        this.steps = steps;
        this.distance = distance;
        // Initializes other vars
        this.date = "date";
        this.minutes = 0;
        this.seconds = 0;
        this.isFavorited = false;

        Log.d(TAG, "Creating a route class named: " + name);
    }


    // Setter methods
    /**
     * Setter method for name instance var.
     * @param name - String denoting new name of starting point
     */
    public void setName(String name) {
        this.name = name.trim();
    }
    /**
     * Setter method for startingPoint instance var.
     * @param startingPoint - String denoting new name of starting point
     */
    public void setStartingPoint(String startingPoint) {
        this.startingPoint = startingPoint.trim();

        // if starting point is empty string, set it to null
        if (this.startingPoint.equals("")) {
            Log.d(TAG, "Starting point set to null since its empty.");
            this.startingPoint = null;
        }
    }
    /**
     * Setter method for date instance var.
     * @param date - String denoting a date for the route
     */
    public void setDate(String date) {
        this.date = date.trim();
    }
    /**
     * Setter method for duration instance var.
     * @param minutes - int, how many mins a walk/run takes
     * @param seconds- int, how many secs a walk/run takes
     */
    public void setDuration(int minutes, int seconds) { this.minutes = minutes; this.seconds = seconds; }


    // Favoriting
    public void toggleIsFavorited() { this.isFavorited = !isFavorited; }
    public boolean getIsFavorited() { return this.isFavorited; }


    // Other (optional) features
    public void setOptionalFeaturesStr(String[] optionalFeaturesStr){
        this.optionalFeaturesStr = optionalFeaturesStr;
    }
    public void setOptionalFeatures(int[] optionalFeatures){
        this.optionalFeatures = optionalFeatures;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }


    /**
     * Compares two Routes. They are the same if they have the same name.
     */
    public boolean compareRoute(Route route){
        Log.d(TAG, "Comparing routes..." + this.name + " vs. " + route.name);
        if(this.name.equals(route.name)){
            Log.d(TAG, "Routes are the same");
            return true;
        }
        Log.d(TAG, "Routes are Different");
        return false;
    }

}

