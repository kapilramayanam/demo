package com.example.demo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Player implements Subject {
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

    //If a penguin is completely isolated, returns true. Else, returns false
    public ArrayList<Penguin> penguinIsolation() {
        if(penguins.isEmpty()){return penguins;}
        int[] desired = new int[3];
        final int[][] directions = penguins.get(0).directions;
        ArrayList<Penguin> removedPenguins = new ArrayList<>();
        for(Penguin p : penguins) {
            boolean removed = true;
            for(int[] i : directions) {
                desired[0] = p.location[0] + i[0];
                desired[1] = p.location[1] + i[1];
                desired[2] = p.location[2] + i[2];
                if(p.bm.validMove(p, desired)) {
                    removed = false;
                    break;
                }
            }
            if(removed)
                removedPenguins.add(p);
        }
        //penguins.get(0).bm.activePenguins.removeAll(removedPenguins);
        //penguins.removeAll(removedPenguins);
        for(Penguin p : removedPenguins) {
            System.out.println("\tPlayer: Remove request: " + Arrays.toString(p.location));
        }
        return removedPenguins;
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
