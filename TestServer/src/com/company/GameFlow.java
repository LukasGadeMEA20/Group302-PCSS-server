package com.company;

import java.net.Socket;

public class GameFlow {
    public void cardCzarFlow(){

    }

    public void otherPlayersFlow(Socket _connectToClient, String _name, ServerUser _currentUser, ServerPrompt _prompt){
        new Thread(
                new WritePromptRunnable(_connectToClient, _name, _currentUser, _prompt)
        ).start();
    }
}
