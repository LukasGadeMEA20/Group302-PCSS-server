package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {
    static int port = 0302;

    public static void main(String[] args) {

        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Server started at: " + new Date() + '\n');

            Socket socket = server.accept();
            System.out.println("Connected to client");

            InputStream input = socket.getInputStream(); //Received from the client
            OutputStream output = socket.getOutputStream(); //Send to the client

            //DATA INPUT/OUTPUT
            //DataInputStream input = new DataInputStream(socket.getInputStream());
            //DataOutputStream output = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server Socket fail!");
        }
    }
}
