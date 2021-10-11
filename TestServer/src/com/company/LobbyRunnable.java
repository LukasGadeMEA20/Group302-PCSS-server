package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class LobbyRunnable implements Runnable{
    Socket connectToClient = null;
    String name = "";
    String id = "";
    ServerPrompt prompt;

    LobbyRunnable(Socket _connectToClient, String _name, String _id, ServerPrompt _prompt){
        connectToClient = _connectToClient;
        name = _name;
        id = _id;
        prompt = _prompt;
    }

    @Override
    public void run() {
        try {
            boolean connected = true;

            DataInputStream fromClient = new DataInputStream(connectToClient.getInputStream());
            DataOutputStream toClient = new DataOutputStream(connectToClient.getOutputStream());

            DataOutputStream toFile = new DataOutputStream(new FileOutputStream(id + ".txt"));

            while (connected) {
                String userName = fromClient.readUTF();
                toFile.writeUTF(userName);
                Thread.sleep(10000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e){

        }
    }
}
