package com.company;

import java.util.LinkedList;

public class PlayerQueue<E> extends LinkedList {
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

    @Override
    public String toString() {
        return super.toString();
    }

    public int getSize(){
        return size();
    }
}
