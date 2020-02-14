package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Route {

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

    // Array staying optional vars
    public String[] optionalFeaturesStr = new String[5];
    public int[] optionalFeatures = new int[5];


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
        this.startingPoint = startingPoint.trim();
        this.steps = steps;
        this.distance = distance;
        this.date = "date";
        this.minutes = 0;
        this.seconds = 0;
        this.isFavorited = false;
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

    public void toggleIsFavorited() { this.isFavorited = !isFavorited; }

    public boolean getIsFavorited() { return this.isFavorited; }

    public void setDistance(float distance){
        this.distance = distance;
    }

    public void setSteps(int steps){
        this.steps = steps;
    }

    public void setOptionalFeaturesStr(String[] optionalFeaturesStr){
        this.optionalFeaturesStr = optionalFeaturesStr;
    }

    public void setOptionalFeatures(int[] optionalFeatures){
        this.optionalFeatures = optionalFeatures;
    }

    /**
     * Compares two Routes.
     */
    public boolean compareRoute(Route route){
        if(this.name.equals(route.name)){
            return true;
        }
        return false;
    }

}

