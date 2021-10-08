package com.company;

import java.util.Random;

public class Prompt {
    String[] answer;
    String randomPrompt;
    Random random = new Random();

    String[] prompts = {
            "Tell a funny joke!",
            "I was afraid of _____",
            "prut",
            "Hej med dig smukke"
    };

    public void chooseWinner(){

    }

    public void delegatePoint(){

    }

    public void choosePrompt() {
        int randomNum = random.nextInt(prompts.length);
        randomPrompt = prompts[randomNum].toString();
        System.out.println(randomPrompt);

    }
}


