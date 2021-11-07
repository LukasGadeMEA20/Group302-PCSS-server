package com.company;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ServerPrompt {

    //ArrayList containing all the prompts from the promptsFile
    ArrayList<String> prompts = new ArrayList<>();

    // What the current prompt is
    String prompt = "";
    
    // Attributes for controlling how many users and how many answers
    ArrayList<UserAnswer> userAnswers = new ArrayList<UserAnswer>();
    int numberOfUsers = 0;
    
    // The answer that won
    String winningAnswer;
    
    // Boolean for checking if everyone has written an answer
    boolean allReady = false;

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

    // Getting a specific user answer at an index
    public UserAnswer getUserAnswerAtPoint(int i){
        return userAnswers.get(i);
    }

    public ArrayList<UserAnswer> getUserAnswers(){
        return userAnswers;
    }

    public boolean getAllReady(){
        return allReady;
    }

    public void setAllReady(boolean _rdy){
        allReady = _rdy;
    }

    // Clears the list of user answers. Used when resetting the game.
    public void clearUserAnswers(){
        userAnswers = new ArrayList<UserAnswer>();
    }

    public void setWinner(int i){
        winningAnswer = getUserAnswerAtPoint(i).getUserAnswer();
    }

    public String getWinner(){
        return winningAnswer;
    }

    // Method for checking if every player has written an answer in
    public void checkAllReady(){
        try{ // We make sure we do not go out of bounds and use a try-catch for null pointer exception.
            // then we check if the list of user answers is smaller than the number of users
            // Minus one, as one of the players is the card czar.
            if(numberOfUsers-1 > userAnswers.size()){
                allReady = false;
            } else {
                allReady = true;
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    //Function that reads a file and adds its contents to an ArrayList
    public void readFile(){
        //Created a scanner that reads the promptsFile.txt file
        try {
            // Scanner attribute which is based on a file instead of "System.in"
            Scanner scanner = new Scanner(new File("promptsFile.txt"));
            // Reads every line and adds it to the prompts attribute
            while (scanner.hasNextLine()){
                prompts.add(scanner.nextLine());
            }
            // Close the scanner to not cause data leaks.
            scanner.close();

            } catch (FileNotFoundException e) { // If it could not find the file, we catch it and write it to the server.
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
        prompts.remove(randomNum);
    }

}


