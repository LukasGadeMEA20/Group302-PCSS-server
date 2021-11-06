package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

public class GameFlowRunnable implements Runnable {
    // Identification it gets from the main thread
    Socket connectToClient = null;
    String name = "";
    String userID;
    int thisUserNumber;

    // The user information of this thread.
    ServerUser user;

    // Static boolean that can tell all the threads that a choice has been made
    static boolean choiceMade;

    // Static that tells each user what the current state of the lobby is.
    static int state = 0;
    // List of all the users.
    static PlayerQueue<ServerUser> joinedUsers = new PlayerQueue<>();
    // Static used to tell the client how many players are in the lobby.
    static int clientNo;
    // The current prompt, also as a static as it is shared between each of the users.
    static ServerPrompt prompt = new ServerPrompt(joinedUsers.getSize());

    // Static booleans for telling each user the current state of the game.
    static boolean roundRunning = true;
    static boolean gameRunning = true;

    // Static booleans that decides how to win and who wins.
    static int winRequirement = 5;
    static ServerUser winnerUser;

    // Input and output stream named conveniently for use.
    DataInputStream fromClient;
    DataOutputStream toClient;

    GameFlowRunnable(Socket _connectToClient, String _name, String _userID, int _thisUserNumber) {
        // Information it gets from the user once it is connected
        connectToClient = _connectToClient;
        name = _name;
        userID = _userID;
        thisUserNumber = _thisUserNumber;

        // Uses the information from the connected user and creates a user object
        user = new ServerUser(connectToClient.getInetAddress().getHostName(), _thisUserNumber, _userID);
        joinedUsers.queue(user);

        // Adds 1 to the number of clients, as a new player has joined.
        clientNo++;
    }

