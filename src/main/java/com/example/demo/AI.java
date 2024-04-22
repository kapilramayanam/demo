package com.example.demo;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
public class AI {
    Random rand = new Random();
    ArrayList<Penguin> TEMP = new ArrayList<>();

    //NOTE: Does not give penguin the amount of fish as of 4/21/24
    //NOTE 4/22/24: As written so far, penguin does not need the amount of fish
    //That should be handled in the Person class?
    public void randomMoveOneSpace() {
        Penguin p = TEMP.get(rand.nextInt(0,TEMP.size()));
        ArrayList<int[]> validDirections = new ArrayList<>();
        int[] possibleLocation = new int[3];
        for (int[] i : p.directions) {
            for(int j = 0; j < 3; j++) {
                possibleLocation[j] = p.location[j] + i[j];
            }
            if(p.bm.validMove(p, possibleLocation)) {
                validDirections.add(i);
            }
        }
        //Guaranteed to have at least one value or the penguin would have been removed
        //from isolation, but error fail-safe
        if(validDirections.size() != 0) {
            possibleLocation = validDirections.get(rand.nextInt(0, validDirections.size()));
            for (int i = 0; i < 3; i++) {
                possibleLocation[i] += p.location[i];
            }
            p.setLocation(possibleLocation);
        }
    }
}