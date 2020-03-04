package com.example.walkwalkrevolution;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class TeammatesPageAdapter {


    public static List<TeamMember> retrieveTeammatesFromCloud(){

        //Use Yoshi's function to call Teammates from cloud
        List<TeamMember> teammates = new ArrayList<>();

        teammates.add(new TeamMember("Titan Ngo", "ttngo@ucsd.edu", "TitanID", "TEAMT", false));
        teammates.add(new TeamMember("Cindy Do", "cido@ucsd.edu", "CindyID", "TEAMC", true));
        teammates.add(new TeamMember("Amrit Singh", "aksingh@ucsd.edu", "AmritID", "TEAMA", false));

        return alphabetizeTeammates(teammates);
    }

    private static List<TeamMember> alphabetizeTeammates(List<TeamMember> teammates){
        TreeSet<TeamMember> treeSet = new TreeSet<TeamMember>(teammates);
        return new ArrayList<TeamMember>(treeSet);
    }
}
