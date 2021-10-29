package com.company;

import java.util.Collections;
import java.util.LinkedList;

public class PlayerQueue<E> extends LinkedList<Object> {
    public void queue(E e){
        add(e);
    }

    public Object dequeue(){
        return removeFirst();
    }

    @Override
    public Object getFirst() {
        return super.getFirst();
    }

    public Object get(int _i){
        return super.get(_i);
    }

    @Override
    public String toString() {
        return super.toString();
    }

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

    public int getUsersPosition(ServerUser user){
        int intToReturn = 0;
        for(int i = 0; i <getSize();i++){
            ServerUser tempUser = (ServerUser) get(i);
            if(user.getIpName().equals(tempUser.getIpName())){
                intToReturn=i;
                break;
            }
        }
        return intToReturn;
    }
}
