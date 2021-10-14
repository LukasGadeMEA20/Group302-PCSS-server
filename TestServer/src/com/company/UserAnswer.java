package com.company;

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

    public void setReady(boolean _r){
        ready = _r;
    }

    public boolean getReady(){
        return ready;
    }
}
