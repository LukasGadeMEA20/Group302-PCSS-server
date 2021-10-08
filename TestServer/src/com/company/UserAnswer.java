package com.company;

public class UserAnswer {
    ServerUser user;
    String userAnswer;

    UserAnswer(ServerUser _user, String _userAnswer){
        user = _user;
        userAnswer = _userAnswer;
    }

    public String getUserAnswer(){
        return userAnswer;
    }
}
