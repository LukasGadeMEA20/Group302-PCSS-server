package com.company;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Main {
    static int port = 302;

    public static void main(String[] args) {

        new Thread( () ->{
            try{
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("Server started at " + new Date() + '\n');
                int clientNo = 0;

                GameFlow game = new GameFlow();
                ServerPrompt prompt = new ServerPrompt();
                int state = 1;
                boolean acceptingUsers = true;
                PlayerQueue<ServerUser> playerQueue = new PlayerQueue<>();

                while(true) {
                    Socket connectToClient = serverSocket.accept();
                    int thisUserNumber = clientNo;
                    clientNo++;

                    System.out.println("Starting thread for client " + clientNo + " at " + new Date()+'\n');
                    InetAddress inetAddress = connectToClient.getInetAddress();
                    System.out.println("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + '\n');
                    System.out.println("Client " + clientNo + "'s host address is " + inetAddress.getHostAddress() + '\n');

                    ServerUser currentUser = new ServerUser(thisUserNumber, inetAddress.getHostName());
                    playerQueue.queue(currentUser);

                    // Check if the lobby is still running.
                    boolean lobbyRunning = true;
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
                                    for(int i = 0; i < playerQueue.getSize(); i++) {
                                        ServerUser tempUser = (ServerUser) playerQueue.get(i); // Have to have this temporary stand in to cast to server uwuser.
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
                                            ServerUser tempUser = (ServerUser) playerQueue.getFirst();
                                            playerQueue.dequeue();
                                            playerQueue.queue(tempUser);
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

    public static void reset(){

    }
}
