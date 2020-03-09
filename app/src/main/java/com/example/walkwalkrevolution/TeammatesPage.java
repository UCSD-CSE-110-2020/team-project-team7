package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.walkwalkrevolution.RecycleViewAdapters.RecyclerViewAdapterTeammates;

import java.util.List;

public class TeammatesPage extends AppCompatActivity {


    private static final String TAG = "TeammatesPage";

    RecyclerViewAdapterTeammates adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teammates_page);

        // ----------- TESTING ------------ //
        MockFirestoreDatabase.teamCreationOnAccept(UserDetailsFactory.get("mockUserOne@ucsd.edu"), UserDetailsFactory.get("mockUserTwo@ucsd.edu"));
        // ----------- TESTING ------------ //

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
        return TeammatesPageAdapter.retrieveTeammatesFromCloud();
    }

    /**
     * Add button clicked, so redirects to AddTeamForm.
     */
    private void redirectToTeammateAddForm(){
        Log.d(TAG, "+ Clicked --> Going to AddTeammate Form");
        Intent intent = new Intent(TeammatesPage.this, AddTeammate.class);
        startActivity(intent);
    }

    /**
     * Home button clicked, so redirects to HomePage. Saves routes before switching pages.
     */
    private void redirectToProposedWalkDetailsForm(){
        Log.d(TAG, "HomeButton Clicked --> Going to HomePage");
        startActivity(new Intent(TeammatesPage.this, ProposedWalkDetailsPage.class));
    }

}
