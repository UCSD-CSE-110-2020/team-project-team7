package com.example.walkwalkrevolution;

import android.graphics.Color;

import java.util.Random;


public class TeamMember implements Comparable<TeamMember>{
/**
 * Object representing each team member's information
 * in /TEAMS/{teamID}/MEMBERS/{memberID} in FireStore
 */

    private String name;
    private String email;
    private String initials = "";
    private int colorVal;
    private boolean pendingStatus;
    private int proposedWalkStatus;//0 = Pending, 1 = Bad Route, 2 = Bad Time, 3 = Accepted


    TeamMember() {}

    public TeamMember(String name, String email, boolean pendingStatus) {
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

    @Override
    public int compareTo(TeamMember teamMember) {
        TreeSetComparator comparator = new TreeSetComparator();
        return comparator.compare(this.getName(), teamMember.getName());
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

        this.colorVal = Color.rgb(r,g,b);
    }

    /**
     * GETTER METHODS
     */
    public String getName() { return this.name; }
    public String getEmail() { return this.email; }
    public String getInitials() { return this.initials; }
    public int getColorVal() { return this.colorVal; }
    public boolean getPendingStatus() { return this.pendingStatus; }
    public int getProposedWalkStatus() { return this.proposedWalkStatus; }

    /**
     * SETTER METHODS
     */
    public void setTeam(String teamID) {
        this.pendingStatus = true;
    }
    public void setProposedWalkStatus(int status) {
        this.proposedWalkStatus = status;
    }
    public void setPendingStatus(boolean pendingStatus) { this.pendingStatus = pendingStatus;}

}
