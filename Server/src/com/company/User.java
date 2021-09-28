package com.company;

public class User {
    String userName;
    int points;
    boolean inLobby;
    boolean afk;
    float afkTimer;
    boolean ready;

    public void setUserName(String userName) { //sets the username of a user
        this.userName = userName;
    }
    public String getUserName() { //gets the assigned username from the user object
        return this.userName;
    }
    public void setPoints(int points) { //sets the points of a user
        this.points = points;
    }
    public int getPoints() { //gets the points of a user
        return this.points;
    }

}
