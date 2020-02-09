package com.example.walkwalkrevolution;

import java.util.Comparator;

class TreeSetComparator implements Comparator<Route> {

    @Override
    public int compare(Route route, Route t1) {
        return route.name.toLowerCase().compareTo(t1.name.toLowerCase());
    }
}