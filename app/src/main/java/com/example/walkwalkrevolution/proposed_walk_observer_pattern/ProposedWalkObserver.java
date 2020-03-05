package com.example.walkwalkrevolution.proposed_walk_observer_pattern;

/**
 * Observer interface for getting ProposedWalk data from the ProposedWalkObservable's static fields.
 */
public interface ProposedWalkObserver {

    /**
     * Called in notifyObservers() in the ProposedWalkObservable.
     */
    void update(ProposedWalkObserver o, Object arg);
}
