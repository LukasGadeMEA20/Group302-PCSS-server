package com.company;

import java.util.Collections;
import java.util.LinkedList;

/*
    A custom made Queue class which extends the LinkedList class, which works with players.
    Reason for using a Queue instead of a LinkedList, is because we always wanted the first player to be the cardczar,
    meaning we could just dequeue and queue the first player to set a new card czar easily.
    
    There are also methods for scrambling the list and also for searching in the list.
*/

public class PlayerQueue<E> extends LinkedList<Object> {
    // Adds a new element
    public void queue(E e){
        add(e);
    }
    
    // Removes the first element
    public Object dequeue(){
        return removeFirst();
    }

    // Gets the first element
    @Override
    public Object getFirst() {
        return super.getFirst();
    }

    // gets a specific element
    public Object get(int _i){
        return super.get(_i);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    // Gets the size of players
    public int getSize(){
        return size();
    }

    //Scrambles the list of users into a random order
    public void scramblePlayers(){
        Collections.shuffle(this);
    }

    //Switches the first item in the array to the last item and moving the second item up
    public void switchToLast(){

        //Finds the first element in the index
        Object firstIndex = getFirst();

        //Removes the element
        this.dequeue();

        //Adds the element back, so it is last
        this.queue((E) firstIndex);

    }

    // Searches for the position of a certain player
    // Badly made code as it uses ServerUser and not e/object like the other methods, but it was done in a rush without any thoughts for optimization.
    // The code also does not tell the user if it did not find the user and returns the first user, which could be a big problem if used otherwise.
    public int getUsersPosition(ServerUser user){
        // Sets the int to return to 0, to have a starting point.
        int intToReturn = 0;
        
        // checks each member and compares the given user to the user it has reached in the list.
        for(int i = 0; i <getSize();i++){
            ServerUser tempUser = (ServerUser) get(i);
            if(user.getIpName().equals(tempUser.getIpName())){
                // once it finds the user in the list, it will set the int to return to be the point in the list, then break the loop.
                intToReturn=i;
                break;
            }
        }
        // returns it. Should be added more so it does not return 0 if it does not find the user, but whoops, no time lol.
        return intToReturn;
    }
}
