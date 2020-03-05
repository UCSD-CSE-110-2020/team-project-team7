package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * RoutesScreen that holds all the Routes saved on the app, allowing interactivity from user.
 */
public class RoutesList extends AppCompatActivity {

    private static final String TAG = "RoutesList";
    public static final String ROUTE_CREATE_INTENT = "From_Routes_Creation";

    RecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_list);

        initRecyclerView();

        // TODO DATABASE TESTING
        // TODO HARDCODED ID, LATER MOCK_USER_ONE SHOULD BE ACQUIRED THROUGH GOOGLE AUTH
        // TODO LATER MOCK_TEAMMATE_ID NEEDS TO BE ACQUIRED WHEN INVITING SOMEONE
        // TODO THESE FUNCTIONS SHOULD BE CALLED AFTER SOMEONE ACCEPTS YOUR INVITE
//        MockFirestoreDatabase.addTeam("CalvinID", "YoshiID");

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

    @Override
    protected void onStart() {
        super.onStart();
        MockFirestoreDatabase.routesListOnStartFireStore(
                UserDetailsFactory.get("yrussell@gmail.com"));
    }

    /**
     * Calls the RecyclerViewAdapter class to create and display all routes to screen.
     */
    private void initRecyclerView(){
        Log.d(TAG, "Starting initRecyclerView ");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewRoutes);
        SharedPreferences sharedPreferences = getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE);

        //create the adapter and set the recylerview to update the screen
        adapter = new RecyclerViewAdapter(this, TreeSetManipulation.loadTreeSet(sharedPreferences));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.d(TAG, "Finished initRecyclerView ");
    }

    /**
     * Add button clicked, so redirects to RouteForm. Saves routes before switching pages.
     */
    private void redirectToFormForRouteCreation(){
        saveRoutes();
        Log.d(TAG, "+ Clicked --> Going to RoutesForm");
        Intent intent = new Intent(RoutesList.this, RoutesForm.class);
        intent.putExtra("From_Intent", ROUTE_CREATE_INTENT);
        startActivity(intent);
        finish();
    }

    /**
     * Home button clicked, so redirects to HomePage. Saves routes before switching pages.
     */
    private void redirectToHomePage(){
        saveRoutes();
        Log.d(TAG, "HomeButton Clicked --> Going to HomePage");
        startActivity(new Intent(RoutesList.this, HomePage.class));
        finish();
    }

    /**
     * Saves routes from adapter to SharedPreferences.
     */
    private void saveRoutes(){
        Log.d(TAG, "All Routes from Recycler Saved to SharedPreferences");
        SharedPreferences prefs = getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE);
        TreeSetManipulation.saveTreeSet(prefs, adapter.routes);
    }

    /**
     * Doesn't allow any back presses, so user naviages page from available buttons.
     */
    @Override
    public void onBackPressed() {
        Log.d(TAG, "User clicked onBackButton");
    }

    @Override
    protected void onDestroy() {
        saveRoutes();
        super.onDestroy();
    }

    /**
     * User leaves the app while on this page, so makes sure to save the routes.
     */
    @Override
    protected void onPause() {
        Log.d(TAG, "User left the app");
        saveRoutes();
        super.onPause();
    }
}