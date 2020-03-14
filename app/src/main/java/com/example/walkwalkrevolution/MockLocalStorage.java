package com.example.walkwalkrevolution;

import android.content.SharedPreferences;

import com.example.walkwalkrevolution.custom_data_classes.ProposedWalk;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Route;

//TODO - Mock Teammates, Team Routes, Proposed Walk, Personal Routes
public class MockLocalStorage {

    public final static String MOCK_LOCAL_STORAGE = "mockTesting";

    private final static String MOCK_TEAMMATES = "mockTeammates";
    private final static String MOCK_TEAM_ROUTES = "mockTeamRoutes";
    private final static String MOCK_PROPOSED_WALK = "mockTeamRoutes";
    private final static String MOCK_PERSONAL_WALKS = "mockTeamRoutes";

    public static void initializeMockStorage(SharedPreferences sharedPreferences){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Empty teammates
        Gson gson = new Gson();
        String json = gson.toJson(new ArrayList<TeamMember>());
        editor.putString(MOCK_TEAMMATES, json);

        //Empty Team Routes
        json = gson.toJson(new ArrayList<Route>());
        editor.putString(MOCK_TEAM_ROUTES, json);

        //Empty Proposed Walk
        editor.putString(MOCK_PROPOSED_WALK, null);

        //Some Personal Routes
        json = gson.toJson(new ArrayList<Route>());
        editor.putString(MOCK_PERSONAL_WALKS, json);

    }

    private static List<TeamMember> mockTeammate(){
        List<TeamMember> list = new ArrayList<>();
        list.add(new TeamMember("Titan Ngo", "ttngo@ucsd.edu", false ));
        return list;
    }


    private static TeamMember mockCurrentUser(){
        return null;
    }


}
