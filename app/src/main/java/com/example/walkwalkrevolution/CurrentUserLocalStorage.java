package com.example.walkwalkrevolution;

import android.content.SharedPreferences;

public class CurrentUserLocalStorage {

    public final static String SHARED_PREFS_CURRENT_USER_INFO = "currentUser";
    private final static String CURRENT_USER_NAME = "name";
    private static final String CURRENT_USER_EMAIL = "email";

    public static void firstTimeLogin(SharedPreferences sharedPreferences, String name, String email){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CURRENT_USER_NAME, name);
        editor.putString(CURRENT_USER_EMAIL, email);
        editor.apply();
    }

    public static String getCurrentUserName(SharedPreferences sharedPreferences){
        return sharedPreferences.getString(CURRENT_USER_NAME, "");
    }

    public static String getCurrentUserEmail(SharedPreferences sharedPreferences){
        return sharedPreferences.getString(CURRENT_USER_EMAIL, "");
    }
}
