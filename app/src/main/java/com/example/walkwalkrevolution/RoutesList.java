package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * TO-DO: Add function for "save" button on details - JUST for tree set
 * Add button on Routes page - link to Titan's Details page
 *
 *
 * Eventually....
 * Functionality in Adapter.java for Start button, Favorite Button, Delete Button ("Are you sure?" Dialogue)
 * Needed Toast when additional entry added in Routes page
 */
public class RoutesList extends AppCompatActivity {

    private static final String TAG = "RoutesList";

    //public TreeSet<Route> routes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_list);

        initRecyclerView();


    }

    private void initRecyclerView(){
        Log.d(TAG, "Starting initRecyclerView ");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewRoutes);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //replace with loadTreeSet instead of routes
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, TreeSetManipulation.loadTreeSet(sharedPreferences));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.d(TAG, "Finished initRecyclerView ");
    }
/*
    private void checkForRoutesDatabaseExistence(){
        Log.d(TAG, "checkForRoutesDatabaseExistence Null: " + (this.routes == null));
        this.routes = new TreeSet<>(new treeSetComparator());
        Route a = new Route("Ark Walk", "SP");
        a.setDate("01/01/2020");
        a.setSteps(2000);
        a.setDistance(1);
        Route b = new Route("Berry Avenue", "SP");
        b.setDate("12/28/2020");
        b.setSteps(500);
        b.setDistance(0.9f);
        Route c = new Route("Gary's Weird Ass Garden", "SP");
        c.setDate("09/09/2000");
        c.setSteps(5);
        c.setDistance(0.1f);
        Route d = new Route("Sunset Valley (Streets)", "SP");
        d.setDate("10/20/2025");
        d.setSteps(100000);
        d.setDistance(50.7f);
        //must remove whitespace from beginning and end
        Route e = new Route("Some really long ass text that keeps going until I'm hoping it gets cut off", "ll");
        e.setDate("10/20/2025");
        e.setSteps(100000);
        e.setDistance(50.7f);
        this.routes.add(b);
        this.routes.add(d);
        this.routes.add(c);
        this.routes.add(a);
        this.routes.add(e);
        Log.d(TAG, "checkForRoutesDatabaseExistence Size: " + (this.routes.size()));
        initRecyclerView();
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//
//        //Called first time app launch (eventually place to height screen)
//        if (!prefs.getBoolean("firstTimeLaunch", false)) {
//            //initializes datebase
//            databaseSetup();
//
//            // mark first time has ran.
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putBoolean("firstTimeLaunch", true);
//            editor.apply();
//        }
//        //app has run before --> display recycler view
//        else{
//            loadTreeSet();
//            initRecyclerView();
//        }
    }
    private void databaseSetup(){
        this.routes = new TreeSet<>(new treeSetComparator());
        //saveTreeSet();
    }
    class treeSetComparator implements Comparator<Route> {
        @Override
        public int compare(Route route, Route t1) {
            return route.name.compareTo(t1.name);
        }
    }
 */
}