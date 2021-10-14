package com.company;

import java.util.ArrayList;

public class ServerPrompt {
    String prompt = "";
    ArrayList<UserAnswer> userAnswers = new ArrayList<UserAnswer>();
    boolean allReady = false;
    int numberOfUsers = 0;

    ServerPrompt(){
        prompt = generatePrompt();
    }
    ServerPrompt(String _prompt){
        prompt = _prompt;
    }

    public String getPrompt(){
        return prompt;
    }

    public void setPrompt(String _prompt){
        prompt = _prompt;
    }

    public void setNumberOfUsers(int _users){
        numberOfUsers = _users;
    }

    public void addUserAnswer(UserAnswer _userAnswer){
        userAnswers.add(_userAnswer);
    }

    public UserAnswer getUserAnswerAtPoint(int i){
        return userAnswers.get(i);
    }

    public ArrayList<UserAnswer> getUserAnswers(){
        return userAnswers;
    }

    public String generatePrompt(){
        return "pis";
    }

    public boolean getAllReady(){
        return allReady;
    }

    public void setAllReady(boolean _rdy){
        allReady = _rdy;
    }

    public void checkAllReady(){
        if(numberOfUsers > userAnswers.size()){
            allReady = false;
        } else {
            allReady = true;
        }
    }
}
