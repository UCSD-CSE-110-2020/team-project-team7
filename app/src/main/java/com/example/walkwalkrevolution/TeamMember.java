package com.example.walkwalkrevolution;

import android.graphics.Color;

import java.util.Random;

public class TeamMember {

    private String name;
    private String email;
    private String initials = "";
    private String team = null;
    private int colorVal;

    TeamMember(String name, String email) {
        this.name = name;
        this.email = email;
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

        this.colorVal = Color.rgb(r,g,b);
    }
}
