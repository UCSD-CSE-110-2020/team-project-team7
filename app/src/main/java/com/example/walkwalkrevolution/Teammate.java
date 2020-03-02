package com.example.walkwalkrevolution;

import android.util.Log;

public class Teammate {

    private static final String TAG = "Teammate";

    private String name;
    private String initials;
    private String hexaColor;

    public Teammate(String name, String initials, int hexaColor) {
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

}
