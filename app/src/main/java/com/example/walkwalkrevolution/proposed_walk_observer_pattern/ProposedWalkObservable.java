package com.example.walkwalkrevolution.proposed_walk_observer_pattern;

import android.util.Log;

import com.example.walkwalkrevolution.MockFirestoreDatabase;
import com.example.walkwalkrevolution.TeamMemberFactory;
import com.example.walkwalkrevolution.custom_data_classes.ProposedWalk;
import com.example.walkwalkrevolution.custom_data_classes.ProposedWalkJsonConverter;

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
        // TODO, fetch the is a proposed walk json string from the cloud

        // Fetch the proposed walk
        ProposedWalk cloudProposedWalk = MockFirestoreDatabase.getProposedWalk(TeamMemberFactory.get("CalvinID"));

        Log.d(TAG, "Comparing fetched proposed walk to current...");

        if (proposedWalk == null && cloudProposedWalk != null) {
            // current proposed walk is null but the one in the cloud isn't
            setProposedWalk(cloudProposedWalk);

        } else if (proposedWalk != null && !proposedWalk.equals(cloudProposedWalk)) {
            // proposed walk exists, but the one in the cloud is different
            setProposedWalk(cloudProposedWalk);
        }
    }

    /**
     * Set the proposed walk instance variable. Notify observers of the change.
     * @param proposedWalk
     */
    public void setProposedWalk(ProposedWalk proposedWalk) {
        if (proposedWalk != null) {
            Log.d(TAG, "A new proposed walk was set. Named: " + proposedWalk.name);
        }

        this.proposedWalk = proposedWalk;
        this.setChanged();
        this.notifyObservers(proposedWalk);
    }

    /**
     * Clear the currently saved proposed walk to null. For withdrawing a walk.
     */
    public void clearProposedWalk() {
        setProposedWalk(null);
        Log.d(TAG, "Cleared Proposed Walk to null");
    }

}
