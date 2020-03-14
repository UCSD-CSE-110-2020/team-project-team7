package com.example.walkwalkrevolution;

import android.util.Pair;

import com.example.walkwalkrevolution.custom_data_classes.ProposedWalk;
import com.example.walkwalkrevolution.custom_data_classes.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is populated with database information
 * Used for fast local access instead of retrieving from database too often
 */
public class TeamMemberFactory {

    // Map where: key   = member email
    //            value = member TeamMate object
    private static Map<String, TeamMember> members = new HashMap<>();

    // List where: key   = member's email
    //             value = member's list of routes
    private static List<Pair<String, Route>> teamRoutes = new ArrayList<>();

    // Team's current proposed walk
    private static ProposedWalk proposedWalk = null;

    /** -------------------------- Getter/Setter of Team Members ------------------------------- */
    // put members into map
    public static void put(String userEmail, TeamMember details) {
        members.put(userEmail, details);
    }

    // get members from map
    public static TeamMember get(String userEmail) {
        return members.get(userEmail);
    }

    // get all members
    public static Map<String, TeamMember> getAllMembers() { return members; }

    /** -------------------------- Getter/Setter of Team Routes -------------------------------- */
    // add route to list of team routes
    public static void addRoute(Pair<String, Route> route)  { teamRoutes.add(route); }

    // get list of team routes
    public static List<Pair<String,Route>> getTeamRoutes() { return teamRoutes; }

    /** ----------------------- Getter/Setter of Team Proposed Walk ---------------------------- */
    // set our Team's Proposed Walk
    public static void setProposedWalk(ProposedWalk newProposedWalk) {
        proposedWalk = newProposedWalk;
    }

    // get our Team's Proposed Walk
    public static ProposedWalk getProposedWalk() { return proposedWalk; }

    /** ---------------------------- Reset Map/List to repopulate ------------------------------ */
    public static void resetMembers() {
        members.clear();
    }
    public static void resetRoutes() {
        teamRoutes.clear();
    }
}