    // Code that gets run when the thread starts.
    @Override
    public void run() {
        // Prints out in the server code that a client joined the server.
        System.out.println("Connected to a client at " + new Date() + '\n');
        // ServerUser tempUser = (ServerUser) joinedUsers.get(thisUserNumber); // Have to have this temporary stand in to cast to server uwuser.

        // Try to connect the server input and output stream to the client.
        try {
            fromClient = new DataInputStream(connectToClient.getInputStream());
            toClient = new DataOutputStream(connectToClient.getOutputStream());

            // Reads the username from the client and sets the threads users username to it.
            String userName = fromClient.readUTF();
            user.setUserName(userName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Check if the lobby is still running.
        boolean lobbyRunning = true;
        while (lobbyRunning) {
            // We use a switch-case to determine which part of the program the game is in.
            // - case 0 is lobby
            // - case 1 is game
            // - case 2 is disconnecting
            switch (state) {
                case 0:
                    // runs the lobby code
                    lobbyFlow();
                    break;
                case 1:
                    // runs the game code
                    gameFlow();
                    break;
                case 2:
                    // Ends the game.
                    endOfGame();
                    lobbyRunning = false;
                    break;
            }
        }
    }

    public void lobbyFlow() {
        // Checks whether they are the first player or not.
        // This is done so the first player is the one who can initialize and start the game, while the others just wait.
        if (joinedUsers.getUsersPosition(user) == 0) {
            firstPlayer();
        } else {
            otherPlayers();
        }
    }

    // The first player code, who is responsible for starting the game
    // We also set it so the first player sets the prompt and loads the file - when they start the game.
    public void firstPlayer() {
        try {
            toClient.writeInt(0); // we write the client an int, which is used to set the state of the client.
            int confirm = fromClient.readInt(); // Reads a confirmation integer from the client
            // This is used to update whether the game started or it has to update the list of players

            // When the confirmation integer is 0, meaning the game started, it will:
            if (confirm == 0) {
                prompt.setNumberOfUsers(clientNo);  // Tell the prompt instance how many clients have joined, used for checking answers
                prompt.readFile();                  // Read the file with all the prompts
                prompt.choosePrompt();              // Randomly choose a prompt
                state = 1;                          // Sets the state to 1, changing the flow to game flow
                // This was used for testing purposes to test alone.
                // Can be used as a warning to the user to say "hey too few players", but that was not implemented, due to time constraints.
                /*if (clientNo == 1) {
                    prompt.addUserAnswer(new UserAnswer(user, "testResponse1", true));
                    prompt.addUserAnswer(new UserAnswer(user, "testResponse2", true));
                    prompt.addUserAnswer(new UserAnswer(user, "testResponse3", true));
                    prompt.addUserAnswer(new UserAnswer(user, "testResponse4", true));
                }*/
                // Else if the confirmation integer is 1, meaning it has to update how many members are in the lobby visually, it will:
            } else if (confirm == 1) {
                // Send the amount of users joined to the client
                toClient.writeInt(clientNo);
                // Then it will send each username connected to the client
                for (int i = 0; i < clientNo; i++) {
                    ServerUser tempUser = (ServerUser) joinedUsers.get(i);
                    toClient.writeUTF(tempUser.getUserName());
                }
                // On the client, a corresponding for loop is set up to add the usernames to a listview.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void otherPlayers() {
        try {
            toClient.writeInt(1);
            toClient.writeInt(clientNo);
            for (int i = 0; i < clientNo; i++) {
                ServerUser tempUser = (ServerUser) joinedUsers.get(i);
                toClient.writeUTF(tempUser.getUserName());
            }
            Thread.sleep(500);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void gameFlow() {
        // Check if the game is still going.
        while (gameRunning) {
            System.out.println("User number " + thisUserNumber + " user ID " + user.getUserName());
            if (joinedUsers.getUsersPosition(user) == 0) {
                cardCzarFlow();

                System.out.println("HELLO");
                if (nextRound()) {
                    System.out.println("test");
                    resetRound();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                    roundRunning = true;
                }

                if (gameFinished()) {
                    // go to end screen.
                    state = 2;
                    roundRunning = false;
                    gameRunning = false;
                }
            } else {
                writeToPromptFlow();
            }
        }
    }

    //Checks if the game should go to the next round, stop or wait for the card czar
    public boolean nextRound() {
        // Goes to next round
        boolean nextRound = true;

        // Unless the card czar has NOT made a choice, it will not go to the next round
        if (!choiceMade) {
            return false;
        }

        // Checks if any of the users has the right amount of points to win. ...
        for (int i = 0; i < joinedUsers.getSize(); i++) {
            ServerUser tempUser = (ServerUser) joinedUsers.get(i); // As the PlayerQueue was designed with all objects in mind, we cast to a ServerUser.
            // Checks if that user has points above or equal to the requirement
            if (tempUser.getPoints() >= winRequirement) {
                nextRound = false; // Tells the program it does not need to go to the next round
                winnerUser = tempUser; // Sets the winning user to be the current player, who reaches the requirement.
                break; // breaks the for loop.
            }
        }

        // Returns true by standard, meaning it should go to the next round.
        // Returns false if the player has not made a choice, or if it should not go to the next round.
        return nextRound;
    }

    // Method for checking if the game is finished or not.
    // This code could be heavily optimized, as we do the same procedure as above.
    // There is simply no need for half of the code here, but we did not have time to change it with testing.
    public boolean gameFinished() {
        boolean gameFinished = false; // By standard the game is not finished
        // We do the same for loop as above, for some reason.
        // We do not know why we did not just check for a boolean, but again, no time to change it and test it.
        for (int i = 0; i < joinedUsers.getSize(); i++) {
            ServerUser tempUser = (ServerUser) joinedUsers.get(i);
            if (tempUser.getPoints() >= winRequirement) {
                gameFinished = true;
                winnerUser = tempUser;
                break;
            }
        }

        // True = game is finished
        // False = game is not finished
        return gameFinished;
    }

    public void resetRound() {
        prompt.clearUserAnswers();
        prompt.choosePrompt();
        joinedUsers.switchToLast();
        prompt.setAllReady(false);
        roundRunning = false;
    }

    public void cardCzarFlow() {
        try {

            //DataOutputStream toFile = new DataOutputStream(new FileOutputStream(user.getIpName()+".txt"));

            //while(connected){
            toClient.writeInt(2);
            toClient.writeUTF(prompt.getPrompt());

            while (!prompt.getAllReady()) {
                toClient.writeBoolean(false);
                prompt.checkAllReady();
                Thread.sleep(500);
            }
            toClient.writeBoolean(true);

            toClient.writeInt(prompt.getUserAnswers().size());
            String userAnswersString = "Please choose the answer which you find the funniest!";
            for (int i = 0; i < prompt.getUserAnswers().size(); i++) {
                toClient.writeUTF(prompt.getUserAnswerAtPoint(i).getUserAnswer());
                //userAnswersString += "\n\t" + i + " - for the answer " + prompt.getUserAnswerAtPoint(i).getUserAnswer();
            }

            //toClient.writeUTF(userAnswersString);
            cardCzarWinnerChoice(fromClient.readInt());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void cardCzarWinnerChoice(int choice) {
        try {
            int cardCzarChoice = choice;
            if (cardCzarChoice > -1) {
                choiceMade = true;
                prompt.setWinner(cardCzarChoice);
                toClient.writeUTF(prompt.getWinner());
                prompt.getUserAnswerAtPoint(cardCzarChoice).getUser().delegatePoint();
                Thread.sleep(500);
            }
        } catch (IOException | InterruptedException e/*| InterruptedException e*/) {
            e.printStackTrace();
        }

    }

    public void writeToPromptFlow() {
        try {
            DataInputStream fromClient = new DataInputStream(connectToClient.getInputStream());
            DataOutputStream toClient = new DataOutputStream(connectToClient.getOutputStream());

            toClient.writeInt(3);
            toClient.writeUTF(prompt.getPrompt());
            String userAnswer = fromClient.readUTF();

            if (!userAnswer.equals("")) {
                prompt.addUserAnswer(new UserAnswer(user, userAnswer, true));
                while (roundRunning) {
                    Thread.sleep(500);
                }
                toClient.writeUTF(prompt.getWinner());
            }
            Thread.sleep(500);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void endOfGame() {
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