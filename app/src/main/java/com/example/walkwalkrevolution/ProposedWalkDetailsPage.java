package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.walkwalkrevolution.RecycleViewAdapters.RecyclerViewAdapterPersonal;
import com.example.walkwalkrevolution.RecycleViewAdapters.RecyclerViewAdapterProposedAvailability;
import com.example.walkwalkrevolution.custom_data_classes.ProposedWalk;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProposedWalkDetailsPage extends AppCompatActivity {

    private static final String TAG = "ProposedWalkDetailsPage";

    RecyclerViewAdapterProposedAvailability adapter;
    ProposedWalk proposedWalk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposed_walk_details_page);

        setProposedWalk();
        setWalkInformation();
        initRecyclerView();

        renderUserLayout();
    }

    /**
     * Calls the RecyclerViewAdapter class to create and display all routes to screen.
     */
    private void initRecyclerView(){
        Log.d(TAG, "Starting initRecyclerView ");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewTeammates);

        //create the adapter and set the recylerview to update the screen
        adapter = new RecyclerViewAdapterProposedAvailability(this, proposedWalk);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.d(TAG, "Finished initRecyclerView ");
    }

    private void setWalkInformation(){
        Log.d(TAG, "Starting initRecyclerView ");

        TextView walkName = (TextView) findViewById(R.id.walkName);
        walkName.setText("Route: " + proposedWalk.getName());
        walkName.setEnabled(false);

        TextView walkDate = (TextView) findViewById(R.id.walkDate);
        walkDate.setText("Date: " + proposedWalk.getDate());
        walkDate.setEnabled(false);

        TextView walkTime = (TextView) findViewById(R.id.walkTime);
        walkTime.setText("Time: " + proposedWalk.getTime());
        walkTime.setEnabled(false);

        TextView walkLocation = (TextView) findViewById(R.id.walkLocation);
        walkLocation.setText("Location: " + proposedWalk.getLocation());
        walkLocation.setEnabled(false);

        TeamMember creator = proposedWalk.getCreator();
        Button icon = (Button) findViewById(R.id.creatorIcon);
        icon.setText(creator.getInitials().toString());
        icon.getBackground().setColorFilter(creator.getColorVal(), PorterDuff.Mode.MULTIPLY);

//        ((TextView) findViewById(R.id.walkName)).setText("Route: " + proposedWalk.getName());
//        ((TextView) findViewById(R.id.walkDate)).setText("Date: " + proposedWalk.getDate());
//        ((TextView) findViewById(R.id.walkTime)).setText("Time: " + proposedWalk.getTime());
//        ((TextView) findViewById(R.id.walkLocation)).setText("Location: " + proposedWalk.getLocation());

    }

    private void renderUserLayout(){

    }

    private void setProposedWalk(){
        TeamMember creator = new TeamMember("Cindy Do", "aksingh@ucsd.edu", "AmritID", "TEAMA", true);
        ProposedWalk walk = new ProposedWalk("Grizzly Lane", "3/19/20", "2:39 PM", creator);
        walk.setLocation("Splash Mountain");
        List<TeamMember> teammates = TeammatesPageAdapter.retrieveTeammatesFromCloud();
        walk.setTeammates(teammates);
        this.proposedWalk = walk;
        sortTeammatesByStatus();
    }

    private void sortTeammatesByStatus(){
        Collections.sort(proposedWalk.getTeammates(), new TeammateStatusComparator<>() );
    }

    static class TeammateStatusComparator<TeamMember> implements Comparator<com.example.walkwalkrevolution.TeamMember> {
        @Override
        public int compare(com.example.walkwalkrevolution.TeamMember teamMember, com.example.walkwalkrevolution.TeamMember t1) {
            return Integer.compare(t1.getProposedWalkStatus(), teamMember.getProposedWalkStatus());
        }
    }
}
