package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.walkwalkrevolution.RecycleViewAdapters.RecyclerViewAdapterScheduledWalks;
import com.example.walkwalkrevolution.RecycleViewAdapters.RecyclerViewAdapterTeammates;
import com.example.walkwalkrevolution.custom_data_classes.ProposedWalk;

import java.util.ArrayList;
import java.util.List;

public class ScheduledWalksPage extends AppCompatActivity {

    private static final String TAG = "ScheduledWalksPage";

    private RecyclerViewAdapterScheduledWalks adapter;
    private List<ProposedWalk> scheduledWalks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_walks_page);

        setScheduledWalks();

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "Starting initRecyclerView ");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewScheduledRoutes);

        adapter = new RecyclerViewAdapterScheduledWalks(this, scheduledWalks);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "Finished initRecyclerView ");
    }

    private void setScheduledWalks(){
        TeamMember creator = new TeamMember("Cindy Do", "cdo@ucsd.edu",  true);
        ProposedWalk walk = new ProposedWalk("Grizzly Lane", "3/19/20", "2:39 PM", creator);
        walk.setLocation("Splash Mountain");
        walk.setIsScheduled(true);
        List<TeamMember> teammates = TeammatesPageAdapter.retrieveTeammatesFromCloud();
        walk.setTeammates(teammates);

        List<ProposedWalk> list = new ArrayList<>();
        list.add(walk);

        this.scheduledWalks = list;
    }
}
