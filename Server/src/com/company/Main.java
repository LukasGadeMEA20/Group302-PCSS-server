package com.company;

import java.io.*;
import java.lang.reflect.Array;
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
                ServerSocket serverSocket = new ServerSocket(12345);
                System.out.println("Server started at " + new Date() + '\n');

                //Create a new prompt and run the choosePrompt function
                //Prompt chosenPrompt = new Prompt();
                //chosenPrompt.choosePrompt();

                //Read the promptsFile.txt file and runs the readFile function
                Prompt readPrompt = new Prompt();
                readPrompt.readFile();
                readPrompt.choosePrompt();

                //Scramble the joined users & move the first user to the last
                SelectUser joinedUsers = new SelectUser();
                joinedUsers.listOfJoinedUsers.add("User 1");
                joinedUsers.listOfJoinedUsers.add("User 2");
                joinedUsers.listOfJoinedUsers.add("User 3");
                joinedUsers.listOfJoinedUsers.add("User 4");

                System.out.println("Unscrambled: " + joinedUsers.listOfJoinedUsers);

                //Scramble users
                joinedUsers.scramblePlayers(joinedUsers.listOfJoinedUsers);
                System.out.println("Scrambled: " + joinedUsers.listOfJoinedUsers);

                //Sets the current first user to last
                joinedUsers.switchToLast();
                System.out.println("Switched to last: " + joinedUsers.listOfJoinedUsers);

                ArrayList<ServerUser> listOfUsers= new ArrayList<ServerUser>();
                //ServerUser[] listOfUsers = new ServerUser[]{new ServerUser("blank")};
                int clientNo = 0;

                while(true) {
                    Socket connectToClient = serverSocket.accept();
                    int thisUserNumber = clientNo;
                    clientNo++;

                    System.out.println("Starting thread for client " + clientNo + " at " + new Date()+'\n');
                    InetAddress inetAddress = connectToClient.getInetAddress();
                    System.out.println("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + '\n');
                    System.out.println("Client " + clientNo + "'s host address is " + inetAddress.getHostAddress() + '\n');

                    new Thread(
                            new WorkerRunnable(connectToClient, "Multithreadded server", inetAddress.getHostAddress())
                    ).start();
                    Thread.sleep(5000);
                    DataInputStream fromFile = new DataInputStream(new FileInputStream(inetAddress.getHostAddress()+".txt"));
                    listOfUsers.add(new ServerUser(fromFile.readUTF()));
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
