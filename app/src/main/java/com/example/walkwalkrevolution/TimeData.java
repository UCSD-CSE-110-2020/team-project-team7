package com.example.walkwalkrevolution;

/**
 * Handles timing/mock timing.
 */
public class TimeData {

    private long mockTime;
    private long mockStartTime;
    private boolean mockTimeSet = false;

    /**
     * Constructor, called only once upon first login.
     */
    public TimeData() {
        // Initializes the time variables into a string to be put in shared prefs
    }

    /**
     * Starts mock timing.
     * @param mockTime - the new time to start at
     */
    public void setMockTime(long mockTime) {
        this.mockTime = mockTime;
        mockTimeSet = true;
        mockStartTime = System.currentTimeMillis();
    }

    /**
     * Returns the time or mocked time.
     */
    public long getTime() {
        if (mockTimeSet) {
            return mockTime + (System.currentTimeMillis() - mockStartTime);

        } else {
            return System.currentTimeMillis();
        }
    }

}
