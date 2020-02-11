package com.example.walkwalkrevolution;

import android.widget.TextView;

import com.example.walkwalkrevolution.fitness.GoogleFitAdapter;

import org.junit.Test;

import static org.junit.Assert.*;





public class StepsCounterUnitTest {


    private class mockHomePage extends HomePage {

        TextView stepView;
        private long stepCount;
        //GoogleFitAdapter gfa = new GoogleFitAdapter(this);
        //StepCountActivity sc = new StepCountActivity(gfa);
        public void setStepCountMyself(long steps) {
            this.stepCount = steps;
        }

    }

}
