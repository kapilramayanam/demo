package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Penguin implements Observer{
    //private static int[] position = {2,8,7};
    private static BoardAndMovement bm;
    private static Subject topic;
    private static int currentScore;
    int[] location = new int[4];
    public Penguin(BoardAndMovement bam) {
        bm = bam;
        this.currentScore = 0;
    }

    //If a penguin is completely isolated, returns true. Else, returns false
    public boolean penguinIsolation() {
        int[][] directions = {{0,-1,1},{0,1,-1},{-1,0,1},{1,0,-1},{1,-1,0},{-1,1,0}};
        int[] desired = new int[3];
        for(int[] i : directions) {
            desired[0] = location[0] + i[0];
            desired[1] = location[1] + i[1];
            desired[2] = location[2] + i[2];
            if(bm.validMove(location, desired, i) == true) {
                return false;
            }
        }
        return true;
    }

    public void setLocation(int[] newLocation) {
        if(bm.validMove(location, newLocation, newLocation)) {
            bm.removeSpot(location);
            //TO DO: Add any points? Or are we handling that fully front end?
            location = newLocation;
        }
    }

    @Override
    public void update() {
        this.currentScore = (int)topic.getUpdate(this);
    }

    public int getCurrentScore(){
        return this.currentScore;
    }

    @Override
    public void setSubject(Subject sub) {
        this.topic = sub;
    }
}