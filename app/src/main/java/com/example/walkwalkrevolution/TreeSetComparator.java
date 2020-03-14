package com.example.walkwalkrevolution;

import java.util.Comparator;

/**
 * Comparator used for TreeSetManipulation.java - compares Routes in TreeSet by their names.
 */
public class TreeSetComparator implements Comparator<String> {

//    /**
//     * Mantains alphabetical order. Capitilized routes rank higher than non-captilized ones with same name.
//     * @param route
//     * @param t1
//     * @return
//     */
//    @Override
//    public int compare(Route route, Route t1) {
//        //compares lowercase values so content is compared, not capitalization (A vs a)
//        int result = route.name.toLowerCase().compareTo(t1.name.toLowerCase());
//        //strings are the same content wise --> compare capitalization now
//        if(result == 0){
//            return route.name.compareTo(t1.name);
//        }
//        return result;
//    }

    @Override
    public int compare(String s, String t1) {
        //compares lowercase values so content is compared, not capitalization (A vs a)
        int result = s.toLowerCase().compareTo(t1.toLowerCase());
        //strings are the same content wise --> compare capitalization now
        if(result == 0){
            return s.compareTo(t1);
        }
        return result;
    }
}