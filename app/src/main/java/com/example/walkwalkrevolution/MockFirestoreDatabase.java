package com.example.walkwalkrevolution;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.example.walkwalkrevolution.custom_data_classes.ProposedWalk;
import com.example.walkwalkrevolution.custom_data_classes.ProposedWalkJsonConverter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MockFirestoreDatabase {

    public static final String TAG = "MOCK_FIRESTORE_DATABASE";
    public static final String USERS = "Users";
    public static final String TEAMS = "Teams";
    public static final String MEMBERS = "Members";

    public static FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
    public static CollectionReference users = dataBase.collection(USERS);
    public static CollectionReference teams = dataBase.collection(TEAMS);


    /**
     * MAKE MOCKFIRESTOREDATABASE SINGLETON CLASS
     */
    private static MockFirestoreDatabase single_instance = null;

    private MockFirestoreDatabase() {}

    public static MockFirestoreDatabase getInstance() {
        if(single_instance == null) {
            single_instance = new MockFirestoreDatabase();
        }
        return single_instance;
    }

    /**
     * Check to see if our current user is in the database
     * @param mock_user_id: the user's google auth ID
     * @param mock_user_email: the user's google auth Email
     * @param name: the user's name specified in the heights form
     */
    public static void checkUserExists(String mock_user_id, String mock_user_email, String name) {

        // try to find their document in the database
        users.document(mock_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Log.d(TAG, "user exists");

                        // populate map with user document data
                        Map<String, Object> data = document.getData();

                        // create a teamMember object of user and put into factory
                        TeamMemberFactory.put(mock_user_id,
                                new TeamMember(data.get("name").toString(),
                                               data.get("email").toString(),
                                               data.get("userID").toString(),
                                               data.get("team").toString(),
                                               (Boolean)data.get("teamStatus")));
                    } else {
                        Log.d(TAG, "user doesnt exist");
                        // if the current user does not exist create a teamMember obj and add to database
                        addUser(mock_user_id, mock_user_email, name);
                    }
                } else {
                    Log.d(TAG, "failed with: ", task.getException());
                }
            }
        });
    }

    /**
     * Add new users to the database via google account
     * @param mock_user_id: create own document of user
     * @param mock_user_email: make fake email
     */
    private static void addUser(String mock_user_id, String mock_user_email, String name) {

        // create teammember obj of user and put them in factory that will hold teammembers
        TeamMember user = new TeamMember(name, mock_user_email, mock_user_id, "", false);
        TeamMemberFactory.put(mock_user_id, user);

        Map<String, Object> user_info = new HashMap<>();
        user_info.put("userID", user.getUserID());
        user_info.put("name", user.getName());
        user_info.put("initials", user.getInitials());
        user_info.put("colorVal", user.getColorVal());
        user_info.put("email", user.getEmail());
        user_info.put("team", user.getTeam());
        user_info.put("teamStatus", user.getTeamStatus());

        // Create a document on Firestore to store user data
        users.document(mock_user_id).set(user_info)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // some Toast message
                            Log.d(TAG, "add to database was successful");
                        } else {
                            // some Toast message
                            Log.d(TAG, "add to database failed");
                        }
                    }
                });
    }

    /**
     * Add routes to user document
     */
    public static void storeRoutes(String routesToStore, TeamMember mock_user_one) {
        Map<String, String> routes = new HashMap<>();
        routes.put("routes", routesToStore);
        try {
            users.document(mock_user_one.getUserID()).set(routes, SetOptions.merge());
        } catch (Exception e) {
            Log.d(TAG, "failed to store routes: ", e);
        }
    }

    /**
     * When someone accepts your invite, acquire new teammate's data from database
     *   then create a team accordingly
     * @param mock_user_one
     * @param mock_teammate_ID
     */
    public static void addTeam(String mock_user_one, String mock_teammate_ID) {

        users.document(mock_teammate_ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(task.isSuccessful()) {
                    Log.d(TAG, "Teammates document successfully found");
                    if(snapshot.getData() != null) {
                        Map<String, Object> data = snapshot.getData();
                        String teammate_name = data.get("name").toString();
                        String teammate_email = data.get("email").toString();
                        String teammate_userID = data.get("userID").toString();
                        String teammate_teamID = data.get("team").toString();
                        boolean teammate_teamStatus = (Boolean)data.get("teamStatus");
                        TeamMember newTeammate = new TeamMember(teammate_name, teammate_email, teammate_userID, teammate_teamID, teammate_teamStatus);
                        TeamMemberFactory.put(mock_teammate_ID, newTeammate);
                        teamCreation(TeamMemberFactory.get(mock_user_one), TeamMemberFactory.get(mock_teammate_ID));
                    }
                } else {
                    Log.d(TAG, "Teammates document not found");
                    // TODO MAYBE SOME TOAST MESSAGE SAYING THEY COULDNT FIND THE USER SPECIFIED
                }
            }
        });
    }

    /**
     * THREE CASES FOR TEAM CREATION
     * CASE 1: BOTH INVITER AND INVITEE ARE NOT IN A TEAM -> CREATE TEAM
     * CASE 2: ONE OF THE TWO IS IN A GROUP -> ONE JOINS THE OTHERS TEAM
     * CASE 3: BOTH ARE IN A GROUP -> THE TEAMS MERGE
     */
    private static void teamCreation(TeamMember mock_user_one, TeamMember mock_user_two) {

        List<TeamMember> list = new ArrayList<>();
        list.add(mock_user_one);
        list.add(mock_user_two);

        // if neither are in a team -> create new team
        if(!mock_user_one.getTeamStatus() && !mock_user_two.getTeamStatus()) {
            Log.d(TAG, "Inside neither have a team");
            // create new team doc in TEAMS
            Map<String, String> teamMember = new HashMap<>();
            DocumentReference newTeamRef = teams.document();

            // put both users into new team and update user's team field
            for(TeamMember member : list) {
                teamMember.put("user", member.getUserID());
                newTeamRef.collection(MEMBERS).add(teamMember);
                member.setTeam(newTeamRef.getId());
                users.document(member.getUserID()).update("team", newTeamRef.getId());
            }
        }
        // if both are in a team -> merge both teams
        else if (mock_user_one.getTeamStatus() && mock_user_two.getTeamStatus()) {
            Log.d(TAG, "Inside both have a team");
            // create new team doc in TEAMS
            DocumentReference newTeamRef = teams.document();
            Log.d(TAG, "Team " + newTeamRef.getId() + " made");

            for(TeamMember member : list) {
                teams.document(member.getTeam()).collection(MEMBERS)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            Map<String, String> updateTeam = new HashMap<>();
                            updateTeam.put("team", newTeamRef.getId());
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                users.document((String)document.getData().get("user"))
                                        .set(updateTeam, SetOptions.merge());
                            }
                        }
                    }
                });
                teams.document(member.getTeam()).delete();
            }

        }
        // one of them are in a team -> merge the one who isn't with the one who is
        else {
            Log.d(TAG, "Inside of one of them has a team");
            Map<String, String> teamMember = new HashMap<>();
            TeamMember oneWithTeam = mock_user_one.getTeamStatus() ? mock_user_one : mock_user_two;
            TeamMember oneWithoutTeam = mock_user_one.getTeamStatus() ? mock_user_two : mock_user_one;

            // include one without team as a member to existing team
            teamMember.put("user", oneWithoutTeam.getUserID());
            DocumentReference teamRef = teams.document(oneWithTeam.getTeam());
            teamRef.collection(MEMBERS).add(teamMember);

            // update new teammember's team field
            Map<String, String> updateTeam = new HashMap<>();
            oneWithoutTeam.setTeam(teamRef.getId());
            updateTeam.put("team", teamRef.getId());
            users.document(oneWithoutTeam.getUserID()).set(updateTeam, SetOptions.merge());
        }
    }

    // TODO GET USERS ROUTES
    public static String getUserRoutes(TeamMember mock_user) {
        final String[] userRoutesJSON = new String[1];
        users.document(mock_user.getUserID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(task.isSuccessful()) {
                    try {
                        userRoutesJSON[0] = snapshot.getData().get("routes").toString();
                    } catch (NullPointerException e) {
                        Log.d(TAG, "No routes");
                        userRoutesJSON[0] = "";
                    }
                }
            }
        });
        return userRoutesJSON[0];
    }

    // TODO GET TEAMS ROUTES


    // TODO ONCE SOMEONE PROPOSES A WALK STORE SCHEDULED WALK TO FIRESTORE
    public static void storeProposedWalk(ProposedWalk proposedWalk, TeamMember creator) {
        String proposedWalkJSON = ProposedWalkJsonConverter.convertWalkToJson(proposedWalk);
        Map<String, String> newWalkDetails = new HashMap<>();
        newWalkDetails.put("current proposed walk", proposedWalkJSON);
        teams.document(creator.getTeam()).set(newWalkDetails, SetOptions.merge());
        // TODO TRIGGER CLOUD FUNCTION TO NOTIFY ALL TEAMMEMBERS
    }

    // TODO WHEN USER GOES TO THE SCHEDULE WALKS PAGE GET WALKS FROM FIRESTORE
    public static ProposedWalk getProposedWalk(TeamMember mock_current_user) {
        final ProposedWalk[] proposedWalk = new ProposedWalk[1];
        teams.document(mock_current_user.getTeam()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(task.isSuccessful()) {
                    if(snapshot != null) {
                        try {
                            String proposedWalkJSON = snapshot.getData().get("current proposed walk").toString();
                            proposedWalk[0] = ProposedWalkJsonConverter.convertJsonToWalk(proposedWalkJSON);
                        } catch (NullPointerException e) {
                            Log.d(TAG, "uhhh theres no proposed walk watchutalknbout");
                            proposedWalk[0] = null;
                        }
                    } else {
                        Log.d(TAG, "team document is empty for some reason..");
                        proposedWalk[0] = null;
                    }
                }
            }
        });
        return proposedWalk[0];
    }



}