package com.example.walkwalkrevolution.proposed_walk_observer_pattern;

import android.util.Log;

import com.example.walkwalkrevolution.custom_data_classes.ProposedWalk;

import java.util.Observable;

/**
 * Observable that keeps track of a Team's ProposedWalk for several Activity observers.
 * Use addObserver and deleteObserver for registering/deleting.
 */
public class ProposedWalkObservable extends Observable {

    private final String TAG = "ProposedWalkObservable";

    private ProposedWalk proposedWalk;

    /**
     * Fetch the team's proposed walk from the cloud, if there is one.
     * If it's different from the current proposed walk, set it.
     */
    public void fetchProposedWalk() {
        // TODO, check if there is a proposed walk in the cloud

        // Fetch the proposed walk
        ProposedWalk cloudProposedWalk = new ProposedWalk("" , "", ""); // Filler code

        Log.d(TAG, "Comparing fetched proposed walk to current...");
        if (!proposedWalk.equals(cloudProposedWalk)) {
            setProposedWalk(cloudProposedWalk);
        }
    }

    /**
     * Set the proposed walk instance variable. Notify observers of the change.
     * @param proposedWalk
     */
    public void setProposedWalk(ProposedWalk proposedWalk) {
        Log.d(TAG, "A new proposed walk was set. Named: " + proposedWalk.name);
        this.proposedWalk = proposedWalk;
        this.setChanged();
        this.notifyObservers(proposedWalk);
    }

    /**
     * Clear the currently saved proposed walk to null.
     */
    public void clearProposedWalk() {
        setProposedWalk(null);
        Log.d(TAG, "Cleared Proposed Walk to null");
    }

}
