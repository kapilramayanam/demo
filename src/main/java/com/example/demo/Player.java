package com.example.demo;

import java.util.ArrayList;

public class Player implements Subject{
    private static int score;
    private Subject subject;
    protected ArrayList<Penguin> penguins;
    private static ArrayList<Observer> observers;
    private static boolean changed;
    private final Object MUTEX = new Object();

    public Player(){
        score = 0;
        penguins = new ArrayList<>();
    }

    public void setScore(int scr){
        score = scr;
    }

    public int getScore(){
        return score;
    }

    public void addPenguinPlayer(Penguin p) {
        penguins.add(p);
        //p.bm.addPenguins(p);
    }

    public ArrayList<Penguin> getPenguins(){return penguins;}

    public void removePenguin(Penguin p) {
        penguins.remove(p);
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
