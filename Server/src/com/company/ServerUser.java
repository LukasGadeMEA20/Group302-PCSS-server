package com.company;

public class ServerUser {
    String userName = "";
    int points= 0;
    int userID = 0;
    String ipName = "";

    ServerUser(String _userName, int _id, String _ipName){
        userName = _userName;
        userID = _id;
        ipName = _ipName;
    }

    ServerUser(int _id, String _ipName){
        userID = _id;
        ipName = _ipName;
    }
    public String getUserName(){
        return userName;
    }

    public void setPoints(int points) { //sets the points of a user
        this.points = points;
    }

    public void delegatePoint(){
        points++;
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
}
