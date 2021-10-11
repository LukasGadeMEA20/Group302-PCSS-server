package com.company;

import java.util.ArrayList;
import java.util.Collections;

public class SelectUser {
    //ArrayList of users joined
    ArrayList<ServerUser> listOfJoinedUsers = new ArrayList<>();

    //Scrambles the list of users into a random order
    public void scramblePlayers(ArrayList<ServerUser> listOfJoinedUsers){
        Collections.shuffle(listOfJoinedUsers);
    }

    //Switches the first item in the array to the last item and moving the second item up
    public void switchToLast(){

        //Finds the first element in the index
        ServerUser firstIndex = listOfJoinedUsers.get(0);

        //Removes the element
        listOfJoinedUsers.remove(firstIndex);

        //Adds the element back, so it is last
        listOfJoinedUsers.add(firstIndex);
    }
}
