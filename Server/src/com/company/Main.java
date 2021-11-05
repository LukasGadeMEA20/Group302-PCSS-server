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


// GAMMEL DANIEL KODE
/*ServerUser user1 = new ServerUser("Brian",0, "Brian");
                ServerUser user2 = new ServerUser("Klaus",1, "Klaus");
                ServerUser user3 = new ServerUser("Dart Monkey",2, "Dart Monkey");

                joinedUsers.queue(user1);
                joinedUsers.queue(user2);
                joinedUsers.queue(user3);
*/
//Scramble the joined users & move the first user to the last
//                SelectUser joinedUsers = new SelectUser();
//
//                joinedUsers.listOfJoinedUsers.add(user1);
//                joinedUsers.listOfJoinedUsers.add(user2);
//                joinedUsers.listOfJoinedUsers.add(user3);

//joinedUsers.listOfJoinedUsers.add(new ServerUser());
/*
                System.out.println("Unscrambled: ");
                for (int i = 0; i < joinedUsers.size(); i++){
                    ServerUser tempUser = (ServerUser) joinedUsers.get(i);
                    System.out.println(i + tempUser.getUserName());
                }

                System.out.println("Scrambled: ");
                //Scramble users
                joinedUsers.scramblePlayers();
                for (int i = 0; i < joinedUsers.size(); i++){
                    ServerUser tempUser = (ServerUser) joinedUsers.get(i);
                    System.out.println(i + tempUser.getUserName());
                }

                System.out.println("Switched to last: ");
                //Sets the current first user to last
                joinedUsers.switchToLast();
                for (int i = 0; i < joinedUsers.size(); i++){
                    ServerUser tempUser = (ServerUser) joinedUsers.get(i);
                    System.out.println(i + tempUser.getUserName());
                }*/

// Join test kode

/*if (thisUserNumber == 0) { // change to take the first user in the queue
                        // run the cardCzars perspective.
                        game.cardCzarFlow(connectToClient, tempUser.getIpName() + " thread", tempUser, prompt);
                    } else {
                        // run the other players' perspective.
                        game.otherPlayersFlow(connectToClient, tempUser.getIpName() + " thread", tempUser, prompt);
                    }*/