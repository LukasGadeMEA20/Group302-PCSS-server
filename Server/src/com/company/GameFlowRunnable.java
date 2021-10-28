package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class GameFlowRunnable implements Runnable {
    Socket connectToClient = null;
    String name = "";
    String userID;
    int thisUserNumber;

    ServerUser user;

    static int state = 0;
    static PlayerQueue<ServerUser> joinedUsers = new PlayerQueue<>();
    static int clientNo;
    static ServerPrompt prompt = new ServerPrompt(joinedUsers.getSize());
    static boolean acceptingUsers = true;

    static int winRequirement = 5;
    static ServerUser winnerUser;

    boolean connected = true;

    DataInputStream fromClient;
    DataOutputStream toClient;

    GameFlowRunnable(Socket _connectToClient, String _name, String _userID, int _thisUserNumber) {
        connectToClient = _connectToClient;
        name = _name;
        userID = _userID;
        thisUserNumber = _thisUserNumber;

        user = new ServerUser(_thisUserNumber, _userID);
        joinedUsers.queue(user);

        clientNo++;
    }

    public boolean nextRound(){
        boolean nextRound = true;

        for(int i = 0; i < joinedUsers.getSize(); i++){
            ServerUser tempUser = (ServerUser) joinedUsers.get(i);
            if(tempUser.getPoints() >= winRequirement){
                nextRound = false;
                winnerUser = tempUser;
                break;
            }
        }

        return nextRound;
    }

    public boolean gameFinished(){
        return true;
    }

    @Override
    public void run() {

        // ServerUser tempUser = (ServerUser) joinedUsers.get(thisUserNumber); // Have to have this temporary stand in to cast to server uwuser.

        // Check if the lobby is still running.
        boolean lobbyRunning = true;
        while(lobbyRunning) {
            switch (state) {
                case 0:
                    lobbyFlow();
                    break;
                case 1:
                    gameFlow();
                    break;
                case 2:
                    endOfGame();
                    lobbyRunning = false;
                    break;
            }
        }
    }

    public void lobbyFlow() {
        System.out.println("Connected to a client at " + new Date() + '\n');
        try{
            fromClient = new DataInputStream(connectToClient.getInputStream());
            toClient = new DataOutputStream(connectToClient.getOutputStream());

            // Do so the users can connect here.
            if(clientNo > 8) {
                acceptingUsers = false;
            } else {
                if(joinedUsers.getUsersPosition(user).getUserID() == 0){
                    firstPlayer();
                } else {
                    otherPlayers();
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void firstPlayer(){
        try {
            toClient.writeInt(0);
            int confirm = fromClient.readInt();
            if (confirm == 0) {

                prompt.setNumberOfUsers(clientNo);
                prompt.readFile();
                prompt.choosePrompt();
                state = 1;
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void otherPlayers(){
        try {
            toClient.writeInt(1);
            System.out.println(joinedUsers.toString());
            Thread.sleep(3000);
        } catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
    }

    public void gameFlow(){
        // Check if the game is still going.
        boolean gameRunning = true;
        while (gameRunning) {
            System.out.println("User number "+ thisUserNumber + " user ID " + user.getUserID());
            if(joinedUsers.getUsersPosition(user).getUserID() == 0){
                cardCzarFlow();

                cardCzarWinnerChoice();//DELEGATE POINTS

                if (nextRound()) {
                    joinedUsers.switchToLast();
                } else if (gameFinished()) {
                    // go to end screen.
                    state = 2;
                    gameRunning = false;
                }
            } else {
                writeToPromptFlow();
            }


        }
    }


    public void cardCzarFlow(){
        try{

            //DataOutputStream toFile = new DataOutputStream(new FileOutputStream(user.getIpName()+".txt"));

            //while(connected){
                toClient.writeInt(2);
                toClient.writeUTF("Please wait while the other users write an answer for the prompt: \n"+prompt.getPrompt());

                while(!prompt.getAllReady()){
                    prompt.checkAllReady();
                    Thread.sleep(2000);
                }

                String userAnswersString = "Please choose the answer which you find the funniest!";
                for(int i = 0; i<prompt.getUserAnswers().size();i++){
                    userAnswersString += "\n\t"+i + " - for the answer " + prompt.getUserAnswerAtPoint(i).getUserAnswer();
                }

                toClient.writeUTF(userAnswersString);

                /*prompt.checkAllReady();
                if(prompt.getAllReady()) {
                    toClient.writeUTF("Please choose the prompt you like the most:");
                    String promptsToPrint = "";
                    for(int i = 0; i < prompt.getUserAnswers().size(); i++){
                        promptsToPrint += i + " - " + prompt.getUserAnswerAtPoint(i).getUserAnswer()+'\n';
                    }
                    toClient.writeUTF(promptsToPrint);
                    toClient.writeBoolean(true);
                    int userAnswer = fromClient.readInt();
                    // take user choice here.
                    try {
                        prompt.getUserAnswerAtPoint(userAnswer);
                        // Delegate point
                    } catch(NullPointerException e) {

                    }
                    connected = false;
                } else {
                    Thread.sleep(2000);
                }*/
                /*String userAnswer = fromClient.readUTF();
                prompt.addUserAnswer(new UserAnswer(user, userAnswer));
                toClient.writeUTF(prompt.getUserAnswerAtPoint(user.getUserID()).getUserAnswer());
                System.out.println(prompt.getUserAnswerAtPoint(user.getUserID()).getUserAnswer());*/
            //}
        } catch (IOException e/*| InterruptedException e*/) {
            e.printStackTrace();
        } catch( InterruptedException e){
            e.printStackTrace();
        }
    }

    public void cardCzarWinnerChoice(){
        try {
            System.out.println("KØRER CARDCZAR WINNER CHOICE");
            int cardCzarChoice = fromClient.readInt();
            System.out.println("FIK INT FRA SERVER");
            prompt.getUserAnswerAtPoint(cardCzarChoice).getUser().delegatePoint();
            System.out.println("CARDZAR WINNER WON: "+prompt.getUserAnswerAtPoint(cardCzarChoice).getUser().getPoints()+" POINTS");
        }catch (IOException e/*| InterruptedException e*/) {
            e.printStackTrace();
        }

    }

    public void writeToPromptFlow(){
        try{
            System.out.println("Connected to a client at " + new Date() + '\n');
            boolean connected = true;

            DataInputStream fromClient = new DataInputStream(connectToClient.getInputStream());
            DataOutputStream toClient = new DataOutputStream(connectToClient.getOutputStream());

            //DataOutputStream toFile = new DataOutputStream(new FileOutputStream(user.getIpName()+".txt"));


            //int i = 0;
            //while(connected){
            toClient.writeInt(3);
            toClient.writeUTF(prompt.getPrompt());
            String userAnswer = fromClient.readUTF();

            if(!userAnswer.equals("")){
                prompt.addUserAnswer(new UserAnswer(user, userAnswer, true));
                System.out.println("HI"+prompt.getUserAnswerAtPoint(0));
            }


                /*toClient.writeBoolean(true);
                String userAnswer = fromClient.readUTF();
                prompt.addUserAnswer(new UserAnswer(user, userAnswer, true));
                toClient.writeUTF(prompt.getUserAnswerAtPoint(user.getUserID()).getUserAnswer());
                System.out.println(prompt.getUserAnswerAtPoint(user.getUserID()).getUserAnswer());*/
            //}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void endOfGame(){
        try {
            toClient.writeInt(4);
            toClient.writeUTF(winnerUser.getUserName() + " is the winner!");
            Thread.sleep(5000);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}


// Change to running the cardCzarFlow for first user in the player queue
// then a for loop for the other players. Much better and does not require using the user number counter.
            /*for(int i = 0; i < joinedUsers.getSize(); i++) {
                ServerUser tempUser = (ServerUser) joinedUsers.get(i); // Have to have this temporary stand in to cast to server uwuser.
                System.out.println("TUN "+ thisUserNumber + " tempUser " + tempUser.getUserID());
                if (thisUserNumber == tempUser.getUserID()) { // change to take the first user in the queue
                    // run the cardCzars perspective.
                    cardCzarFlow();
                } else {
                    // run the other players' perspective.
                    writeToPromptFlow();
                }
            }*/