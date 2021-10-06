package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class WorkerRunnable implements Runnable{
    Socket connectToClient = null;
    String name = "";
    String id = "";

    WorkerRunnable(Socket _connectToClient, String _name, String _id){
        connectToClient = _connectToClient;
        name = _name;
        id = _id;
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
