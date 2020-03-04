package com.example.walkwalkrevolution;

public class UserDetails {
    String name;
    String email;
    String routes = "";
    String team = "";

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

    public String getName() { return this.name; }
    public String getEmail() { return this.email; }
    public String getRoutes() { return this.routes; }
    public String getTeam() { return this.team; }


}
