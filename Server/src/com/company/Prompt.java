package com.company;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Prompt {
    String[] answer;
    String randomPrompt;

    //ArrayList containing all the prompts from the promptsFile
    ArrayList<String> prompts = new ArrayList<>();

    //Function that reads a file and adds its contents to an ArrayList
    public void readFile(){
        //Created a scanner that reads the promptsFile.txt file
        try (Scanner scanner = new Scanner(new File("promptsFile.txt"))){

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
        //Creating a random
        Random random = new Random();

        //Gets the size of the ArrayList
        int randomNum = random.nextInt(prompts.size());
        randomPrompt = prompts.get(randomNum);

        //Print a value from the ArrayList
        System.out.println(randomPrompt);
    }

}


