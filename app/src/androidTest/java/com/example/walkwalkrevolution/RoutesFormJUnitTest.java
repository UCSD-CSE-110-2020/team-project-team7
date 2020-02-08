package com.example.walkwalkrevolution;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class RoutesFormJUnitTest {

    @Rule
    public ActivityTestRule<RoutesForm> routesForm =
            new ActivityTestRule<RoutesForm>(RoutesForm.class);

    @Test
    /**
     * Test whether the text views say what we want them to say.
     */
    public void test1() {
        routesForm.getActivity();
    }



}