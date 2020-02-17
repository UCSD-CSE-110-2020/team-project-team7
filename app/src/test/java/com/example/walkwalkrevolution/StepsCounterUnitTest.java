package com.example.walkwalkrevolution;

import android.widget.TextView;

import com.example.walkwalkrevolution.fitness.GoogleFitAdapter;

import org.junit.Test;

import static org.junit.Assert.*;





public class StepsCounterUnitTest {


    private class mockHomePage extends HomePage {

        private double miles;
        public void setStepUpdateView(long steps) {
            stepsPerMile = calculateStepsPerMile(70);
            stepCount = steps;
            miles = Math.floor(steps / stepsPerMile * 100)/100;
        }

    }

    @Test
    public void testStepsToMiles() {

        mockHomePage mock = new mockHomePage();
        mock.setStepUpdateView(2300);
        assertEquals(1.04, mock.miles, 0.01);
        assertEquals(2300, mock.stepCount);

    }

}
