package com.company;

import java.net.Socket;

public class GameFlow {
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
}
