package com.example.walkwalkrevolution;

import java.util.Comparator;

class TreeSetComparator implements Comparator<Route> {

    @Override
    public int compare(Route route, Route t1) {
        //compares lowercase values so content is compared, not capitalization (A vs a)
        int result = route.name.toLowerCase().compareTo(t1.name.toLowerCase());
        //strings are the same content wise --> compare capitalization now
        if(result == 0){
            return route.name.compareTo(t1.name);
        }
        return result;
    }
}