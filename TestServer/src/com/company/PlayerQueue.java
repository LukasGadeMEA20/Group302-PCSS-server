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
}
