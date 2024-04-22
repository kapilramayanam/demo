package com.example.demo;

import java.util.ArrayList;

public class Player implements Subject{
    private static int score;
    private Subject subject;
    private static ArrayList<Penguin> penguins;
    private static ArrayList<Observer> observers;
    private static boolean changed;
    private final Object MUTEX = new Object();
    public Player(int score, ArrayList<Penguin> penguins){
        this.score = score;
        this.penguins = penguins;
    }

    public void setScore(int score){
        this.score = score;
    }

    public int getScore(){
        return this.score;
    }


    @Override
    public void register(Observer obj) {
        if(obj == null) throw new NullPointerException("Null Observer");
        synchronized (MUTEX) {
            if(!observers.contains(obj)) observers.add(obj);
        }
    }

    @Override
    public void unregister(Observer obj) {
        synchronized (MUTEX) {
            observers.remove(obj);
        }
    }

    @Override
    public void notifyObservers() {
        ArrayList<Observer> observersLocal = null;
        synchronized (MUTEX) {
            if(!changed) {
                return;
            }
            observersLocal = new ArrayList<>(this.observers);
            this.changed = false;
        }
        for(Observer obj : observersLocal){
            obj.update();
        }
    }

    @Override
    public Object getUpdate(Observer obj) {
        return this.score;
    }
}
