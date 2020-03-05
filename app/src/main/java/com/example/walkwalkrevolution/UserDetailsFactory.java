package com.example.walkwalkrevolution;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to fill factory with user data from FireStore for faster local access
 */
public class UserDetailsFactory {

    // Holds Map of users, mostly will only have one user, the current user
    private static Map<String, UserDetails> users = new HashMap<>();

    // Put users info into map
    public static void put(String userEmail, UserDetails details) {
        users.put(userEmail, details);
    }

    // Get users info from map
    public static UserDetails get(String userEmail) {
        return users.get(userEmail);
    }
}
