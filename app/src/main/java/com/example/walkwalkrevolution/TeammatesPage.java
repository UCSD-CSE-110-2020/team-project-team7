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

import java.util.ArrayList;
import java.util.List;

public class TeammatesPage extends AppCompatActivity {


    private static final String TAG = "TeammatesPage";

    RecyclerViewAdapterTeammates adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teammates_page);

        initRecyclerView();

        Button addTeammateButton = (Button) findViewById(R.id.addTeammateButton);

        addTeammateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToTeammateAddForm();
            }
        });

        Button getProposedWalkButton = (Button) findViewById(R.id.proposedWalkButton);

        getProposedWalkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToProposedWalkDetailsForm();
            }
        });

    }

    /**
     * Calls the RecyclerViewAdapter class to create and display all routes to screen.
     */
    private void initRecyclerView(){
        Log.d(TAG, "Starting initRecyclerView ");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewTeammates);

        adapter = new RecyclerViewAdapterTeammates(this, loadTeammates());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "Finished initRecyclerView ");
    }

    private List<TeamMember> loadTeammates(){
        List<TeamMember> teammates = new ArrayList<>();

        teammates.add(new TeamMember("Amrit Singh", "AS", 0x7986CB));
        teammates.add(new TeamMember("Titan Ngo", "TN", 0xB2DFDB));
        teammates.add(new TeamMember("Cindy Do", "CD", 0xFFCCBC));
        return teammates;
    }

    /**
     * Add button clicked, so redirects to RouteForm. Saves routes before switching pages.
     */
    private void redirectToTeammateAddForm(){
        Log.d(TAG, "+ Clicked --> Going to RoutesForm");
//        Intent intent = new Intent(RoutesList.this, RoutesForm.class);
//        intent.putExtra("From_Intent", ROUTE_CREATE_INTENT);
//        startActivity(intent);
//        finish();
    }

    /**
     * Home button clicked, so redirects to HomePage. Saves routes before switching pages.
     */
    private void redirectToProposedWalkDetailsForm(){
        Log.d(TAG, "HomeButton Clicked --> Going to HomePage");
//        startActivity(new Intent(RoutesList.this, HomePage.class));
//        finish();
    }

}
