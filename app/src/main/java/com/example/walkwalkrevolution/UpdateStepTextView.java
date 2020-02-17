package com.example.walkwalkrevolution;

public interface UpdateStepTextView {
    void updateStepView(String updatedSteps);
    void setStepCount(long sc);
    long getStepCount();
    void updatesMilesView(String updatedMiles);
    void setMiles(double mi);
    double getMiles();
    double getStepsPerMile();

}
