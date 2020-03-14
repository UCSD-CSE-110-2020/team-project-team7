package com.example.walkwalkrevolution;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.example.walkwalkrevolution.custom_data_classes.ProposedWalk;
import com.example.walkwalkrevolution.custom_data_classes.ProposedWalkJsonConverter;
import com.example.walkwalkrevolution.custom_data_classes.Route;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class CloudDatabase {
    public static final String TAG = "CLOUD";
    public static final String USERS = "Users";
    public static final String TEAMS = "Teams";
    public static final String TOPIC = "topic";
    public static final String MEMBERS = "Members";

    public static FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
    public static CollectionReference users = dataBase.collection(USERS);
    public static CollectionReference teams = dataBase.collection(TEAMS);
    public static CollectionReference topic = dataBase.collection(TOPIC);
    public static UserDetails currentUser;
    public static TeamMember currentUserMember;

    /**
     * MAKE MOCKFIRESTOREDATABASE SINGLETON CLASS
     */
    private static CloudDatabase single_instance = null;

    private CloudDatabase() {}

    public static CloudDatabase getInstance() {
        if(single_instance == null) {
            single_instance = new CloudDatabase();
        }
        return single_instance;
    }

    // TODO [START] (POPULATE STATIC CLASSES) ------------------------------------------------------
    /**
     * !!! CALL ON CREATE OF HOMEPAGE !!! (FOR YOSHI/CALVIN)
     * CHECK IF CURRENT USER EXISTS IN DATABASE
     * -> IN BOTH CASES CREATE USERDETAILS OBJECT REPRESENTING CURRENT USER AND STORE IN
     *    USERDETAILSFACTORY
     */
    public static void populateUserDetails(String currentUserEmail, String currentUserName, CloudCallBack cb) {

        // try to find their document in the database
        users.document(currentUserEmail).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()) {
                                Log.d(TAG, "user exists");

                                // create userDetails object and put into factory for fast local access
                                currentUser = document.toObject(UserDetails.class);
                                currentUserMember = new TeamMember(currentUser.getName(), currentUser.getEmail(), true);
                                cb.callBack();
                            } else {
                                Log.d(TAG, "user doesn't exist");

                                // if the current user does not exist ...
                                //   create a userDetails obj and add to database
                                addUserToFireStore(currentUserEmail, currentUserName);
                            }
                        } else {
                            Log.d(TAG, "task failed in checkUserExists: ", task.getException());
                        }
                    }
                });
    }

    /**
     * !!! CALL ONSTART OF ROUTES PAGE !!! (FOR AMRIT)
     * Update UserDetail's routes by grabbing from firestore on activity start
     */
    public static void populateUserRoutes(CloudCallBack cb) {

        // Retrieve the routes field value of current User from Database
        users.document(currentUser.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot snapshot = task.getResult();
                        if(task.isSuccessful()) {
                            if(snapshot != null) {
                                String routesJSON = (String)snapshot.get("routes");
                                currentUser.setRoutes(routesJSON);
                            } else {
                                Log.d(TAG, "routes was null");
                            }
                            cb.callBack();
                        } else {
                            Log.d(TAG, "Error getting routeslist on load");
                        }
                    }
                });
    }

    /**
     * !!! TO ACTUALLY GET THE TEAMROUTES LIST YOU NEED TO CALL TEAMMEMBERFACTORY.GETTEAMROUTES()
     * !!! CALL THIS IN ONSTART OF TEAM ROUTES PAGE !!! (FOR AMRIT)
     * TeamMemberFactory will then have a list of pairs of everyone's routes
     */
    public static void populateTeamRoutes(CloudCallBack cb) {
        if(!currentUser.getTeam().equals("")) {
            TeamMemberFactory.resetRoutes();

            users.whereEqualTo("team", currentUser.getTeam()).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                try {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<List<Route>>() {
                                    }.getType();
                                    for (QueryDocumentSnapshot members : Objects.requireNonNull(task.getResult())) {
                                        String memberEmail = (String) members.getData().get("email");
                                        String routesJSON = (String) members.getData().get("routes");
                                        if (!routesJSON.equals("") && !memberEmail.equals(currentUser.getEmail())) {
                                            List<Route> membersRoutes = gson.fromJson(routesJSON, type);
                                            for (Route route : membersRoutes) {
                                                TeamMemberFactory.addRoute(new Pair<>(memberEmail, route));
                                            }
                                        }
                                    }
                                    cb.callBack();
                                } catch (Exception e) {
                                    Log.d(TAG, "failed to create teamroutes in populateTeamRoutesOnStart " +
                                            "with exception: ", e);
                                }
                            } else {
                                Log.d(TAG, "failed to get snapshot of users where team " +
                                        "is equal to teamID in populateTeamRoutesOnStart method");
                            }
                        }
                    });
        } else {
            Log.d(TAG, "no team routes to populate, user is not on any team");
        }
    }

    /**
     * !!! THIS SHOULD BE CALLED ONCREATE OF TEAMS PAGE !!!
     * !!! PROPOSED WALK IS SENT TO TEAMMEMBERFACTORY,
     *     TO GET, CALL TEAMMEMBERFACTORY.GETPROPOSEDWALK() !!! (FOR TITAN)
     * Retrieving the proposed walk from database
     */
    public static void populateTeamProposedWalk(CloudCallBack cb) {
        if(currentUser.getTeam().equals("")) {
            Log.d(TAG, "user has no proposed walk because they are not on a team!");
        } else {
            teams.document(currentUser.getTeam()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot snapshot = task.getResult();
                            if (task.isSuccessful()) {
                                if (snapshot.exists()) {
                                    if (snapshot != null) {
                                        String proposedWalkJSON = (String) snapshot.get("current proposed walk");
                                        if (!proposedWalkJSON.equals("")) {
                                            ProposedWalk pw = ProposedWalkJsonConverter.convertJsonToWalk(proposedWalkJSON);
                                            TeamMemberFactory.setProposedWalk(pw);
                                        } else {
                                            Log.d(TAG, "team has no proposed walk");
                                            TeamMemberFactory.setProposedWalk(null);
                                        }
                                    } else {
                                        Log.d(TAG, "failed to get snapshot of the current proposed walk");
                                    }
                                } else {
                                    Log.d(TAG, "current user's team does not have a proposed walk");
                                }
                            } else {
                                Log.d(TAG, "failed to get snapshot of teamID " +
                                        "document in getProposedWalk method");
                            }
                        }
                    });
        }

        cb.callBack();
        Log.d(TAG, "Finished calling CloudDatabase.populateTeamProposedWalk()");
    }

    /**
     * !!! HELPER METHOD FOR ABOVE FUNCTION !!!
     * populate Map of TeamMates every time we go into TeamPage
     */
    public static void populateTeamMateFactory(CloudCallBack cb) {

        TeamMemberFactory.resetMembers();
        if(!currentUser.getTeam().equals("")) {
            teams.document(currentUser.getTeam()).collection(MEMBERS).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                try {
                                    for (QueryDocumentSnapshot member : Objects.requireNonNull(task.getResult())) {
                                        String memberEmail = (String) member.get("email");
                                        if (memberEmail != null && !(memberEmail).equals(currentUser.getEmail())) {
                                            TeamMember memberObj = member.toObject(TeamMember.class);
                                            TeamMemberFactory.put(memberEmail, memberObj);
                                        }
                                        else if(memberEmail.equals(currentUser.getEmail())) {
                                            currentUserMember = member.toObject(TeamMember.class);
                                        }
                                    }
                                    cb.callBack();
                                } catch (Exception e) {
                                    Log.d(TAG, "failed to get create teammembers in populateTeamMateFactory" +
                                            " with exception: ", e);
                                }
                            } else {
                                Log.d(TAG, "failed to get snapshot of MEMBERS " +
                                        "collection in populateTeamMateFactory");
                            }
                        }
                    });
        } else {
            Log.d(TAG, "current user is not on any team");
        }
    }
    // TODO [END] (POPULATE STATIC CLASSES) --------------------------------------------------------


    // TODO [START] (STORE INFO TO CLOUD) ----------------------------------------------------------
    /**
     * !!! PUT THIS IN TREEMANIPULATION.SAVETREE !!! (FOR AMRIT)
     * Add/update routes to user document on FireStore
     */
    public static void storeUserRoutes(String routesToStore) {

        // create map to add/update user document with key value pair
        Map<String, String> routes = new HashMap<>();
        routes.put("routes", routesToStore);

        try {
            users.document(currentUser.getEmail()).set(routes, SetOptions.merge());
        } catch (Exception e) {
            Log.d(TAG, "failed to store routes: ", e);
        }
    }

    /**
     * WHEN CURRENT USER WALKS A TEAMMATE'S ROUTE STORE INTO TEAMROUTEWALKED FIELD IN CLOUD
     */
    public static void storeUserTeamRoutesWalked(String routesToStore) {

        // create map to add/update user document with key value pair
        Map<String, String> teamRoutesWalked = new HashMap<>();
        teamRoutesWalked.put("teamRoutesWalked", routesToStore);
        currentUser.setTeamRoutesWalked(routesToStore);
        try {
            users.document(currentUser.getEmail()).set(teamRoutesWalked, SetOptions.merge());
        } catch (Exception e) {
            Log.d(TAG, "failed to store teamRoutesWalked: ", e);
        }
    }

    /**
     * !!! PUT IN SENDPROPOSEDWALK.SENDPROPOSEDWALK() !!! (FOR TITAN)
     * Store the proposed walk into the cloud teams document and send out notification
     */
    public static void storeTeamProposedWalk(ProposedWalk proposedWalk) {

        Map<String, String> newWalkDetails = new HashMap<>();
        Map<String, String> notify = new HashMap<>();
        if(proposedWalk == null) {
            newWalkDetails.put("current proposed walk", "");
            Log.d("notification for withdraw","withdraw");
            notify.put("notify", "withdrawn");

        } else {
            String proposedWalkJSON = ProposedWalkJsonConverter.convertWalkToJson(proposedWalk);
            newWalkDetails.put("current proposed walk", proposedWalkJSON);
            if(proposedWalk.getIsScheduled()) {
                Log.d("notification for schedule", "scheduled");
                notify.put("notify", "scheduled");
            } else {
                Log.d("notification for proposedWalk made", "proposed");
                notify.put("notify", "proposed");
            }
        }
        teams.document(currentUser.getTeam()).set(newWalkDetails, SetOptions.merge());

        // TODO TRIGGER CLOUD FUNCTION TO NOTIFY ALL TEAMMEMBERS
        topic.document("topic2")
                .collection("messages2")
                .document("messages2").set(notify, SetOptions.merge());
    }

    /**
     * Store all teamMembers back into database
     */
    public static void storeTeam() {

        Map<String, TeamMember> members = TeamMemberFactory.getAllMembers();

        for(Map.Entry<String, TeamMember> member : members.entrySet()) {
            teams.document(currentUser.getTeam())
                    .collection(MEMBERS).document(member.getValue().getEmail()).set(member.getValue());
        }
        updateCurrentUserInTeam();
    }

    /**
     * update specific teamMember in database
     */
    public static void updateCurrentUserInTeam() {
        teams.document(currentUser.getTeam()).collection(MEMBERS).document(currentUser.getEmail()).set(currentUserMember);
        Map<String, Integer> notif = new HashMap<>();
        notif.put("notif", currentUserMember.getProposedWalkStatus());
        topic.document("topic")
                .collection("messages")
                .document("messages").set(notif, SetOptions.merge());
    }

    // TODO [END] (STORE INFO TO CLOUD) ------------------------------------------------------------


    // TODO [START] (GET LIST OF USER'S PERSONAL/TEAM ROUTES) --------------------------------------
    /**
     * !!! PUT THIS IN TREEMANIPULATION.LOADTREE !!! (FOR AMRIT)
     * Get updated routes from UserDetails as it should always be updated
     */
    public static List<Route> getUserRoutes() {

        // convert routes represented as string to list of routes
        Gson gson = new Gson();
        Type type = new TypeToken<List<Route>>() {}.getType();
        if(currentUser.getRoutes().equals("")) return new ArrayList<>();
        else return gson.fromJson(currentUser.getRoutes(), type);
    }

    /**
     * GET THE CURRENT USER'S ROUTES THAT WERE TAKEN FROM TEAM ROUTES PAGE
     */
    public static List<Route> getUserTeamRoutesWalked() {

        // convert teamrouteswalked represented as string to list of routes
        Gson gson = new Gson();
        Type type = new TypeToken<List<Route>>() {}.getType();
        if(currentUser.getTeamRoutesWalked().equals("null")) {
            Log.d(TAG, "NULL " + currentUser.getTeamRoutesWalked());
            return new ArrayList<>();
        } else {
            Log.d(TAG, currentUser.getTeamRoutesWalked());
            return gson.fromJson(currentUser.getTeamRoutesWalked(), type);
        }
    }
    // TODO [END] (GET LIST OF USER'S PERSONAL/TEAM ROUTES) ----------------------------------------


    // TODO [START] NOTIFICATIONS ------------------------------------------------------------------
    /**
     * WHEN USER INVITES SOMEONE TO THEIR TEAM (FOR HARRISON)
     */
    public static void inviteToTeam(String mock_teammate_email) {
        Log.d("AHHHH", mock_teammate_email);
        users.document(mock_teammate_email).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot snapshot = task.getResult();
                        if(task.isSuccessful()) {
                            if (snapshot.exists()) {
                                // if inviter isn't in a team create one
                                if (snapshot != null) {
                                    Log.d(TAG, snapshot.toString());

                                    if (currentUser.getTeam().equals("")) {
                                        Map<String, String> updateTeam = new HashMap<>();
                                        DocumentReference newTeamRef = teams.document();
                                        newTeamRef.collection(MEMBERS).document(currentUser.getEmail())
                                                .set(new TeamMember(currentUser.getName(),
                                                        currentUser.getEmail(),
                                                        true));
                                        currentUser.setTeam(newTeamRef.getId());
                                        updateTeam.put("team", newTeamRef.getId());
                                        users.document(currentUser.getEmail()).set(updateTeam, SetOptions.merge());
                                    }

                                    // get invitee's info to create TeamMember object to store in new team
                                    UserDetails pendingTeamMate = new UserDetails();
                                    pendingTeamMate = snapshot.toObject(UserDetails.class);
                                    TeamMember pendingMember = new TeamMember(pendingTeamMate.getName(),
                                            pendingTeamMate.getEmail(),
                                            false);
                                    teams.document(currentUser.getTeam())
                                            .collection(MEMBERS)
                                            .document(mock_teammate_email).set(pendingMember);

                                    Map<String, String> updateTopic = new HashMap<>();
                                    updateTopic.put("name", currentUser.getName());
                                    updateTopic.put("email", currentUser.getEmail());
                                    topic.document("topic")
                                            .collection("messages")
                                            .document("messages")
                                            .set(updateTopic, SetOptions.merge());

                                } else {
                                    Log.d(TAG, "failed to create team for inviter in " +
                                            "inviteToTeam bc snapshot is null");
                                }
                                // TODO SEND NOTIFICATION TO INVITEE ALONG WITH INVITER EMAIL

                            } else {
                                Log.d(TAG, "getting user doc failed in inviteToTeamMethod");
                            }
                        } else {
                            Log.d(TAG, "user doesn't exist");
                        }
                    }
                });
    }

    public static void acceptInvite(String inviterEmail) {
        Log.d("OOOOOOWWW", inviterEmail);
        users.document(inviterEmail)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(task.isSuccessful()) {
                    if(snapshot != null) {
                        UserDetails inviter = snapshot.toObject(UserDetails.class);
                        teamCreationOnAccept(inviter);
                    } else {
                        Log.d(TAG, "snapshot was empty for inviter details");
                    }
                } else {
                    Log.d(TAG, "task failed when retrieving inviter details");
                }
            }
        });
    }

    /**
     * IF INVITEE ACCEPTS INVITE (FOR HARRISON)
     */
    private static void teamCreationOnAccept(UserDetails inviter) {

        Map<String, String> updateTeam = new HashMap<>();
        updateTeam.put("team", inviter.getTeam());
        Map<String, Boolean> updateStatus = new HashMap<>();
        updateStatus.put("pendingStatus", true);

        // if current user is already on a team, change everyone on your team's team to new team
        if(!currentUser.getTeam().equals("")) {
            Log.d(TAG, "Accepted member does have a team");

            teams.document(currentUser.getTeam()).collection(MEMBERS)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            String newMembersName = (String)document.get("name");
                            String newMembersEmail = (String)document.get("email");
                            if(!newMembersEmail.equals(currentUser.getEmail())) {
                                users.document(newMembersEmail).set(updateTeam, SetOptions.merge());
                                teams.document(inviter.getTeam()).collection(MEMBERS).document(newMembersEmail)
                                        .set(new TeamMember(newMembersName, newMembersEmail, true));
                            }
                        }
                    } else {
                        Log.d(TAG, "Getting snapshot of MEMBERS collection failed in teamCreationAccept method");
                    }
                }
            });
            teams.document(currentUser.getTeam()).delete();
        }
        users.document(currentUser.getEmail()).set(updateTeam, SetOptions.merge());
        teams.document(inviter.getTeam()).collection(MEMBERS).document(currentUser.getEmail()).set(updateStatus, SetOptions.merge());
        currentUser.setTeam(inviter.getTeam());
        //users.document(invitee.getEmail()).set(updateStatus, SetOptions.merge());
    }

    public static void declineInvite(String inviterEmail) {

        users.document(inviterEmail)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(task.isSuccessful()) {
                    if(snapshot != null) {
                        UserDetails inviter = snapshot.toObject(UserDetails.class);
                        teamCreationOnDecline(inviter);
                    } else {
                        Log.d(TAG, "snapshot is null for inviter ");
                    }
                } else {
                    Log.d(TAG, "task failed in declineInvite");
                }
            }
        });
    }

    /**
     * IF INVITEE DECLINES INVITE (FOR HARRISON)
     */
    public static void teamCreationOnDecline(UserDetails inviter) {
        teams.document(inviter.getTeam()).collection(MEMBERS).document(currentUser.getEmail()).delete();
    }
    // TODO [END] (NOTIFICATIONS) ------------------------------------------------------------------

    /**
     * !!! HELPER METHOD FOR FUNCTION ABOVE !!!
     * Add new users to the database via google account
     * @param mock_user_email: make fake email
     */
    private static void addUserToFireStore(String mock_user_email, String name) {

        // create userDetails object and put into factory for fast local access
        currentUser = new UserDetails(name, mock_user_email);
        UserDetailsFactory.put(mock_user_email, currentUser);

        // Create a document on Firestore to store user data
        users.document(mock_user_email).set(currentUser)
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
}

