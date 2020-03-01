package com.example.walkwalkrevolution;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

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

    public final String USERS = "Users";
    public final String TEAMS = "Teams";
    public final String MEMBERS = "Members";

    public FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
    public CollectionReference users = dataBase.collection(USERS);
    public CollectionReference teams = dataBase.collection(TEAMS);


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
     *      add them if they arent
     *      ignore if they are
     * @param mock_user_id: the user's name
     * @param mock_user_email: the user's email
     */
    public void checkUserExists(String mock_user_id, String mock_user_email, String name) {
        users.document(mock_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Log.d("DB", "user exists");

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
                        Log.d("DB", "user doesnt exist");
                        addUser(mock_user_id, mock_user_email, name);
                    }
                } else {
                    Log.d("DB", "failed with: ", task.getException());
                }
            }
        });
    }

    /**
     * Add new users to the database via google account
     * @param mock_user_id: create own document of user
     * @param mock_user_email: make fake email
     */
    private void addUser(String mock_user_id, String mock_user_email, String name) {

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
                            Log.d("DB", "add to database was successful");
                        } else {
                            // some Toast message
                            Log.d("DB", "add to database failed");
                        }
                    }
                });
    }

    /**
     * Add routes to user document
     */
    public void storeRoutes(String routesToStore, TeamMember mock_user_one) {
        Map<String, String> routes = new HashMap<>();
        routes.put("routes", routesToStore);
        try {
            users.document(mock_user_one.getUserID()).set(routes, SetOptions.merge());
        } catch (Exception e) {
            Log.d("DB", "failed to store routes: ", e);
        }
    }

    public void getNewTeamMemberData(String mock_user_one, String mock_teammate_ID) {
        users.document(mock_teammate_ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(task.isSuccessful()) {
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
    public void teamCreation(TeamMember mock_user_one, TeamMember mock_user_two) {

        List<TeamMember> list = new ArrayList<>();
        list.add(mock_user_one);
        list.add(mock_user_two);

        // set user_one and user_two team status and team names
//        for(TeamMember members : list) {
//            users.document(members.getUserID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    DocumentSnapshot snapshot = task.getResult();
//                    if(task.isSuccessful()) {
//                        if(snapshot.getData() != null) {
//                            Map<String, Object> data = snapshot.getData();
//                            if(data.get("team") != "") {
//                                Log.d("DB", "inside team != null");
//                                user_triplet.setTeamID((String)data.get("team"));
//                                user_triplet.setTeamStatus(true);
//                            }
//                        }
//                    }
//                }
//            });
//        }


        // if neither are in a team
        if(!mock_user_one.getTeamStatus() && !mock_user_two.getTeamStatus()) {
            Log.d("DB", "Inside neither have a team");
            // create new team doc in TEAMS
            Map<String, String> teamMember = new HashMap<>();
            DocumentReference newTeamRef = teams.document();

            // put both users into new team and update user's team field
            for(TeamMember member : list) {
                teamMember.put("user", member.getUserID());
                newTeamRef.collection(MEMBERS).add(teamMember);
                users.document(member.getUserID()).update("team", newTeamRef.getId());
            }
        }
        else if (mock_user_one.getTeamStatus() && mock_user_two.getTeamStatus()) {
            Log.d("DB", "Inside both have a team");
            // create new team doc in TEAMS
            DocumentReference newTeamRef = teams.document();
            Log.d("DB", "Team " + newTeamRef.getId() + " made");

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

//            // merge both teams to a new team
//            teams.document(userOneTeam).collection(MEMBERS)
//                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if(task.isSuccessful()) {
//                        Map<String, String> updateTeam = new HashMap<>();
//                        for(QueryDocumentSnapshot document : task.getResult()) {
//                            updateTeam.put("team", newTeamRef.getId());
//                            users.document((String)document
//                                    .getData()
//                                    .get("user"))
//                                    .set(updateTeam, SetOptions.merge());
//                        }
//                    }
//                }
//            });
//
//            teams.document(userTwoTeam).collection(MEMBERS)
//                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if(task.isSuccessful()) {
//                        Map<String, String> updateTeam = new HashMap<>();
//                        for(QueryDocumentSnapshot document : task.getResult()) {
//                            updateTeam.put("team", newTeamRef.getId());
//                            users.document((String)document
//                                    .getData()
//                                    .get("user"))
//                                    .set(updateTeam, SetOptions.merge());
//                        }
//                    }
//                }
//            });
//
//            teams.document(userOneTeam).delete();
//            teams.document(userTwoTeam).delete();

        }
        else {
            Log.d("DB", "Inside of them has a team");
            Map<String, String> teamMember = new HashMap<>();
            if (mock_user_one.getTeamStatus()) {
                teamMember.put("user", mock_user_two.getUserID());
                DocumentReference teamRef = teams.document(mock_user_one.getTeam());
                teamRef.collection(MEMBERS).add(teamMember);
                Map<String, String> updateTeam = new HashMap<>();
                updateTeam.put("team", teamRef.getId());
                users.document(mock_user_two.getUserID()).set(updateTeam, SetOptions.merge());
            } else {
                teamMember.put("user", mock_user_one.getUserID());
                DocumentReference teamRef = teams.document(mock_user_two.getTeam());
                teamRef.collection(MEMBERS).add(teamMember);
                Map<String, String> updateTeam = new HashMap<>();
                updateTeam.put("team", teamRef.getId());
                users.document(mock_user_one.getUserID()).set(updateTeam, SetOptions.merge());
            }
        }
    }
}
