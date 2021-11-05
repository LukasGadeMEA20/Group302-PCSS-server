package com.company;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Main {
    static int port = 8000;

    public static void main(String[] args) {

        new Thread( () ->{
            try{
                // Starts the server and prints the information of the server.
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("Yakboks Server\nIP: " + InetAddress.getLocalHost().getHostAddress() + "\nPort: " + port  + "\nStarted at " + new Date() + '\n');

                // A counter for how many clients have joined
                int clientNo = 0;
                // A limit to the amount of clients that have joined
                // - this is special for our game, but can be changed in the while loop to true
                // - we just wanted a limit to how many people could join the game, as too many players can be chaotic.
                int maxClients = 7;

                // While loop which connects users to the server and starts a thread for that user.
                while(clientNo < maxClients) {
                    // Gets the users socket
                    Socket connectToClient = serverSocket.accept();
                    // Gives the user a number and counts the global client number up. This is used for identification in the list
                    int thisUserNumber = clientNo;
                    clientNo++;

                    // Prints to the server command prompt that a user joined the server.
                    System.out.println("Starting thread for client " + clientNo + " at " + new Date()+'\n');
                    InetAddress inetAddress = connectToClient.getInetAddress();
                    System.out.println("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + '\n');
                    System.out.println("Client " + clientNo + "'s host address is " + inetAddress.getHostAddress() + '\n');

                    // Starts the thread for the user and starts it
                    new Thread(
                            new GameFlowRunnable(connectToClient, inetAddress.getHostName() + " thread", inetAddress.getHostName(), thisUserNumber)
                    ).start();

                    // Waits half a second before it begins connecting a new user
                    Thread.sleep(500);
                }
            // Catches exceptions and prints it.
            } catch (IOException e){
                System.err.println(e);
            } catch(InterruptedException e){
                System.err.println(e);
            }
        }).start();
    }
}