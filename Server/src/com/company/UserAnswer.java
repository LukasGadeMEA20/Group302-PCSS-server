package com.company;

/*
    A very simple class which store which user wrote in the answer,
    what the answer is and a simple boolean to make sure the program knows if it is ready.
*/
public class UserAnswer {
    ServerUser user;
    String userAnswer;
    boolean ready;
    
    UserAnswer(ServerUser _user, String _userAnswer, boolean _ready){
        user = _user;
        userAnswer = _userAnswer;
        ready = _ready;
    }

    public String getUserAnswer(){
        return userAnswer;
    }

    public ServerUser getUser(){
        return user;
    }

    public void setReady(boolean _r){
        ready = _r;
    }

    public boolean getReady(){
        return ready;
    }
}
