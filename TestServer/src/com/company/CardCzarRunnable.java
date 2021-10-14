package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class CardCzarRunnable implements Runnable{
    Socket connectToClient = null;
    String name = "";
    ServerUser user;
    ServerPrompt prompt;

    CardCzarRunnable(Socket _connectToClient, String _name, ServerUser _user, ServerPrompt _prompt){
        connectToClient = _connectToClient;
        name = _name;
        user = _user;
        prompt = _prompt;
    }

    @Override
    public void run() {
        try{
            System.out.println("Connected to a client at " + new Date() + '\n');
            boolean connected = true;

            DataInputStream fromClient = new DataInputStream(connectToClient.getInputStream());
            DataOutputStream toClient = new DataOutputStream(connectToClient.getOutputStream());

            DataOutputStream toFile = new DataOutputStream(new FileOutputStream(user.getIpName()+".txt"));

            while(connected){
                toClient.writeUTF(prompt.getPrompt());
                prompt.checkAllReady();
                if(prompt.getAllReady()) {
                    toClient.writeUTF("Please choose the prompt you like the most:");
                    String promptsToPrint = "";
                    for(int i = 0; i < prompt.getUserAnswers().size(); i++){
                        promptsToPrint += i + " - " + prompt.getUserAnswerAtPoint(i).getUserAnswer()+'\n';
                    }
                    toClient.writeUTF(promptsToPrint);
                    toClient.writeBoolean(true);
                    String userAnswer = fromClient.readUTF();
                    // take user choice here.
                } else {
                    Thread.sleep(2000);
                }
                /*String userAnswer = fromClient.readUTF();
                prompt.addUserAnswer(new UserAnswer(user, userAnswer));
                toClient.writeUTF(prompt.getUserAnswerAtPoint(user.getUserID()).getUserAnswer());
                System.out.println(prompt.getUserAnswerAtPoint(user.getUserID()).getUserAnswer());*/
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
