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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walkwalkrevolution.RecycleViewAdapters.RecyclerViewAdapterTeammates;
import com.example.walkwalkrevolution.custom_data_classes.Route;
import com.example.walkwalkrevolution.forms.AddTeammateForm;

import java.util.List;

public class TeammatesPage extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "TeammatesPage";

    private static boolean mockInviteGotten = false;

    RecyclerViewAdapterTeammates adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teammates_page);

        if(HomePage.MOCK_TESTING){
            initRecyclerView();
        }
        else {
            // once team's routes have been fetched from db
            CloudDatabase.populateTeamMateFactory(new CloudCallBack() {
                @Override
                public void callBack() {
                    initRecyclerView();
                }
            });
        }

        setUp();
    }


    @Override
    protected void onStop() {
        super.onStop();
        // Calls most updated proposed walk
        //ProposedWalkObservable.fetchProposedWalk();
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

    private void setUp(){
        ((Button) findViewById(R.id.addTeammateButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.proposedWalkButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.acceptTeamInvitation)).setOnClickListener(this);
        ((Button) findViewById(R.id.declineTeamInvitation)).setOnClickListener(this);

        renderLayoutForTeamInvitation();

    }

    private void renderLayoutForTeamInvitation(){
        if(HomePage.MOCK_TESTING){
            if(mockInviteGotten){
                ((LinearLayout) findViewById(R.id.acceptDeclineTeamInvitation)).setVisibility(View.GONE);
            }else{
                ((TextView) findViewById(R.id.inviteeMessage)).setText("Team Invitation from " + "Harrison Kim" +  "!");
                ((LinearLayout) findViewById(R.id.acceptDeclineTeamInvitation)).setVisibility(View.VISIBLE);
                mockInviteGotten = true;
            }
            return;
        }
        Intent intent = this.getIntent();
        try{
            String inviterName = intent.getExtras().getString("email");
            ((TextView) findViewById(R.id.inviteeMessage)).setText("Team Invitation from " + inviterName +  "!");
            ((LinearLayout) findViewById(R.id.acceptDeclineTeamInvitation)).setVisibility(View.VISIBLE);
            Log.d(TAG, "Invitation Notification clicked --> Accept options enabled");
        }catch(Exception e){
            Log.d(TAG, "No notification received for team invites");
            ((LinearLayout) findViewById(R.id.acceptDeclineTeamInvitation)).setVisibility(View.GONE);
        }
    }

    /**
     * Add button clicked, so redirects to AddTeamForm.
     */
    private void redirectToTeammateAddForm(){
        Log.d(TAG, "+ Clicked --> Going to AddTeammateForm Form");
        Intent intent = new Intent(TeammatesPage.this, AddTeammateForm.class);
        startActivity(intent);
    }

    /**
     * Home button clicked, so redirects to HomePage. Saves routes before switching pages.
     */
    private void redirectToProposedWalkDetailsForm(){
        Log.d(TAG, "HomeButton Clicked --> Going to HomePage");
        startActivity(new Intent(TeammatesPage.this, ScheduledWalksPage.class));
    }

    private void teamInvitationAccepted(){
        if(HomePage.MOCK_TESTING){
            TeamMember mockedMember = new TeamMember("Harrison Kim", "hkim@ucsd.edu", true);
            TeamMemberFactory.put("hkim@ucsd.edu",mockedMember );
            recreate();
            return;
        }
        String inviterEmail = this.getIntent().getExtras().getString("email");
        this.getIntent().removeExtra("email");
        SharedPreferences sharedPreferences = getSharedPreferences(CurrentUserLocalStorage.SHARED_PREFS_CURRENT_USER_INFO, MODE_PRIVATE);
        CloudDatabase.populateUserDetails(CurrentUserLocalStorage.getCurrentUserEmail(sharedPreferences), CurrentUserLocalStorage.getCurrentUserEmail(sharedPreferences), new CloudCallBack() {
            @Override
                public void callBack() {
                    CloudDatabase.acceptInvite(inviterEmail);
                    recreate();
                }
        });
        Toast.makeText(this, "Your teams have merged!", Toast.LENGTH_SHORT).show();

    }

    private void teamInvitationDeclined(){
        if(HomePage.MOCK_TESTING){
            recreate();
            return;
        }
        String inviterEmail = this.getIntent().getExtras().getString("email");
        this.getIntent().removeExtra("email");
        SharedPreferences sharedPreferences = getSharedPreferences(CurrentUserLocalStorage.SHARED_PREFS_CURRENT_USER_INFO, MODE_PRIVATE);
        CloudDatabase.populateUserDetails(CurrentUserLocalStorage.getCurrentUserEmail(sharedPreferences), CurrentUserLocalStorage.getCurrentUserEmail(sharedPreferences), new CloudCallBack() {
            @Override
            public void callBack() {
                CloudDatabase.declineInvite(inviterEmail);
                recreate();
            }
        });
        Toast.makeText(this, "Merge declined", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.addTeammateButton:
                redirectToTeammateAddForm();
                break;
            case R.id.proposedWalkButton:
                redirectToProposedWalkDetailsForm();
                break;
            case R.id.acceptTeamInvitation:
                teamInvitationAccepted();
                break;
            case R.id.declineTeamInvitation:
                teamInvitationDeclined();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TeammatesPage.this, HomePage.class));
    }
}
