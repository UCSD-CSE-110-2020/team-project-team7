package com.example.walkwalkrevolution;


import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MultRoutesTest{

    @Rule
    public ActivityTestRule<HomePage> mActivityTestRule = new ActivityTestRule<>(HomePage.class);

    @Test
    public void multRoutesTest() {
    }
}
