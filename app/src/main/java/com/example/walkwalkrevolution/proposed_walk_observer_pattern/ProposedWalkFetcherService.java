package com.example.walkwalkrevolution.proposed_walk_observer_pattern;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Handling asynchronous task requests for fetching Proposed Walk data from the cloud.
 * Only runs in ProposedWalkObservers.
 */
public class ProposedWalkFetcherService extends IntentService {

    private static final String TAG = "ProposedWalkFetcherService";

    private boolean isRunning = false;

    // Constructor
    public ProposedWalkFetcherService() {
        super("ProposedWalkFetcherService");
    }

    /**
     * Runs when the intent service is started. Turns on the fetching service.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Started service!");
        isRunning = true;
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Runs when the intent service is stopped. Turns off the fetching service.
     */
    @Override
    public void onDestroy() {
        Log.d(TAG, "Stopped service!");
        isRunning = false;
        super.onDestroy();
    }

    private final int WAIT_TIME = 150; // Constant, how long to wait in between calls to fetches

    /**
     * Call the ProposedWalkObserver's static fetch method to update the Observable based on
     * what's in the cloud.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        while (isRunning) {
            synchronized (this) {
                    try {
                        wait(WAIT_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Log.d(TAG, "calling fetch method...");
                    ProposedWalkObservable.fetchProposedWalk();
                }

            }
    }

}
