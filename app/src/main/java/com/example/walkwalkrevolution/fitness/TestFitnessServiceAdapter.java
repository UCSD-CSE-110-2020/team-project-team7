package com.example.walkwalkrevolution.fitness;

import com.example.walkwalkrevolution.HomePage;

public class TestFitnessServiceAdapter implements FitnessService {
    private static final String TAG = "[TestFitnessService]: ";
    private HomePage homePage;
    private long nextStepCount;

    public TestFitnessServiceAdapter(HomePage hp) {
        this.homePage = hp;
    }

    @Override
    public void setNextStepCount(long sc) {
        this.nextStepCount = sc;
    }

    @Override
    public int getRequestCode() {
        return 0;
    }

    @Override
    public void setup() {
        System.out.println(TAG + "setup");
    }

    @Override
    public void updateStepCount() {
        System.out.println(TAG + "updateStepCount");
        homePage.setStepCount(nextStepCount);
        homePage.updateStepView(String.valueOf(homePage.getStepCount()));
    }
}