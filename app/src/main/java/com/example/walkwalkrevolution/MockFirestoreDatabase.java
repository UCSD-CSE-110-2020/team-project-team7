package com.example.walkwalkrevolution;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void checkUserExists(String mock_user_id, String mock_user_email) {
        users.document(mock_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Log.d("DB", "user exists");
                    } else {
                        Log.d("DB", "user doesnt exist");
                        addUser(mock_user_id, mock_user_email);
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
    private void addUser(String mock_user_id, String mock_user_email) {

        Map<String, String> user_info = new HashMap<>();
        user_info.put("email", mock_user_email);
        user_info.put("team", "");

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
    public void storeRoutes(String routesToStore, String mock_user_id) {
        Map<String, String> routes = new HashMap<>();
        routes.put("routes", routesToStore);
        users.document(mock_user_id).set(routes, SetOptions.merge());
    }
}
