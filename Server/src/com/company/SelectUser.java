package com.company;


import java.util.ArrayList;
import java.util.Collections;

public class SelectUser {
    //ArrayList of users joined
    ArrayList<String> listOfJoinedUsers = new ArrayList<>();

    //Scrambles the list of users into a random order
    public void scramblePlayers(ArrayList<String> listOfJoinedUsers){
        Collections.shuffle(listOfJoinedUsers);
    }

    //Switches the first item in the array to the last item and moving the second item up
    public void switchToLast(){
        //int indexNum = listOfJoinedUsers.indexOf(currentUser);
        //listOfJoinedUsers.remove(indexNum);
        //listOfJoinedUsers.add()

        //Finds the first element in the index
        String firstIndex = listOfJoinedUsers.get(0);

        //Removes the element
        listOfJoinedUsers.remove(firstIndex);

        //Adds the element back;
        listOfJoinedUsers.add(firstIndex);

//        for (int i = 0; i < listOfJoinedUsers.size(); i++){
//            String item = listOfJoinedUsers.get(i);
//
//
//            listOfJoinedUsers.remove(i--);
//            listOfJoinedUsers.add(item);
//            System.out.println("Hej" + i);
//        }
    }
}
