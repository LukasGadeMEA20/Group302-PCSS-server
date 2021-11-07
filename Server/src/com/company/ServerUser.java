package com.company;

public class ServerUser {
    // Information about the user
    String userName = "";
    int points= 0;
    int userID = 0;
    String ipName = "";

    // Constructor for creating a user with a username
    ServerUser(String _userName, int _id, String _ipName){
        userName = _userName;
        userID = _id;
        ipName = _ipName;
    }

    // Constructor for creating a user without a username
    ServerUser(int _id, String _ipName){
        userID = _id;
        ipName = _ipName;
    }
    
    // - Getters and setters - //
    public String getUserName(){
        return userName;
    }

    public void setPoints(int points) { //sets the points of a user
        this.points = points;
    }

    public int getPoints() { //gets the points of a user
        return this.points;
    }

    public void setUserName(String _userName){
        userName = _userName;
    }

    public int getUserID(){
        return userID;
    }

    public String getIpName(){
        return ipName;
    }
    
    // Delegating a singular point to the user.
    public void delegatePoint(){
        points++;
    }
}
