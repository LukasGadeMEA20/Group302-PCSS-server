package com.company;

public class ServerUser {
    String userName = "";
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

    public int getUserID(){
        return userID;
    }

    public String getIpName(){
        return ipName;
    }


}
