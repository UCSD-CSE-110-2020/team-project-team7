package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Route {

    // Public instance variables
    public String name;
    public String startingPoint;
    public int steps;
    public float distance;
    public String date;
    public int minutes;
    public int seconds;

    /**
     * Constructor for a Route object.
     *
     * @param name          - String labeling the object
     * @param startingPoint - String of the chosen starting point
     * @param steps         - int, num of steps
     * @param distance      - float, distance traveled
     */
    public Route(String name, String startingPoint, int steps, float distance) {
        this.name = name;
        this.startingPoint = startingPoint;
        this.steps = steps;
        this.distance = distance;
    }

    // Setter methods
    /**
     * Setter method for name instance var.
     * @param name - String denoting new name of starting point
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Setter method for startingPoint instance var.
     * @param startingPoint - String denoting new name of starting point
     */
    public void setStartingPoint(String startingPoint) {
        this.startingPoint = startingPoint;
    }
    /**
     * Setter method for date instance var.
     * @param date - String denoting a date for the route
     */
    public void setDate(String date) {
        this.date = date;
    }
    /**
     * Setter method for duration instance var.
     * @param minutes - int, how many mins a walk/run takes
     * @param seconds- int, how many secs a walk/run takes
     */
    public void setDuration(int minutes, int seconds) { this.minutes = minutes; this.seconds = seconds; }

    public void setDistance(float distance){
        this.distance = distance;
    }


}
