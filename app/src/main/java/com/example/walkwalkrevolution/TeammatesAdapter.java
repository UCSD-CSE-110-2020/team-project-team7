package com.example.walkwalkrevolution;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class TeammatesAdapter {

    private List<TeamMember> teammates;

    public void retrieveTeammatesFromCloud(){
        this.teammates = null;
    }

    public void alphabetizeTeammates(){
        TreeSet<TeamMember> treeSet = new TreeSet<TeamMember>(teammates);
        this.teammates = new ArrayList<TeamMember>(treeSet);
    }
}
