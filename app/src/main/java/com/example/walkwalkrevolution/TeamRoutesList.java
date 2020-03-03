package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * RoutesScreen that holds all the Routes saved on the app, allowing interactivity from user.
 */
public class TeamRoutesList extends AppCompatActivity {

    private static final String TAG = "TeamRoutesList";

    RecyclerViewAdapterTeam adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_routes_list);

        initRecyclerView();

        Button goToPersonalRoutesPage = (Button) findViewById(R.id.goToPersonalRoutesPage);

        goToPersonalRoutesPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToPersonalRoutesPage();
            }
        });

        Button goToHomePage = (Button) findViewById(R.id.goToHomePage);

        goToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToHomePage();
            }
        });

    }

    /**
     * Calls the RecyclerViewAdapter class to create and display all routes to screen.
     */
    private void initRecyclerView(){
        Log.d(TAG, "Starting initRecyclerView ");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewTeamRoutes);
        //SharedPreferences sharedPreferences = getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE);

        //create the adapter and set the recylerview to update the screen
        adapter = new RecyclerViewAdapterTeam(this, getTeamRoutes());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.d(TAG, "Finished initRecyclerView ");
    }

    private List<Route> getTeamRoutes(){
        List<Route> list = new ArrayList<>();
        Route route1 = new Route("Arker Walk", "Garden Grove", 30, 2.4);
        route1.setCreator(new TeamMember("Amrit Singh", "AS", 0x7986CB));
        list.add(route1);

        Route route2 = new Route("Bryan Avenue", "Archer Ave", 20, 1.4);
        route2.setCreator(new TeamMember("Titan Ngo", "TN", 0xB2DFDB));
        list.add(route2);

        Route route3 = new Route("Celt Drive", "Grisly Garden", 440, 30.4);
        route3.setCreator(new TeamMember("Cindy Do", "CD", 0xFFCCBC));
        list.add(route3);

        return list;
    }

    /**
     * Add button clicked, so redirects to RouteForm. Saves routes before switching pages.
     */
    private void redirectToPersonalRoutesPage(){
        Log.d(TAG, "+ Clicked --> Going to RoutesForm");
        Intent intent = new Intent(TeamRoutesList.this, RoutesList.class);
        startActivity(intent);
        finish();
    }

    /**
     * Home button clicked, so redirects to HomePage. Saves routes before switching pages.
     */
    private void redirectToHomePage(){
        Log.d(TAG, "HomeButton Clicked --> Going to HomePage");
        startActivity(new Intent(TeamRoutesList.this, HomePage.class));
        finish();
    }
}