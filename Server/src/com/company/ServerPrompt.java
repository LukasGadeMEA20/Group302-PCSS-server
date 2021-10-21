package com.company;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ServerPrompt {

    //String containing the random selected prompt
    String randomPrompt;

    //ArrayList containing all the prompts from the promptsFile
    ArrayList<String> prompts = new ArrayList<>();

    String prompt = "";
    ArrayList<UserAnswer> userAnswers = new ArrayList<UserAnswer>();
    boolean allReady = false;
    int numberOfUsers = 0;

    ServerPrompt(int _numberOfUsers){
        prompt = generatePrompt();
        numberOfUsers = _numberOfUsers;
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
        try{
            System.out.println(numberOfUsers); System.out.println(userAnswers.size());
        if(numberOfUsers > userAnswers.size()){
            allReady = false;
        } else {
            allReady = true;
            System.out.println("POG");
        }}catch (NullPointerException e){
            //e.printStackTrace();
        }
    }

    //Function that reads a file and adds its contents to an ArrayList
    public void readFile(){
        //Created a scanner that reads the promptsFile.txt file
        try {
            Scanner scanner = new Scanner(new File("promptsFile.txt"));
            while (scanner.hasNextLine()){
                prompts.add(scanner.nextLine());
            }
            scanner.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("Couldn't find file");
            }
    }

    //Choose a random prompt from the ArrayList
    public void choosePrompt() {
        // Creating a random
        Random random = new Random();

        // Gets a random integer based on the size of the prompts and sets it as the prompt.
        int randomNum = random.nextInt(prompts.size());
        prompt = prompts.get(randomNum);
    }

}


