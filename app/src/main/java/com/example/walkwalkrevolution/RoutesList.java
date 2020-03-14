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

import com.example.walkwalkrevolution.RecycleViewAdapters.RecyclerViewAdapterPersonal;
import com.example.walkwalkrevolution.custom_data_classes.Route;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

/**
 * RoutesScreen that holds all the Routes saved on the app, allowing interactivity from user.
 */
public class RoutesList extends AppCompatActivity {

    private static final String TAG = "RoutesList";
    public static final String ROUTE_CREATE_INTENT = "From_Routes_Creation";

    RecyclerViewAdapterPersonal adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_list);

        if (HomePage.MOCK_TESTING) {
            initRecyclerView();
        } else {
            // Once current user's routes are fetched from database
            CloudDatabase.populateUserRoutes(new CloudCallBack() {
                @Override
                public void callBack() {
                    initRecyclerView();
                }
            });
        }

        Button addRouteButton = (Button) findViewById(R.id.addRouteButton);

        addRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToFormForRouteCreation();
            }
        });

        Button goToTeamRoutes = (Button) findViewById(R.id.goToTeamRoutes);

        goToTeamRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToTeamRoutesPage();
            }
        });

    }

    /**
     * Calls the RecyclerViewAdapter class to create and display all routes to screen.
     */
    private void initRecyclerView() {
        Log.d(TAG, "Starting initRecyclerView ");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewRoutes);
        SharedPreferences sharedPreferences = getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE);

        //create the adapter and set the recylerview to update the screen
        if (HomePage.MOCK_TESTING) {
            adapter = new RecyclerViewAdapterPersonal(this, mockTreeSet());
        } else {
            adapter = new RecyclerViewAdapterPersonal(this, TreeSetManipulation.loadTreeSet(sharedPreferences));
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.d(TAG, "Finished initRecyclerView ");
    }

    /**
     * Add button clicked, so redirects to RouteForm. Saves routes before switching pages.
     */
    private void redirectToFormForRouteCreation() {
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
    private void redirectToTeamRoutesPage() {
        saveRoutes();
        Log.d(TAG, "HomeButton Clicked --> Going to Team Routes");
        startActivity(new Intent(RoutesList.this, TeamRoutesList.class));
        finish();
    }

    /**
     * Saves routes from adapter to SharedPreferences.
     */
    private void saveRoutes() {
        Log.d(TAG, "All Routes from Recycler Saved to SharedPreferences");
        if(HomePage.MOCK_TESTING){
            return;
        }

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


    private List<Route> mockTreeSet() {
        ArrayList<Route> routeList = new ArrayList<Route>();

        UserDetails user = UserDetailsFactory.get("currentUser");

        routeList.add(Route.RouteBuilder.newInstance()
                .setName("Canyon Hike")
                .setStartingPoint("Voigt Drive")
                .setSteps(500)
                .setDistance(1.3)
                .setDate("03/11/2020")
                .setDuration(12, 49)
                .setUserHasWalkedRoute(false)
                .setCreator(new TeamMember(user.getName(), user.getEmail(), true))
                .buildRoute());
        routeList.add(Route.RouteBuilder.newInstance()
                .setName("San Diego Walk")
                .setStartingPoint("UCSD")
                .setSteps(1450)
                .setDistance(2.6)
                .setDate("03/12/2020")
                .setDuration(24, 29)
                .setUserHasWalkedRoute(false)
                .setCreator(new TeamMember(user.getName(), user.getEmail(), true))
                .buildRoute());

        return routeList;
    }

}