package com.example.walkwalkrevolution;

/**
 * Object representing user's information
 * in USERS/{userID} in FireStore
 */
public class UserDetails {
    private String name;
    private String email;
    private String routes = "";
    private String team = "";
    private String teamRoutesWalked = "";

    UserDetails() {}

    UserDetails(String name, String email) {
        this.name = name;
        this.email = email;
    }

    UserDetails(String name, String email, String routes, String team) {
        this.name = name;
        this.email = email;
        this.routes = routes;
        this.team = team;
    }

    public void setRoutes(String routes) {
        this.routes = routes;
    }
    public void setTeam(String team) {
        this.team = team;
    }
    public void setTeamRoutesWalked(String teamRouteWalked) { this.teamRoutesWalked = teamRouteWalked; }

    public String getName() { return this.name; }
    public String getEmail() { return this.email; }
    public String getRoutes() { return this.routes; }
    public String getTeam() { return this.team; }
    public String getTeamRoutesWalked() { return this.teamRoutesWalked; }


}
