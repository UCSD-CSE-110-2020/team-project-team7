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

    RecyclerViewAdapter adapter;


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
        adapter = new RecyclerViewAdapter(this, getTeamRoutes());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.d(TAG, "Finished initRecyclerView ");
    }

    private List<Route> getTeamRoutes(){
        List<Route> list = new ArrayList<>();

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