package com.example.walkwalkrevolution;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class TeammatesPageAdapter {


    public static List<TeamMember> retrieveTeammatesFromCloud(){

        //Use Yoshi's function to call Teammates from cloud
        List<TeamMember> teammates = new ArrayList<>();

        TeamMember member1 = new TeamMember("Aashna Setia", "cido@ucsd.edu",  false);
        member1.setProposedWalkStatus(1);
        teammates.add(member1);

        TeamMember member2 = new TeamMember("Amrit Singh", "aksingh@ucsd.edu",  true);
        member2.setProposedWalkStatus(2);
        teammates.add(member2);

        TeamMember member3 = new TeamMember("Titan Ngo", "ttngo@ucsd.edu",  true);
        member3.setProposedWalkStatus(3);
        teammates.add(member3);

        TeamMember member4 = new TeamMember("Harrison Kim", "aksingh@ucsd.edu",  true);
        member4.setProposedWalkStatus(3);
        teammates.add(member4);

        teammates.add(new TeamMember("Yoshi Russel", "aksingh@ucsd.edu",  true));
        teammates.add(new TeamMember("Calvin Nguyen", "aksingh@ucsd.edu",  false));

        return alphabetizeTeammates(teammates);
    }

    private static List<TeamMember> alphabetizeTeammates(List<TeamMember> teammates){
        TreeSet<TeamMember> treeSet = new TreeSet<TeamMember>(teammates);
        return new ArrayList<TeamMember>(treeSet);
    }
}
