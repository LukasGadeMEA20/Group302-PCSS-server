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

                //Read the promptsFile.txt file and runs the readFile function
                GameFlow game = new GameFlow();
                ServerPrompt prompt = new ServerPrompt();
                int state = 0;
                boolean acceptingUsers = true;
                prompt.readFile();
                prompt.choosePrompt();

                PlayerQueue<ServerUser> joinedUsers = new PlayerQueue<>();
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

                int clientNo = 0;

                while(true) {
                    Socket connectToClient = serverSocket.accept();
                    int thisUserNumber = clientNo;
                    clientNo++;

                    System.out.println("Starting thread for client " + clientNo + " at " + new Date()+'\n');
                    InetAddress inetAddress = connectToClient.getInetAddress();
                    System.out.println("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + '\n');
                    System.out.println("Client " + clientNo + "'s host address is " + inetAddress.getHostAddress() + '\n');

                    ServerUser currentUser = new ServerUser(thisUserNumber, inetAddress.getHostName());
                    joinedUsers.queue(currentUser);

                    ServerUser tempUser = (ServerUser) joinedUsers.get(thisUserNumber); // Have to have this temporary stand in to cast to server uwuser.

                    if (thisUserNumber == 0) { // change to take the first user in the queue
                        // run the cardCzars perspective.
                        game.cardCzarFlow(connectToClient, tempUser.getIpName() + " thread", tempUser, prompt);
                    } else {
                        // run the other players' perspective.
                        game.otherPlayersFlow(connectToClient, tempUser.getIpName() + " thread", tempUser, prompt);
                    }

                    // Check if the lobby is still running.
                    boolean lobbyRunning = false;
                    while(lobbyRunning) {
                        switch (state) {
                            case 0:

                                break;
                            case 1:
                                prompt.setNumberOfUsers(clientNo);
                                // Check if the game is still going.
                                boolean gameRunning = true;
                                while (gameRunning) {
                                    // Change to running the cardCzarFlow for first user in the player queue
                                    // then a for loop for the other players. Much better and does not require using the user number counter.
                                    for(int i = 0; i < joinedUsers.getSize(); i++) {
                                        //ServerUser tempUser = (ServerUser) joinedUsers.get(i); // Have to have this temporary stand in to cast to server uwuser.
                                        if (i == 0) { // change to take the first user in the queue
                                            // run the cardCzars perspective.
                                            game.cardCzarFlow(connectToClient, tempUser.getIpName() + " thread", tempUser, prompt);
                                        } else {
                                            // run the other players' perspective.
                                            game.otherPlayersFlow(connectToClient, tempUser.getIpName() + " thread", tempUser, prompt);
                                        }
                                    }

                                    // Check if the round is still going
                                    boolean roundRunning = true;
                                    while(roundRunning) {
                                        if (game.nextRound()) {
                                            joinedUsers.switchToLast();

                                            // Delegate point to winner here (?)
                                        } else if (game.gameFinished()) {
                                            // go to end screen.
                                            state = 2;
                                            gameRunning = false;
                                        }
                                    }
                                }
                                break;
                            case 2:
                                break;
                        }
                    }

                    new Thread(
                            new WorkerRunnable(connectToClient, "Multithreadded server", inetAddress.getHostAddress())
                    ).start();

                    Thread.sleep(5000);
                    DataInputStream fromFile = new DataInputStream(new FileInputStream(inetAddress.getHostAddress()+".txt"));
                    //listOfUsers.add(new ServerUser(fromFile.readUTF()));
                    System.out.println(thisUserNumber);

                }
            } catch (IOException e){
                System.err.println(e);
            } catch(InterruptedException e){
                System.err.println(e);
            }
        }).start();
    }
}
