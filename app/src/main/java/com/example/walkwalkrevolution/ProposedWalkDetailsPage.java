package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walkwalkrevolution.RecycleViewAdapters.RecyclerViewAdapterPersonal;
import com.example.walkwalkrevolution.RecycleViewAdapters.RecyclerViewAdapterProposedAvailability;
import com.example.walkwalkrevolution.RecycleViewAdapters.RecyclerViewAdapterScheduledWalks;
import com.example.walkwalkrevolution.custom_data_classes.ProposedWalk;
import com.example.walkwalkrevolution.custom_data_classes.ProposedWalkJsonConverter;
import com.example.walkwalkrevolution.proposed_walk_observer_pattern.ProposedWalkObservable;
import com.google.android.gms.common.api.GoogleApiActivity;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ProposedWalkDetailsPage extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ProposedWalkDetailsPage";

    RecyclerViewAdapterProposedAvailability adapter;
    ProposedWalk proposedWalk;
    List<TeamMember> teammates;
    TeamMember currentUser;

    Button badTime, badRoute, accept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposed_walk_details_page);

        CloudDatabase.populateTeamMateFactory(new CloudCallBack() {
            @Override
            public void callBack() {
                setWalkInformation();
                initRecyclerView();
                renderUserLayout();
            }
        });
    }

    /**
     * Calls the RecyclerViewAdapter class to create and display all routes to screen.
     */
    private void initRecyclerView(){
        Log.d(TAG, "Starting initRecyclerView ");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewTeammates);

        //create the adapter and set the recylerview to update the screen
        adapter = new RecyclerViewAdapterProposedAvailability(this, teammates);
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

        ImageButton googleMapsButton = (ImageButton) findViewById(R.id.googleMaps);
        Button scheduleWalk = (Button) findViewById(R.id.scheduleWalkButton);
        Button withdrawWalk = (Button) findViewById(R.id.withdrawWalkButton);
        badTime = (Button) findViewById(R.id.badTime);
        badRoute = (Button) findViewById(R.id.badRoute);
        accept = (Button) findViewById(R.id.Accept);


        googleMapsButton.setOnClickListener(this);
        scheduleWalk.setOnClickListener(this);
        withdrawWalk.setOnClickListener(this);
        badTime.setOnClickListener(this);
        badRoute.setOnClickListener(this);
        accept.setOnClickListener(this);

        initializeFields();

        orangeBackgroundForChosenAvailability();
    }

    private void initializeFields(){
        Map<String, TeamMember> map = TeamMemberFactory.getAllMembers();
        this.teammates = new ArrayList<>();
        teammates.addAll(map.values());
        this.currentUser = CloudDatabase.currentUserMember;

    }

    private void renderUserLayout(){
        //creator of walk
        if(proposedWalk.getCreator().getEmail().equals(currentUser.getEmail())){
            ((LinearLayout) findViewById(R.id.buttonsLayoutUser)).setVisibility(View.INVISIBLE);
            ((LinearLayout) findViewById(R.id.buttonsLayoutCreator)).setVisibility(View.VISIBLE);

            if(proposedWalk.getIsScheduled()){
                ((Button) findViewById(R.id.scheduleWalkButton)).setVisibility(View.INVISIBLE);
            }
        }
        //some teammate
        else{
            ((LinearLayout) findViewById(R.id.buttonsLayoutUser)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.buttonsLayoutCreator)).setVisibility(View.INVISIBLE);
        }

    }

    private void launchGoogleMaps(){
        Uri uri = Uri.parse("geo:0,0?q=" + proposedWalk.getLocation());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
        Toast.makeText(this, "Google Maps Launched", Toast.LENGTH_SHORT).show();
    }

    private void withdrawWalk(){
        ProposedWalkObservable.clearProposedWalk();
        startActivity(new Intent(ProposedWalkDetailsPage.this, ScheduledWalksPage.class));
        Toast.makeText(this, "Successfully Withdrawn", Toast.LENGTH_SHORT).show();
    }

    private void scheduleWalk(){
        this.proposedWalk.setIsScheduled(true);
        ProposedWalkObservable.setProposedWalk(this.proposedWalk);
        Intent intent = new Intent(ProposedWalkDetailsPage.this, ScheduledWalksPage.class);
        startActivity(intent);
        Toast.makeText(this, "Successfully Scheduled", Toast.LENGTH_SHORT).show();
    }

    private void availabilityChanged(int status){
        this.currentUser.setProposedWalkStatus(status);
        CloudDatabase.updateCurrentUserInTeam();
        orangeBackgroundForChosenAvailability();
        sortTeammatesByStatus();
        this.adapter.notifyDataSetChanged();
        Toast.makeText(this, "Updated Availability", Toast.LENGTH_SHORT).show();
    }

    private void orangeBackgroundForChosenAvailability(){
        switch(currentUser.getProposedWalkStatus()){
            case 0:
                break;
            case 1:
                badTime.setBackgroundColor(Color.parseColor("#FB8C00"));

                badRoute.setBackgroundColor(Color.parseColor("#2196F3"));
                accept.setBackgroundColor(Color.parseColor("#2196F3"));
                break;
            case 2:
                badRoute.setBackgroundColor(Color.parseColor("#FB8C00"));

                badTime.setBackgroundColor(Color.parseColor("#2196F3"));
                accept.setBackgroundColor(Color.parseColor("#2196F3"));
                break;
            case 3:
                accept.setBackgroundColor(Color.parseColor("#FB8C00"));

                badTime.setBackgroundColor(Color.parseColor("#2196F3"));
                badRoute.setBackgroundColor(Color.parseColor("#2196F3"));
                break;
        }
    }

    private void setProposedWalk(){
//
//        //TODO Call Titan's function
////        this.proposedWalk = ProposedWalk.getProposedWalk();
////        sortTeammatesByStatus();
//        // this.currentUser = TODO
//
//
//        Intent intent = this.getIntent();
//
//        TeamMember creator = new TeamMember("Cindy Do", "cdo@ucsd.edu", true);
//
//        //Call Yoshi's Firebase stuff here
//        ProposedWalk walk = new ProposedWalk("Grizzly Lane", "3/19/20", "2:39 PM", creator);
//        walk.setLocation("Splash Mountain");
//        List<TeamMember> teammates = TeammatesPageAdapter.retrieveTeammatesFromCloud();
//        walk.setTeammates(teammates);
//        this.proposedWalk = walk;
//        Log.d(TAG, "Intent NOT from Scheduled Walks Page");
//
//        CloudDatabase.cu
//        this.currentUser = teammates.get(3);
//        sortTeammatesByStatus();
    }

    private void sortTeammatesByStatus(){
        Collections.sort(teammates, new TeammateStatusComparator<>() );
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.googleMaps:
                launchGoogleMaps();
                break;
            case R.id.scheduleWalkButton:
                scheduleWalk();
                break;
            case R.id.withdrawWalkButton:
                withdrawWalk();
                break;
            case R.id.badTime:
                availabilityChanged(1);
                break;
            case R.id.badRoute:
                availabilityChanged(2);
                break;
            case R.id.Accept:
                availabilityChanged(3);
                break;
        }
    }

    static class TeammateStatusComparator<TeamMember> implements Comparator<com.example.walkwalkrevolution.TeamMember> {
        @Override
        public int compare(com.example.walkwalkrevolution.TeamMember teamMember, com.example.walkwalkrevolution.TeamMember t1) {
            return Integer.compare(t1.getProposedWalkStatus(), teamMember.getProposedWalkStatus());
        }
    }
}
