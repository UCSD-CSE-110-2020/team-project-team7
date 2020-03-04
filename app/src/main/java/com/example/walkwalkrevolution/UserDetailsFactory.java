package com.example.walkwalkrevolution;

import java.util.HashMap;
import java.util.Map;

public class UserDetailsFactory {

    private static Map<String, UserDetails> users = new HashMap<>();

    public static void put(String userEmail, UserDetails details) {
        users.put(userEmail, details);
    }

    public static UserDetails get(String userEmail) {
        return users.get(userEmail);
    }
}
