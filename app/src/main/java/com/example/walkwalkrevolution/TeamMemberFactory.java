package com.example.walkwalkrevolution;

import java.util.HashMap;
import java.util.Map;

public class TeamMemberFactory {

    private static Map<String, TeamMember> members = new HashMap<>();

    public static void put(String userID, TeamMember details) {
        members.put(userID, details);
    }

    public static TeamMember get(String userID) {
        return members.get(userID);
    }
}
