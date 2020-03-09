package com.example.walkwalkrevolution;

import com.example.walkwalkrevolution.custom_data_classes.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class TeamRoutesListAdapter {

    public static List<Route> retrieveTeammatesFromCloud(){

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

        //HOW ARE WE KNOWING WHICH ROUTES THE USER WALKED?????

        return alphabetizeTeammates(list);
    }

    private static List<Route> alphabetizeTeammates(List<Route> routes){
        TreeSet<Route> treeSet = new TreeSet<Route>(routes);
        return new ArrayList<Route>(treeSet);
    }
}
