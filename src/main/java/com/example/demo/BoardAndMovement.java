package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BoardAndMovement {
    public static ArrayList<int[]> activeSpots = new ArrayList<>();
    private static ArrayList<Penguin> activePenguins;

    public BoardAndMovement(ArrayList<Penguin> penguins) {
        activePenguins = penguins;
    }

    public void setStartSpots(){
        activeSpots.clear();
        Random rnd = new Random();
        int oneFish = 30;
        int twoFish = 20;
        int threeFish = 10;
        int q = 5;
        int r = 1;
        int s = 11;
        while(activeSpots.size() < 60){
            int fishChoice = rnd.nextInt(1,7);
            if((fishChoice == 1 || fishChoice == 2 || fishChoice == 3)&& oneFish > 0){
                oneFish--;
                activeSpots.add(new int[] {q,r,s,1});
            } else if((fishChoice == 4 || fishChoice == 5) && twoFish > 0) {
                twoFish--;
                activeSpots.add(new int[] {q,r,s,2});
            } else if(fishChoice == 6 && threeFish > 0) {
                threeFish--;
                activeSpots.add(new int[] {q,r,s,3});
            } else {
                continue;
            }

            //NOTE: Just hard-coding all the new lines
            //There is probably a more efficient way of doing this
            if(q == 11 && s == 5){
                q = 4;
                s = 11;
                r++;
            } else if(q == 11 && s == 4){
                q = 4;
                s = 10;
                r++;
            } else if(q == 10 && s == 4){
                q = 3;
                s = 10;
                r++;
            } else if(q == 10 && s == 3){
                q = 3;
                s = 9;
                r++;
            } else if(q == 9 && s == 3){
                q = 2;
                s = 9;
                r++;
            } else if(q == 9 && s == 2){
                q = 2;
                s = 8;
                r++;
            } else if(q == 8 && s == 2){
                q = 1;
                s = 8;
                r++;
            } else {
                q++;
                s--;
            }
        }
    }

    //takes two int[] of 3 values and returns an int[] of the direction between the two points
    //returns null if the desired cannot be accessed in a straight line
    public int[] getDirection(int[] current, int[] desired) {
        //each array contains 3 values representing q,r, and s
        //q is the movement \, r is the movement -, and s is the movement /

        int q = desired[0] - current[0];
        int r = desired[1] - current[1];
        int s = desired[2] - current[2];

        if(q == 0 && r > 0 && s < 0)
            return new int[]{0,1,-1};
        else if(q == 0 && r < 0 && s > 0)
            return new int[]{0,-1,1};
        else if(q < 0 && r == 0 && s > 0)
            return new int[]{-1,0,1};
        else if(q > 0 && r == 0 && s < 0)
            return new int[]{1,0,-1};
        else if(q > 0 && r < 0 && s == 0)
            return new int[]{1,-1,0};
        else if(q < 0 && r > 0 && s == 0)
            return new int[]{-1,1,0};
        else
            return null;    //Not a straight path
    }

    public void removeSpot(int[] oldLocation){
        for (int[] tempArray : activeSpots) {
            if(Arrays.equals(Arrays.copyOf(tempArray,3), oldLocation))
            {
                activeSpots.remove(tempArray);
                System.out.println(Arrays.toString(oldLocation) + " removed successfully");
                break;
            }
            else if(activeSpots.indexOf(tempArray) == activeSpots.size()-1){
                System.out.println(Arrays.toString(oldLocation) + " is not in the array");
            }
        }
    }


    //NOTE: Only works if you do not hand the actual memory address from activeSpots
    //If current's address is in activeSpots, current's values will change, messing up the board
    public boolean validMove(int[] current, int[] desired, int[] direction) {
        //if the space is already occupied
        for (Penguin p : activePenguins) {
            if(Arrays.equals(Arrays.copyOf(p.location,3),current))
                return false;
        }

        //Only continue if activeSpots contains the equivelent array
        for (int[] tempArray : activeSpots) {
            if(Arrays.equals(Arrays.copyOf(tempArray,3), current)) {
                break;
            }
            else if(tempArray.equals(activeSpots.get(activeSpots.size()-1)))
            {
                return false;
            }
        }

        if(Arrays.equals(current,desired))
        {
            return true;
        }
        else {
            for(int i = 0; i < 3; i++) {
                current[i] += direction[i];
            }
            return validMove(current, desired, direction);
        }
    }
}