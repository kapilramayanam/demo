package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BoardAndMovement {
    public static ArrayList<int[]> activeSpots = new ArrayList<>();
    public static ArrayList<Penguin> activePenguins = new ArrayList<>();

    public static void main(String[] args) {
        BoardAndMovement bm = new BoardAndMovement();
        bm.setStartSpots(bm.numbers());

        activePenguins.add(new Penguin(bm));
        activePenguins.get(0).setLocation(new int[]{1,8,8});
        activePenguins.get(0).setLocation(new int[]{2,7,8});
        activePenguins.add(new Penguin(bm));
        activePenguins.get(1).setLocation(new int[]{1,8,8});
        /*for (Penguin i : activePenguins) {
            System.out.println("*" + Arrays.toString(i.location));
        }

        activePenguins.get(1).setLocation(new int[]{2,7,8});
        for (Penguin i : activePenguins) {
            System.out.println("*" + Arrays.toString(i.location));
        }*/
        //activePenguins.get(1).setLocation(new int[]{});
        //activePenguins.get(0).setLocation(new int[]{2,7,8});

        //System.out.println(Arrays.toString(activePenguins.get(0).location) + " equals: " +
        //bm.match(activePenguins.get(0).location));
        //System.out.println(bm.validMove(activePenguins.get(0), new int[]{2,7,8}, new int[]{1,-1,0}));


    }

    public void setPenguins(ArrayList<Penguin> p) {
        activePenguins = p;
    }

    //NOTE: TESTING METHOD
    //Used to test the setStartSpots method
    public List<Integer> numbers() {
        List<Integer> fishTiles = new ArrayList<>();
        fishTiles.clear();
        for (int i = 0; i < 30; i++) fishTiles.add(1);
        for (int i = 0; i < 20; i++) fishTiles.add(2);
        for (int i = 0; i < 10; i++) fishTiles.add(3);
        Collections.shuffle(fishTiles);
        return fishTiles;
    }
    /*
        //Commented out 4/22/24
        //Should be unnecessary
        public void setStartSpots() {
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
        } */
    public void setStartSpots(List<Integer> fishTiles) {
        activeSpots.clear();
        int q = 5;
        int r = 1;
        int s = 11;
        int fishIndex = 0;
        for (int row = 0; row < 8; row++) {
            int numColsInRow = row % 2 == 0 ? 7 : 8;
            for (int col = 0; col < numColsInRow; col++) {
                activeSpots.add(new int[]{q,r,s,fishTiles.get(fishIndex)});
                q++;
                s--;
                fishIndex++;
            }
            for (int col = 0; col < numColsInRow; col++) {
                q--;
                s++;
            }
            if(numColsInRow == 7) {
                q--;
            } else {
                s--;
            }
            r++;
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
        if(activeSpots.removeIf(a -> Arrays.equals(a, 0, 3, oldLocation, 0,3)))
            System.out.println(Arrays.toString(oldLocation) + " removed successfully");
        else
            System.out.println(Arrays.toString(oldLocation) + " is not in the array");
    }


    //NOTE: Only works if you do not hand the actual memory address from activeSpots
    //If current's address is in activeSpots, current's values will change, messing up the board
    public boolean validMove(Penguin p, int[] desired) {
        //System.out.println("~" + p + "~");
        int[] current = Arrays.copyOf(p.location, p.location.length);
        int[] direction = getDirection(p.location, desired);
        if(direction == null)
            return false;

        while (activeSpots.stream().anyMatch(a -> Arrays.equals(a, 0, 3, current, 0,3))) {//Code via https://stackoverflow.com/questions/4849051/using-contains-on-an-arraylist-with-integer-arrays
            //System.out.println(Arrays.toString(current));

            //if the space is already occupied by a different penguin
            if(!(activePenguins.stream().filter(a -> Arrays.equals(a.location, 0, 3, current, 0,3)).allMatch(a -> a == p)))
                return false;

            if(Arrays.equals(current, 0, 3, desired, 0,3)) {
                return true;
            }
            else {
                for(int i = 0; i < 3; i++) {
                    current[i] += direction[i];
                }
            }
        }
        //System.out.println("Exitted loop");
        return false;
    }

}