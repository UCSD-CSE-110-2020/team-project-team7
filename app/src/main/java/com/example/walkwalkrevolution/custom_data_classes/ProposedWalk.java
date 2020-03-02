package com.example.walkwalkrevolution.custom_data_classes;


import android.util.Log;

/**
 * Class for storing data on Team Proposed Walks. Setter methods included, as well as
 * an overriden equals() methods.
 */
public class ProposedWalk {

    private final String TAG = "ProposedWalk";

    public String name, date, time, location;
    public boolean isScheduled = false;

    /**
     * Constructor
     * @param date
     * @param time
     */
    public ProposedWalk(String name, String date, String time) {
        this.name= name;
        setDate(date);
        setTime(time);
        Log.d(TAG, "Finished constructing a ProposedWalk with name: " + name);
    }

    // Setter methods
    public void setDate(String date) {
        this.date = date;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setIsScheduled(boolean isScheduled) {
        this.isScheduled = isScheduled;
    }


    /**
     * Compares an Object to this Proposed Walk to see if they're equal.
     */
    @Override
    public boolean equals(Object o) {
        // Edge cases, comparing o to itself or not a proposed walk
        if (o == this) {
            return true;
        }
        if (!(o instanceof ProposedWalk)) {
            return false;
        }

        // Typecast
        ProposedWalk p = (ProposedWalk) o;

        // Compare the data
        return name.equals(p.name) && date.equals(p.date)
                && time.equals(p.time) && location.equals(p.location)
                && (isScheduled == p.isScheduled);
    }

}
