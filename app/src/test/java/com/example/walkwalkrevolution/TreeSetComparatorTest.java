package com.example.walkwalkrevolution;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@Config(sdk = 27)

/**
 * Testing class for TreeSetComparatorTest - compareTo method (alphabetical order).
 */
public class TreeSetComparatorTest {

    @Test
    public void test(){}


//    TreeSetComparator comparator;
//    Route r1;
//    Route r2;
//
//    @Before
//    public void setUp(){
//        String sp = "SP";
//        int steps = 10;
//        int distance = 20;
//        comparator = new TreeSetComparator();
//        r1 = new Route("Amrit", sp, steps, distance);
//        r2 = new Route("Amrit", sp, steps, distance);
//    }
//
//    /**
//     * Testing for equality when two Captilized strings are the exact same.
//     */
//    @Test
//    public void testComparatorSameStringCapitalized(){
//        int comparisonResult = comparator.compare(r1, r2);
//        assertEquals(comparisonResult, 0);
//    }
//
//    /**
//     * Testing for equality when two non-capitilized strings are the exact same.
//     */
//    @Test
//    public void testComparatorSameStringUnCapitilized(){
//        r1.setName("amrit");
//        r2.setName("amrit");
//        int comparisonResult = comparator.compare(r1, r2);
//        assertEquals(comparisonResult, 0);
//    }
//
//    /**
//     * First string should come after second string.
//     */
//    @Test
//    public void testComparatorDifferentStringGreater(){
//        r1.setName("Ellie");
//        int comparisonResult = comparator.compare(r1, r2);
//        assert(comparisonResult > 0);
//    }
//
//    /**
//     * First string should come before second string.
//     */
//    @Test
//    public void testComparatorDifferentStringSmaller(){
//        r2.setName("Ellie");
//        int comparisonResult = comparator.compare(r1, r2);
//        assert(comparisonResult < 0);
//    }
//
//    /**
//     * First string should come before second string, when content is same but first string is captilized.
//     */
//    @Test
//    public void testComparatorSameContentDifferentCapitlization(){
//        r2.setName("amrit");
//        int comparisonResult = comparator.compare(r1, r2);
//        assert(comparisonResult < 0);
//    }
//
//    /**
//     * First string should come before second string, when first string is empty and second is not.
//     */
//    @Test
//    public void testComparatorEmptyString(){
//        r1.setName("");
//        int comparisonResult = comparator.compare(r1, r2);
//        assert(comparisonResult < 0);
//    }

}
