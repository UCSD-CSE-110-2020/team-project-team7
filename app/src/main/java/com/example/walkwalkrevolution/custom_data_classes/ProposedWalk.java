package com.example.walkwalkrevolution.custom_data_classes;


import android.util.Log;

import com.example.walkwalkrevolution.TeamMember;

import java.util.List;

/**
 * Class for storing data on Team Proposed Walks. Setter methods included, as well as
 * an overriden equals() methods.
 */
public class ProposedWalk {

    private final String TAG = "ProposedWalk";

    private String name, date, time, location;
    private TeamMember creator;
    private boolean isScheduled = false;

    private List<TeamMember> teammates;

    /**
     * Constructor
     * @param date
     * @param time
     */
    public ProposedWalk(String name, String date, String time, TeamMember creator) {
        this.name= name;
        this.creator = creator;
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

    public String getName() {
        return name;
    }
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public String getLocation() {
        return location;
    }
    public TeamMember getCreator() {
        return creator;
    }
    public boolean getIsScheduled(){return this.isScheduled;}


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
