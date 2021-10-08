package com.company;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class Main {
    static int port = 302;

    public static void main(String[] args) {

        new Thread( () ->{
            try{
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("Server started at " + new Date() + '\n');
                int clientNo = 0;

                ServerPrompt prompt = new ServerPrompt();
                int state = 0;
                boolean acceptingUsers = true;

                while(true) {
                    Socket connectToClient = serverSocket.accept();
                    int thisUserNumber = clientNo;
                    clientNo++;

                    System.out.println("Starting thread for client " + clientNo + " at " + new Date()+'\n');
                    InetAddress inetAddress = connectToClient.getInetAddress();
                    System.out.println("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + '\n');
                    System.out.println("Client " + clientNo + "'s host address is " + inetAddress.getHostAddress() + '\n');

                    switch(state){
                        case 0:

                    }

                    ServerUser currentUser = new ServerUser(thisUserNumber, inetAddress.getHostName());

                    new Thread(
                            new WorkerRunnable(connectToClient, "Multithreaded server", inetAddress.getHostName(), prompt)
                    ).start();
                    Thread.sleep(3000);
                    //System.out.println(prompt.getUserAnswerAtPoint(thisUserNumber).getUserAnswer());
                }
            } catch (IOException e){
                System.err.println(e);
            }catch (InterruptedException e){
                System.err.println(e);
            }
        }).start();
    }
}
