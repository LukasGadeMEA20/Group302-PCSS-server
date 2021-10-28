package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
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

    public void cardCzarFlow(Socket _connectToClient, String _name, ServerUser _currentUser, ServerPrompt _prompt){
        new Thread(
                new CardCzarRunnable(_connectToClient, _name, _currentUser, _prompt)
        ).start();
    }

    public void otherPlayersFlow(Socket _connectToClient, String _name, ServerUser _currentUser, ServerPrompt _prompt){
        new Thread(
                new WritePromptRunnable(_connectToClient, _name, _currentUser, _prompt)
        ).start();
    }

    public boolean nextRound(){
        return true;
    }

    public boolean gameFinished(){
        return true;
    }

    @Override
    public void run() {

        // ServerUser tempUser = (ServerUser) joinedUsers.get(thisUserNumber); // Have to have this temporary stand in to cast to server uwuser.

        // Check if the lobby is still running.
        System.out.println(name+ " 1");
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
                    // End of the game
                    break;
            }
            System.out.println(state);
        }
    }

    public void lobbyFlow() {
        System.out.println("Connected to a client at " + new Date() + '\n');
        try{
            fromClient = new DataInputStream(connectToClient.getInputStream());
            toClient = new DataOutputStream(connectToClient.getOutputStream());

            // Do so the users can connect here.
            if(clientNo > 8){
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
            if (confirm == 0){
                state=1;
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void otherPlayers(){
        try {
            toClient.writeInt(1);
            Thread.sleep(1000);
        } catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
    }

    public void gameFlow(){
        prompt.setNumberOfUsers(clientNo);
        prompt.readFile();
        prompt.choosePrompt();
        // Check if the game is still going.
        boolean gameRunning = true;
        while (gameRunning) {
            System.out.println("YEP WORKS" + "TUN "+ thisUserNumber + " tempUser " + user.getUserID());
            if(joinedUsers.getUsersPosition(user).getUserID() == 0){
                cardCzarFlow();
            } else {
                writeToPromptFlow();
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

            // Check if the round is still going
            boolean roundRunning = true;
            while(roundRunning) {
                if (nextRound()) {
                    cardCzarWinnerChoice();//DELEGATE POINTS
                    joinedUsers.switchToLast();
                } else if (gameFinished()) {
                    // go to end screen.
                    state = 2;
                    gameRunning = false;
                }
            }
        }
    }

    public void cardCzarWinnerChoice(){
        try {
            System.out.println("KØRER CARDCZAR WINNER CHOICE");
            int cardCzarChoice = fromClient.readInt();
            System.out.println("FIK INT FRA SERVER OMEGAEZ");
            prompt.getUserAnswerAtPoint(cardCzarChoice).getUser().delegatePoint();
            System.out.println("CARDZAR WINNER WON: "+prompt.getUserAnswerAtPoint(cardCzarChoice).getUser().getPoints()+" POINTS");
        }catch (IOException e/*| InterruptedException e*/) {
            e.printStackTrace();
        }

    }
    public void cardCzarFlow(){
        prompt.readFile();
        prompt.choosePrompt();
        try{

            //DataOutputStream toFile = new DataOutputStream(new FileOutputStream(user.getIpName()+".txt"));

            while(connected){
                toClient.writeInt(2);
                toClient.writeUTF("Please wait while the other users write an answer for the prompt: "+prompt.getPrompt());

                while(!prompt.getAllReady()){
                    prompt.checkAllReady();
                    Thread.sleep(2000);
                }

                String userAnswersString = "Please choose the answer which you find the funniest!";
                for(int i = 0; i<prompt.getUserAnswers().size();i++){
                    userAnswersString += "\n\t"+i + " - for the answer " + prompt.getUserAnswerAtPoint(i);
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
            }
        } catch (IOException e/*| InterruptedException e*/) {
            e.printStackTrace();
        } catch( InterruptedException e){
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

            
            while(connected){
                toClient.writeInt(3);
                toClient.writeUTF(prompt.getPrompt());
                String userAnswer = fromClient.readUTF();
                prompt.addUserAnswer(new UserAnswer(user, userAnswer, true));
                System.out.println(prompt.getUserAnswerAtPoint(0));

                /*toClient.writeBoolean(true);
                String userAnswer = fromClient.readUTF();
                prompt.addUserAnswer(new UserAnswer(user, userAnswer, true));
                toClient.writeUTF(prompt.getUserAnswerAtPoint(user.getUserID()).getUserAnswer());
                System.out.println(prompt.getUserAnswerAtPoint(user.getUserID()).getUserAnswer());*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
