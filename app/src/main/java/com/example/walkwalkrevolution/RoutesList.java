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

import java.util.TreeSet;

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
    public static final String ROUTE_CREATE_INTENT = "From_Routes_Creation";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_list);

        initRecyclerView();

        Button addRouteButton = (Button) findViewById(R.id.addRouteButton);

        addRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToFormForRouteCreation();
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

    private void initRecyclerView(){
        Log.d(TAG, "Starting initRecyclerView ");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewRoutes);
        SharedPreferences sharedPreferences = getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE);
        //RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, routes);
        //replace with loadTreeSet instead of routes
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, TreeSetManipulation.loadTreeSet(sharedPreferences));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.d(TAG, "Finished initRecyclerView ");
    }

    private void redirectToFormForRouteCreation(){
        Intent intent = new Intent(RoutesList.this, RoutesForm.class);
        intent.putExtra("From_Intent", ROUTE_CREATE_INTENT);
        startActivity(intent);
    }

    private void redirectToHomePage(){
        startActivity(new Intent(RoutesList.this, HomePage.class));
    }

    @Override
    public void onBackPressed() {
    }
}