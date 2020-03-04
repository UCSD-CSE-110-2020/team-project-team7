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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // TODO [START] (HOMEPAGE ON CREATE) -------------------------------------------------------------------

    /**
     * CHECK IF CURRENT USER EXISTS IN DATABASE -> IN BOTH CASES CREATE GLOBAL USERDETAILS OBJECT OF THEM
     * @param currentUserEmail
     * @param currentUserName
     */
    public static void homePageOnCreateFireStore(String currentUserEmail, String currentUserName) {
        checkUserExists(currentUserEmail, currentUserName);
    }

    /**
     * Check to see if our current user is in the database
     * @param mock_user_email: the user's google auth Email
     * @param name: the user's name specified in the heights form
     */
    private static void checkUserExists(String mock_user_email, String name) {

        // try to find their document in the database
        users.document(mock_user_email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Log.d(TAG, "user exists");

                        // create userDetails object and put into factory for fast local access
                        UserDetails currentUser = document.toObject(UserDetails.class);
                        UserDetailsFactory.put(mock_user_email, currentUser);
                    } else {
                        Log.d(TAG, "user doesnt exist");

                        // if the current user does not exist create a userDetails obj and add to database
                        addUserToFireStore(mock_user_email, name);
                    }
                } else {
                    Log.d(TAG, "failed with: ", task.getException());
                }
            }
        });
    }

    /**
     * Add new users to the database via google account
     * @param mock_user_email: make fake email
     */
    private static void addUserToFireStore(String mock_user_email, String name) {

        // create userDetails object and put into factory for fast local access
        UserDetails currentUser = new UserDetails(name, mock_user_email);
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

    // TODO [END] (HOMEPAGE ON CREATE) -------------------------------------------------------------------


    // TODO [START] (ROUTES PAGE) ------------------------------------------------------------------------
    /**
     * Update UserDetail's routes by grabbing from firestore on activity start
     * @param currentUser
     */
    public static void routesListOnStartFireStore(UserDetails currentUser) {

        users.document(currentUser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(task.isSuccessful()) {
                    String routesJSON = snapshot.get("routes").toString();
                    currentUser.setRoutes(routesJSON);
                } else {
                    Log.d(TAG, "Error getting routeslist on load");
                }
            }
        });
    }

    /**
     * Add/update routes to user document on FireStore
     */
    public static void storeRoutes(String routesToStore, UserDetails currentUser) {

        Map<String, String> routes = new HashMap<>();
        routes.put("routes", routesToStore);
        try {
            users.document(currentUser.getEmail()).set(routes, SetOptions.merge());
        } catch (Exception e) {
            Log.d(TAG, "failed to store routes: ", e);
        }
    }

    /**
     * Get updated routes from UserDetails as it should always be updated
     * @param currentUser
     * @return
     */
    public static List<Route> getUserRoutes(UserDetails currentUser) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Route>>() {}.getType();
        return gson.fromJson(UserDetailsFactory.get(currentUser.getEmail()).getRoutes(), type);
    }
    // TODO [END] (ROUTES PAGE) ------------------------------------------------------------------------


    // TODO [START] (TEAM PAGE) ------------------------------------------------------------------------
    /**
     * Called everytime we go into TeamPage to populate Map of TeamMates
     * @param currentUserEmail
     */
    public static void teamsPageOnStart(String currentUserEmail) {

        users.document(currentUserEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(task.isSuccessful()) {
                    String team = snapshot.get("team").toString();
                    UserDetailsFactory.get(currentUserEmail).setTeam(team);
                    populateTeamMateFactory(team);
                } else {
                    Log.d(TAG, "Error getting team on load");
                }
            }
        });
    }

    /**
     * populate Map of TeamMates every time we go into TeamPage
     * @param teamID
     */
    private static void populateTeamMateFactory(String teamID) {
        TeamMemberFactory.resetMembers();

        teams.document(teamID).collection(MEMBERS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot member : task.getResult()) {
                        String memberEmail = member.get("email").toString();
                        TeamMember memberObj = member.toObject(TeamMember.class);
                        TeamMemberFactory.put(memberEmail, memberObj);
                    }
                }
            }
        });
    }

    /**
     * Call this onCreate/onStart of TeamRoutesPage
     * @param teamID
     */
    public static void populateTeamRoutes(String teamID) {
        TeamMemberFactory.resetRoutes();
        getProposedWalk(teamID);

        users.whereEqualTo("team", teamID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Route>>() {}.getType();
                    for(QueryDocumentSnapshot members : task.getResult()) {
                        String memberEmail = members.getData().get("email").toString();
                        String routesJSON = members.getData().get("routes").toString();
                        if(routesJSON != "") {
                            List<Route> membersRoutes = gson.fromJson(routesJSON, type);
                            for(Route route : membersRoutes) {
                                TeamMemberFactory.addRoute(new Pair<>(memberEmail,route));
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * WHEN USER INVITES SOMEONE TO THEIR TEAM
     * @param currentUser
     * @param mock_teammate_email
     */
    public static void inviteToTeam(UserDetails currentUser, String mock_teammate_email) {

        users.document(mock_teammate_email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(task.isSuccessful()) {
                    // if inviter isn't in a team create one
                    if(currentUser.getTeam() == "") {
                        DocumentReference newTeamRef = teams.document();
                        newTeamRef.collection(MEMBERS).document(currentUser.getEmail())
                                .set(new TeamMember(currentUser.getName(), currentUser.getEmail(), false));
                        currentUser.setTeam(newTeamRef.getId());
                    }

                    // get invitee's info to create TeamMember object to store in new team
                    UserDetails pendingTeamMate = new UserDetails();
                    pendingTeamMate = snapshot.toObject(UserDetails.class);
                    TeamMember pendingMember = new TeamMember(pendingTeamMate.getName(), pendingTeamMate.getEmail(), true);
                    teams.document(currentUser.getTeam()).collection(MEMBERS).document(mock_teammate_email).set(pendingMember);
                    // TODO SEND NOTIFICATION TO INVITEE ALONG WITH INVITER EMAIL
                }
            }
        });
    }

    /**
     * IF USER ACCEPTS INVITE
     * THREE CASES FOR TEAM CREATION
     * CASE 1: BOTH INVITER AND INVITEE ARE NOT IN A TEAM -> CREATE TEAM
     * CASE 2: ONE OF THE TWO IS IN A GROUP -> ONE JOINS THE OTHERS TEAM
     * CASE 3: BOTH ARE IN A GROUP -> THE TEAMS MERGE
     */
    public static void teamCreationOnAccept(UserDetails currentUser, UserDetails newTeamMate) {

        Map<String, String> updateTeam = new HashMap<>();
        updateTeam.put("team", currentUser.getTeam());
        Map<String, Boolean> updateStatus = new HashMap<>();
        updateStatus.put("pendingStatus", false);

        // if accepter was already on a team
        if(newTeamMate.getTeam() != "") {
            Log.d(TAG, "Accepted member does have a team");

            teams.document(newTeamMate.getTeam()).collection(MEMBERS)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for(QueryDocumentSnapshot document : task.getResult()) {
                                    String newMembersName = document.get("name").toString();
                                    String newMembersEmail = document.get("email").toString();
                                    if(newMembersEmail != newTeamMate.getEmail()) {
                                        users.document(newMembersEmail).set(updateTeam, SetOptions.merge());
                                        teams.document(currentUser.getTeam()).collection(MEMBERS).document(newMembersEmail)
                                                .set(new TeamMember(newMembersName, newMembersEmail, false));
                                    }
                                }
                            }
                        }
                    });
                    teams.document(newTeamMate.getTeam()).delete();
            }
        users.document(newTeamMate.getEmail()).set(updateTeam, SetOptions.merge());
        users.document(newTeamMate.getEmail()).set(updateStatus, SetOptions.merge());
    }

    /**
     * If invitee declines, if inviter was originally not on any team delete the temporary team
     * @param inviter
     * @param invitee
     */
    public static void teamCreationOnDecline(UserDetails inviter, UserDetails invitee) {
        teams.document(inviter.getTeam()).collection(MEMBERS).document(invitee.getEmail()).delete();
    }

    // TODO [END] (TEAM PAGE) ------------------------------------------------------------------------




    // TODO [START] (PROPOSED WALKS) -----------------------------------------------------------------

    /**
     * Store the proposed walk into the teams document and send out notification
     * @param proposedWalk
     * @param currentUser
     */
    public static void storeProposedWalk(ProposedWalk proposedWalk, UserDetails currentUser) {

        String proposedWalkJSON = ProposedWalkJsonConverter.convertWalkToJson(proposedWalk);
        Map<String, String> newWalkDetails = new HashMap<>();
        newWalkDetails.put("current proposed walk", proposedWalkJSON);
        teams.document(currentUser.getTeam()).set(newWalkDetails, SetOptions.merge());
        // TODO TRIGGER CLOUD FUNCTION TO NOTIFY ALL TEAMMEMBERS
    }

    /**
     * Retrieving the proposed walk from database
     * @param teamID
     * @return
     */
    public static void getProposedWalk(String teamID) {

        teams.document(teamID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(task.isSuccessful()) {
                    String poposedWalkJSON = snapshot.get("current proposed walk").toString();
                    if(poposedWalkJSON != null) {
                        ProposedWalk pw = ProposedWalkJsonConverter.convertJsonToWalk(poposedWalkJSON);
                        TeamMemberFactory.setProposedWalk(pw);
                    }
                }
            }
        });
    }

    // TODO [END] (PROPOSED WALKS) -------------------------------------------------------------------

}
