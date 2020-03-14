package com.example.walkwalkrevolution;

import android.util.Log;
import android.util.Pair;

import com.example.walkwalkrevolution.custom_data_classes.Route;
import com.google.firebase.firestore.auth.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class TeamRoutesListAdapter {

    // Stores any team routes that the user has edited (either walked or favorited)
    public static List<Route> userRoutes;
    private static final String TAG = "TeamRoutesListAdapter";


    /**
     * Gets both teamRoutes and team routes walked by the user from the cloud.
     * Saves them into the static vars, teamRoutes and userRoutes.
     * Returns a List with teamRoutes and userRoutes combined and sorted alphabetically.
     */
    public static List<Route> retrieveTeamRoutesFromCloud(){

        if(HomePage.MOCK_TESTING){
            userRoutes = new ArrayList<Route>();
        }else{
            userRoutes = CloudDatabase.getUserTeamRoutesWalked();
        }

        List<Route> teamRoutes = new ArrayList<Route>();

        List<Pair<String,Route>> teamRoutePairs = TeamMemberFactory.getTeamRoutes();

        // Iterate over all team routes received
        for (Pair<String,Route> pair : teamRoutePairs) {
            // Add route to teamRoute static var if the user hasn't walked it
            teamRoutes.add(pair.second);
            Log.d(TAG, pair.second.name);
        }

        return alphabetizeTeamRoutes(userRoutes, teamRoutes);
    }

    public static void saveWalkedUserRoutes(){
        Gson gson = new Gson();
        String json = gson.toJson(userRoutes);
        if(HomePage.MOCK_TESTING){
            UserDetailsFactory.get("currentUser").setTeamRoutesWalked(json);
        }else{
            CloudDatabase.storeUserTeamRoutesWalked(json);
        }
    }


    /**
     * Combines userRoutes (the team routes walked by the user) and teamRoutes,
     * then alphabetizes them.
     */
    private static List<Route> alphabetizeTeamRoutes(List<Route> userRoutes, List<Route> teamRoutes){
        TreeSet<Route> treeSet = new TreeSet<>();
        if(userRoutes.size() > 0){
            treeSet.addAll(userRoutes);

        }
        if(teamRoutes.size() > 0){
            treeSet.addAll(teamRoutes);
        }
        return new ArrayList<Route>(treeSet);
    }

    private  List<Route> mockTeamRoutes(){
                //Use Yoshi's function to call Teammates from cloud
        List<Route> list = new ArrayList<>();

        Route route2 = Route.RouteBuilder.newInstance()
                .setName("Bryan Avenue")
                .setStartingPoint("Archer Ave")
                .setSteps(20)
                .setDistance(1.4)
                .setCreator(new TeamMember("Titan Ngo", "ttngo@ucsd.edu",  false))
                .setUserHasWalkedRoute(true)
                .buildRoute();
        list.add(route2);

        Route route3 = Route.RouteBuilder.newInstance()
                .setName("Celt Drive")
                .setStartingPoint("Grisly Garden")
                .setSteps(440)
                .setDistance(30.4)
                .setCreator(new TeamMember("Cindy Do", "cido@ucsd.edu", false))
                .buildRoute();
        list.add(route3);

        Route route1 = Route.RouteBuilder.newInstance()
                .setName("Arker Walk")
                .setStartingPoint("Garden Grove")
                .setSteps(30)
                .setDistance(2.4)
                .setCreator(new TeamMember("Amrit Singh", "aksingh@ucsd.edu",  false))
                .buildRoute();
        list.add(route1);
        return list;
    }
}
