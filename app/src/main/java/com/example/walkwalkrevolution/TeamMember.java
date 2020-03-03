package com.example.walkwalkrevolution;

import android.util.Log;

import java.util.Comparator;

public class TeamMember implements Comparable<TeamMember> {

    private static final String TAG = "Teammate";

    private String name;
    private String initials;
    private String hexaColor;

    public TeamMember(String name, String initials, int hexaColor) {
        this.name = name.trim();
        this.initials = initials.trim();
        this.hexaColor = String.format("#%06X", (0xFFFFFF & hexaColor));

        Log.d(TAG, "Creating a Teammate named: " + name);
    }

    public String getName(){
        return this.name;
    }

    public String getInitials(){
        return this.initials;
    }

    public String getColor(){
        return this.hexaColor;
    }


    @Override
    public int compareTo(TeamMember teamMember) {
        TreeSetComparator comparator = new TreeSetComparator();
        return comparator.compare(this.getName(), teamMember.getName());
    }
}
