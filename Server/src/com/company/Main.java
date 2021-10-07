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
                ServerSocket serverSocket = new ServerSocket(12345);
                System.out.println("Server started at " + new Date() + '\n');

                ServerUser[] listOfUsers = new ServerUser[]{new ServerUser("blank")};
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
                            new WorkerRunnable(connectToClient, "Multithreadded server", inetAddress.getHostName())
                    ).start();

                    DataInputStream fromFile = new DataInputStream(new FileInputStream(inetAddress.getHostName()+".txt"));
                    listOfUsers[thisUserNumber] = new ServerUser(fromFile.readUTF());
                    System.out.println(thisUserNumber);
                }
            } catch (IOException e){
                System.err.println(e);
            }
        }).start();
    }
}
