package com.example.walkwalkrevolution;

import android.widget.EditText;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class RoutesFormJUnitTest {

    @Rule
    public ActivityScenarioRule<RoutesForm> scenarioRule =
            new ActivityScenarioRule<>(RoutesForm.class);

    public ActivityScenario scenario;

    @Before
    public void setup() {
        scenario = scenarioRule.getScenario();
    }

    private EditText routeNameEditText;
    private EditText startingPEditText;
    private EditText minutesEditText;
    private EditText secondsEditText;
    private TextView dateDisplayTextView;
    private TextView stepsView;
    private TextView distanceView;


    private void init(ActivityScenario scenario) {
//        routeNameEditText = (EditText) scenario.findViewById(R.id.routeNameEditText);
//        startingPEditText = (EditText) scenario.routesForm.findViewById(R.id.startingPEditText);
//        minutesEditText = (EditText) scenario.findViewById(R.id.minutesEditText);
//        secondsEditText = (EditText) scenario.findViewById(R.id.secondsEditText);
//        dateDisplayTextView = (TextView) scenario.findViewById(R.id.dateDisplayTextView);
//        stepsView = (TextView) scenario.findViewById(R.id.stepsNumView);
//        distanceView = (TextView) scenario.findViewById(R.id.distanceNumView);
    }

    @Test
    /**
     * Test whether the text views say what we want them to say.
     */
    public void test1() {


    }



}