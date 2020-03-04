package com.example.walkwalkrevolution;

import android.util.Pair;

import com.example.walkwalkrevolution.custom_data_classes.ProposedWalk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamMemberFactory {

    private static Map<String, TeamMember> members = new HashMap<>();
    private static List<Pair<String,Route>> teamRoutes = new ArrayList<>();
    private static ProposedWalk proposedWalk = null;

    /**
     * put/get MAP
     */
    public static void put(String userEmail, TeamMember details) {
        members.put(userEmail, details);
    }
    public static TeamMember get(String userEmail) {
        return members.get(userEmail);
    }

    /**
     * add/get LIST
     */
    public static void addRoute(Pair<String, Route> route) {
        teamRoutes.add(route);
    }
    public static List<Pair<String,Route>> getTeamRoutes() { return teamRoutes; }

    /**
     * set/get proposed walk
     * !!! SET THROUGH FIRESTORE
     */
    public static void setProposedWalk(ProposedWalk newProposedWalk) {
        proposedWalk = newProposedWalk;
    }
    public static ProposedWalk getProposedWalk() { return proposedWalk; }

    /**
     * RESET everything to populate with most recent
     */
    public static void resetMembers() {
        members.clear();
    }
    public static void resetRoutes() {
        teamRoutes.clear();
    }
}
