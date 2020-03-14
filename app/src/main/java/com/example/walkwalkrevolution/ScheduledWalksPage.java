package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.walkwalkrevolution.RecycleViewAdapters.RecyclerViewAdapterScheduledWalks;
import com.example.walkwalkrevolution.RecycleViewAdapters.RecyclerViewAdapterTeammates;
import com.example.walkwalkrevolution.custom_data_classes.ProposedWalk;
import com.example.walkwalkrevolution.proposed_walk_observer_pattern.ProposedWalkObservable;

import java.util.ArrayList;
import java.util.List;

public class ScheduledWalksPage extends AppCompatActivity {

    private static final String TAG = "ScheduledWalksPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "INSIDE SCHEDULED WALKS");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_walks_page);

        if(HomePage.MOCK_TESTING){
            setUp();
        }else{
            ProposedWalkObservable.fetchProposedWalk(new CloudCallBack() {
                @Override
                public void callBack() {
                    setUp();
                    Log.d(TAG, "Called setup");
                }
            });
        }

        Button goToHomePage = (Button) findViewById(R.id.goToHomePage2);

        goToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToHomePage();
            }
        });
        //setUp();
    }

    private void setUp(){
        //Proposed walk = ProposedWalk.getProposedWalk(); TODO
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.proposedWalk);
        TextView header = (TextView)  findViewById(R.id.proposedWalkHeaderText);
        ProposedWalk walk = getProposedWalk();
        //ProposedWalk walk = new ProposedWalk("Grizzly Road", "04/17/2000", "9:00PM", new TeamMember("Amrit Singh", "aksingh@ucsd.edu", false));

        Log.d(TAG, "Walk is NULL: " + ((walk == null) ? true:false));
        if(walk == null){
            layout.setVisibility(View.INVISIBLE);
            return;
        }
        renderWalkOnScreen(walk);

        if(walk.getIsScheduled()){
            layout.getBackground().setColorFilter(Color.parseColor("#FFC400"), PorterDuff.Mode.MULTIPLY);
            header.setText("Scheduled Walks");
        }
        else{
            layout.getBackground().setColorFilter(Color.parseColor("#E0E0E0"), PorterDuff.Mode.MULTIPLY);
        }

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchProposedWalkDetailsPage();
            }
        });
    }

    private ProposedWalk getProposedWalk(){
        if(HomePage.MOCK_TESTING){
            return TeamMemberFactory.getProposedWalk();
        }
        return ProposedWalkObservable.getProposedWalk();
    }

    private void renderWalkOnScreen(ProposedWalk walk){
        ((TextView) findViewById(R.id.proposedWalkName)).setText(walk.getName());
        Button icon = (Button)  findViewById(R.id.teammateIcon);
        icon.setText(walk.getCreator().getInitials());
        icon.getBackground().setColorFilter(walk.getCreator().getColorVal(), PorterDuff.Mode.MULTIPLY);
    }

    private void launchProposedWalkDetailsPage(){
        startActivity(new Intent(ScheduledWalksPage.this, ProposedWalkDetailsPage.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ScheduledWalksPage.this, TeammatesPage.class));
    }

    /**
     * Home button clicked, so redirects to HomePage. Saves routes before switching pages.
     */
    private void redirectToHomePage(){
        Log.d(TAG, "HomeButton Clicked --> Going to HomePage");
        startActivity(new Intent(ScheduledWalksPage.this, HomePage.class));
        finish();
    }
}
