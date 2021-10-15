package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class WritePromptRunnable implements Runnable{
    Socket connectToClient = null;
    String name = "";
    ServerUser user;
    ServerPrompt prompt;

    WritePromptRunnable(Socket _connectToClient, String _name, ServerUser _user, ServerPrompt _prompt){
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
                toClient.writeUTF(prompt.getPrompt() + " player");
                /*toClient.writeBoolean(true);
                String userAnswer = fromClient.readUTF();
                prompt.addUserAnswer(new UserAnswer(user, userAnswer, true));
                toClient.writeUTF(prompt.getUserAnswerAtPoint(user.getUserID()).getUserAnswer());
                System.out.println(prompt.getUserAnswerAtPoint(user.getUserID()).getUserAnswer());*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
