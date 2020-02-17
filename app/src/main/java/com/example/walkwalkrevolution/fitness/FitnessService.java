package com.example.walkwalkrevolution.fitness;

public interface FitnessService {
    int getRequestCode();
    void setup();
    void updateStepCount();
    void setNextStepCount(long sc);
}
