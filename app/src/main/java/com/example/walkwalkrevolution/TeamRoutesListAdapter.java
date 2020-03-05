package com.example.walkwalkrevolution;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class TeamRoutesListAdapter {

    public static List<Route> retrieveTeammatesFromCloud(){

        //Use Yoshi's function to call Teammates from cloud
        List<Route> list = new ArrayList<>();

        Route route2 = new Route("Bryan Avenue", "Archer Ave", 20, 1.4);
        route2.setCreator(new TeamMember("Titan Ngo", "ttngo@ucsd.edu", "TitanID", "TEAMT", false));
        route2.setUserHasWalkedRoute(true);
        list.add(route2);

        Route route3 = new Route("Celt Drive", "Grisly Garden", 440, 30.4);
        route3.setCreator(new TeamMember("Cindy Do", "cido@ucsd.edu", "CindyID", "TEAMC", false));
        list.add(route3);

        Route route1 = new Route("Arker Walk", "Garden Grove", 30, 2.4);
        route1.setCreator(new TeamMember("Amrit Singh", "aksingh@ucsd.edu", "AmritID", "TEAMA", false));
        list.add(route1);


        //HOW ARE WE KNOWING WHICH ROUTES THE USER WALKED?????

        return alphabetizeTeammates(list);
    }

    private static List<Route> alphabetizeTeammates(List<Route> routes){
        TreeSet<Route> treeSet = new TreeSet<Route>(routes);
        return new ArrayList<Route>(treeSet);
    }
}
