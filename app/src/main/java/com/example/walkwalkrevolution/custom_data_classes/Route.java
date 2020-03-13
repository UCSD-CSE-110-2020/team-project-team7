package com.example.walkwalkrevolution.custom_data_classes;

import android.util.Log;

import com.example.walkwalkrevolution.TeamMember;
import com.example.walkwalkrevolution.TreeSetComparator;

import java.util.Optional;

/**
 * Route object for storing information such as Name, Date, and Steps/Distance of a saved Route.
 * Built using a RouteBuilder (see below).
 */
public class Route implements Comparable<Route> {

    // Constant for logging
    private static final String TAG = "Route.class";

    // Public final instance variables
    public final String name, startingPoint, date, notes;
    public final long steps;
    public final double distance;
    public final int minutes, seconds;

    // Array saving optional vars (Flat/Hilly, etc.)
    public final String[] optionalFeaturesStr; //Strings of optionals chosen
    public final int[] optionalFeatures; //States of optionals chosen

    // Team functionality
    public final TeamMember creator;
    public boolean userHasWalkedRoute;

    // Toggable instance var
    public boolean isFavorited;


    /**
     * Constructor for a Route object. Takes in a RouteBuilder object.
     */
    public Route(RouteBuilder builder) {
        this.name = builder.name;
        this.startingPoint = builder.startingPoint;
        this.date = builder.date;
        this.notes = builder.notes;

        this.steps = builder.steps;
        this.distance = builder.distance;
        this.minutes = builder.minutes;
        this.seconds = builder.seconds;

        this.optionalFeaturesStr = builder.optionalFeaturesStr;
        this.optionalFeatures = builder.optionalFeatures;

        this.creator = builder.creator;
        this.userHasWalkedRoute = builder.userHasWalkedRoute;

        Log.d(TAG, "Creating a route class named: " + this.name);
    }


    // Favoriting
    public void toggleIsFavorited() { this.isFavorited = !isFavorited; }
    public boolean getIsFavorited() { return this.isFavorited; }
    public void setUserHasWalkedRoute(boolean userHasWalkedRoute) { this.userHasWalkedRoute = userHasWalkedRoute; }


    /**
     * Builder for a Route.
     */
    public static class RouteBuilder {

        // Private instance variables
        private String name, startingPoint, date, notes;
        private long steps;
        private double distance;
        private int minutes, seconds;
        private boolean isFavorited;

        // Array saving optional vars (Flat/Hilly, etc.)
        private String[] optionalFeaturesStr = new String[5]; //Strings of optionals chosen
        private int[] optionalFeatures = new int[5]; //States of optionals chosen

        // Team functionality
        private TeamMember creator;
        private boolean userHasWalkedRoute;


        /**
         * Returns a new RouteBuilder to reset all instance vars.
         */
        public static RouteBuilder newInstance() {
            return new RouteBuilder();
        }

        /**
         * Returns a Route built using this RouteBuilder's instance vars.
         */
        public Route buildRoute()
        {
            return new Route(this);
        }


        // Builder Setter methods ------------------------------------------------------------------
        public RouteBuilder setName(String name) {
            this.name = name.trim();
            return this;
        }
        public RouteBuilder setStartingPoint(String startingPoint) {
            this.startingPoint = startingPoint.trim();

            // if starting point is empty string, set it to null
            if (this.startingPoint.equals("")) {
                Log.d(TAG, "Starting point set to null since its empty.");
                this.startingPoint = null;
            }
            return this;
        }

        public RouteBuilder setSteps(long steps) {
            this.steps = steps;
            return this;
        }
        public RouteBuilder setDistance(double distance) {
            this.distance = distance;
            return this;
        }
        public RouteBuilder setDate(String date) {
            this.date = date.trim();
            return this;
        }
        public RouteBuilder setDuration(int minutes, int seconds) {
            this.minutes = minutes;
            this.seconds = seconds;
            return this;
        }

        // Team functionality
        public RouteBuilder setCreator(TeamMember creator){
            this.creator = creator;
            return this;
        }
        public RouteBuilder setUserHasWalkedRoute(boolean hasWalkedRoute){
            this.userHasWalkedRoute = hasWalkedRoute;
            return this;
        }

        // Other optional features
        public RouteBuilder setOptionalFeaturesStr(String[] optionalFeaturesStr){
            this.optionalFeaturesStr = optionalFeaturesStr;
            return this;
        }
        public RouteBuilder setOptionalFeatures(int[] optionalFeatures){
            this.optionalFeatures = optionalFeatures;
            return this;
        }
        public RouteBuilder setNotes(String notes) {
            this.notes = notes;
            return this;
        }

    } // End RouteBuilder class


    // ROUTE COMPARING -----------------------------------------------------------------------------
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

    @Override
    public int compareTo(Route route) {
        TreeSetComparator comparator = new TreeSetComparator();

        if (comparator.compare(this.name, route.name) == 0) {
            // RouteNames are the same, sort lexicographically by email
            return this.creator.getEmail().toLowerCase().compareTo(route.creator.getEmail().toLowerCase());

        } else {
            // RoutesNames are diff
            return comparator.compare(this.name, route.name);
        }
    }

    @Override
    public boolean equals(Object o) {
        // Edge cases, comparing o to itself or not a proposed walk
        if (o == this) {
            return true;
        }
        if (!(o instanceof Route)) {
            return false;
        }

        // Typecast
        Route p = (Route) o;

        return name.equals(p.name) && startingPoint.equals(p.startingPoint) && date.equals(p.date)
                && notes.equals(p.notes) && (steps == p.steps) && (distance == p.distance)
                && (minutes == p.minutes) && (seconds == p.seconds) && (creator == p.creator);
    }

}

