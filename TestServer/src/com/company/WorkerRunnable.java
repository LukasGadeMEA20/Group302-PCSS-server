package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class WorkerRunnable implements Runnable{
    Socket connectToClient = null;
    String name = "";
    String id = "";
    ServerPrompt prompt;

    WorkerRunnable(Socket _connectToClient, String _name, String _id, ServerPrompt _prompt){
        connectToClient = _connectToClient;
        name = _name;
        id = _id;
        prompt = _prompt;
    }

    @Override
    public void run() {
        try{
            System.out.println("Connected to a client at " + new Date() + '\n');
            boolean connected = true;

            DataInputStream fromClient = new DataInputStream(connectToClient.getInputStream());
            DataOutputStream toClient = new DataOutputStream(connectToClient.getOutputStream());

            DataOutputStream toFile = new DataOutputStream(new FileOutputStream(id+".txt"));

            while(connected){
                toClient.writeUTF(prompt.getPrompt());
                String userAnswer = fromClient.readUTF();
                prompt.addUserAnswer(new UserAnswer(new ServerUser("test") , userAnswer));
                System.out.println(prompt.getUserAnswerAtPoint(0).getUserAnswer());
                Thread.sleep(10000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e){

        }
    }
}
