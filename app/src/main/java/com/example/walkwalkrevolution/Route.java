package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Route {

    // Public instance variables
    // Required vars
    public String name;
    public String startingPoint;
    public int steps;
    public float distance;
    public String date;
    public int minutes;
    public int seconds;
    
    // Optional vars
    public boolean isFlat; //Flat or Hilly
    public boolean isLoop; // Loop or OutBack
    public boolean isStreet; // Street or Trail
    public boolean isEven; // Even or Uneven
    public String difficulty; // Easy, medium, or hard
    public String notes;

    /**
     * Constructor for a Route object.
     *
     * @param name          - String labeling the object
     * @param startingPoint - String of the chosen starting point
     * @param steps         - int, num of steps
     * @param distance      - float, distance traveled
     */
    public Route(String name, String startingPoint, int steps, float distance) {
        this.name = name.trim();
        this.startingPoint = startingPoint.trim();
        this.steps = steps;
        this.distance = distance;
        this.date = "date";
        this.minutes = 0;
        this.seconds = 0;
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

    public void setDistance(float distance){
        this.distance = distance;
    }

    public void setSteps(int steps){
        this.steps = steps;
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

    /**
     * Setter method for isFlat.
     */
    public void setIsFlat(boolean flatOrHilly) { this.isFlat = flatOrHilly; }
    /**
     * Setter method for isLoop.
     */
    public void setIsLoop(boolean loopOrOutBack) { this.isLoop = loopOrOutBack; }
    /**
     * Setter method for isStreet.
     */
    public void setIsStreet(boolean streetOrTrail) { this.isStreet = streetOrTrail; }
    /**
     * Setter method for isEven.
     */
    public void setIsEven(boolean evenOrUneven) { this.isEven = evenOrUneven; }
    /**
     * Setter method for difficulty.
     */
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    /**
     * Setter method for note.
     */
    public void setNotes(String notes) { this.notes = notes; }

}
