package com.example.walkwalkrevolution.proposed_walk_observer_pattern;

import android.util.Log;

import com.example.walkwalkrevolution.CloudCallBack;
import com.example.walkwalkrevolution.CloudDatabase;
import com.example.walkwalkrevolution.TeamMemberFactory;
import com.example.walkwalkrevolution.custom_data_classes.ProposedWalk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

/**
 * Observable that keeps track of a Team's ProposedWalk for several Activity observers.
 * Use addObserver and deleteObserver for registering/deleting.
 */

public  class ProposedWalkObservable extends Observable {

    private final static String TAG = "ProposedWalkObservable";

    // Static var holding team proposed walk, updated to current proposed walk when fetch is called
    private static ProposedWalk proposedWalk = null;

    // ArrayList holding all observers
    private static ArrayList<ProposedWalkObserver> observers
            = new ArrayList<ProposedWalkObserver>();


    /**
     * Fetch the team's proposed walk from the cloud, if there is one.
     * If it's different from the current proposed walk, set it.
     */
    public static void fetchProposedWalk(CloudCallBack cb) {
        // Fetch the proposed walk
        Log.d(TAG, "calling Cloud database populate team proposed walk...");

        CloudDatabase.populateTeamProposedWalk(new CloudCallBack() {
            @Override
            public void callBack() {
                ProposedWalk cloudProposedWalk = TeamMemberFactory.getProposedWalk();

                Log.d(TAG, "Comparing fetched proposed walk to current...");

                if (proposedWalk == null && cloudProposedWalk != null) {
                    // current proposed walk is null but the one in the cloud isn't
                    setProposedWalk(cloudProposedWalk);

                } else if (proposedWalk != null && !proposedWalk.equals(cloudProposedWalk)) {
                    // proposed walk exists, but the one in the cloud is different
                    setProposedWalk(cloudProposedWalk);
                }
                cb.callBack();
            }
        });
    }


    // MANIPULATION OF PROPOSED WALK DATA VAR ------------------------------------------------------

    /**
     * Set the proposed walk instance variable. Notify observers of the change.
     */
    public static void setProposedWalk(ProposedWalk proposedWalkA) {
        if (proposedWalk != null) {
            Log.d(TAG, "A new proposed walk was set. Named: " + proposedWalk.getName());
        }

        proposedWalk = proposedWalkA;
        notifyObservers(proposedWalk);
    }

    /**
     * Set the proposed walk and store it in the cloud for scheduling.
     */
    public static void setProposedWalkInCloud(ProposedWalk proposedWalkA) {
        if (proposedWalk != null) {
            Log.d(TAG, "A new proposed walk was set. Named: " + proposedWalk.getName());
        }

        proposedWalk = proposedWalkA;
        CloudDatabase.storeTeamProposedWalk(proposedWalk);
        notifyObservers(proposedWalk);
    }

    /**
     * Clear the currently saved proposed walk to null.
     */
    public static void clearProposedWalk() {
        setProposedWalk(null);
        CloudDatabase.storeTeamProposedWalk(proposedWalk);
        fetchProposedWalk(new CloudCallBack() {
            @Override
            public void callBack() {
                return;
            }
        });
        Log.d(TAG, "Cleared Proposed Walk to null");
    }


    // MANIPULATION OF OBSERVERS -------------------------------------------------------------------

    /**
     * Loop through all concrete observers in the observers ArrayList and call their update methods.
     */
    public static void notifyObservers(ProposedWalk proposedWalk) {
        for (ProposedWalkObserver observer : observers) {
            observer.update(observer, proposedWalk);
        }
    }

    /**
     * Add an observer to observers.
     */
    public static void register(ProposedWalkObserver observer) {
        observers.add(observer);
    }

    /**
     * Remove an observer form observers.
     */
    public static void removeObserver(ProposedWalkObserver observer) {
        // Create an iterator over the observers array list
        Iterator<ProposedWalkObserver> i = observers.iterator();

        // loop over observers
        while (i.hasNext()) {
            ProposedWalkObserver myObserver = i.next();

            // if we found the observer, delete it from the array list
            if (observer == myObserver) {
                i.remove();
                break;
            }
        }
    }


    // GETTER AND SETTER FOR PROPOSED WALK ---------------------------------------------------------

    /**
     * Returns the proposedWalk static variable.
     */
    public static ProposedWalk getProposedWalk() {
        return proposedWalk;
    }

    /**
     * Store the proposedWalk into the cloud.
     */
    public static void storeProposedWalk(ProposedWalk proposedWalk) {
        CloudDatabase.storeTeamProposedWalk(proposedWalk);
    }

}
