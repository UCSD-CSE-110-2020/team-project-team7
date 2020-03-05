package com.example.walkwalkrevolution;

import android.graphics.Color;

import java.util.Random;

/**
 * Object representing each team member's information
 * in /TEAMS/{teamID}/MEMBERS/{memberID} in FireStore
 */
public class TeamMember {

    private String name;
    private String email;
    private String initials = "";
    private String colorVal;
    private boolean pendingStatus;

    TeamMember() {}

    TeamMember(String name, String email, boolean pendingStatus) {
        this.name = name;
        this.email = email;
        this.pendingStatus = pendingStatus;
        initialsMaker();
        randomColorGenerator();
    }

    /**
     * Helper methods
     */
    // Helper method to create Initials of Team Member
    private void initialsMaker() {
        String[] nameSplit = name.split(" ");
        for(String n : nameSplit) {
            this.initials += n.charAt(0);
        }
    }
    // Helper method to generate random pastel color for user emblem
    private void randomColorGenerator() {
        Random rand = new Random();
        final int baseColor = Color.WHITE;
        final int baseRed = Color.red(baseColor);
        final int baseGreen = Color.green(baseColor);
        final int baseBlue = Color.blue(baseColor);
        final int r = (baseRed + rand.nextInt(256)) / 2;
        final int g = (baseGreen + rand.nextInt(256)) / 2;
        final int b = (baseBlue + rand.nextInt(256)) / 2;

        this.colorVal = String.format("#%06X", (0xFFFFFF & Color.rgb(r,g,b)));
    }

    /**
     * GETTER METHODS
     */
    public String getName() { return this.name; }
    public String getEmail() { return this.email; }
    public String getInitials() { return this.initials; }
    public String getColorVal() { return this.colorVal; }
    public boolean getPendingStatus() { return this.pendingStatus; }
}
