package com.example.demo;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class AI extends Player{
    Random rand = new Random();

    //Places first unplaced penguin onto the board
    public int[] randomStart(Penguin p) {
        List<int[]> allValids;
        penguins.add(p);
        allValids = p.bm.activeSpots.stream().filter(Objects::nonNull).filter(a -> Arrays.equals(a, 3,4,new int[]{0,0,0,1},3,4)).toList();

        for (Penguin i : p.bm.activePenguins) {
            allValids = allValids.stream().filter(a -> !Arrays.equals(a, i.location)).toList();
        }

        return allValids.get(rand.nextInt(0,allValids.size()));
    }

    public int[] randomMove(Penguin p) {
        if(penguins.isEmpty()) {
            return null;
        }
        System.out.println("\tAI: Penguin chosen: " + penguins.indexOf(p));
        ArrayList<int[]> validArrayList = new ArrayList<>();
        int[] possibleLocation = new int[3];
        for (int[] i : p.directions) {
            for(int j = 0; j < 3; j++) {
                possibleLocation[j] = p.location[j] + i[j];
            }
            if(p.bm.validMove(p, possibleLocation)) {
                System.out.println("\tAI: Can move from " + Arrays.toString(p.location)
                        + " to " + Arrays.toString(possibleLocation));
                validArrayList.add(i);
            } else {
                System.out.println("\tAI: Cannot move from " + Arrays.toString(p.location) + " to " + Arrays.toString(possibleLocation));
            }
        }
        int[] direction = validArrayList.get(rand.nextInt(0,validArrayList.size()));

        if(direction == null) {return null;}

        int max;
        for (max = 1; max < 9; max++) {
            possibleLocation[0] = p.location[0] + (direction[0]*max);
            possibleLocation[1] = p.location[1] + (direction[1]*max);
            possibleLocation[2] = p.location[2] + (direction[2]*max);
            if(!p.bm.validMove(p, possibleLocation)) {
                if(max == 1)
                    System.out.println("\tAI: Error: Cannot from " + Arrays.toString(p.location) +
                            " to " + Arrays.toString(possibleLocation));
                System.out.println("\tAI: Max: " + max + "\t\t" + Arrays.toString(possibleLocation));
                break;
            }
        }

        max = rand.nextInt(1,max);
        possibleLocation[0] = p.location[0] + (direction[0]*max);
        possibleLocation[1] = p.location[1] + (direction[1]*max);
        possibleLocation[2] = p.location[2] + (direction[2]*max);

        int[] chosen = null;
        for(int[] is : p.bm.activeSpots) {
            if(is == null)
                continue;
            else if (Arrays.equals(is, 0,3, possibleLocation,0,3)) {
                chosen = is;
                System.out.println("\tAI: Found in activeSpots: " + Arrays.toString(chosen));
                break;
            } else if(p.bm.activeSpots.indexOf(is) == p.bm.activeSpots.size() - 1) {
                System.out.println("\tAI: " + Arrays.toString(possibleLocation) + " not found");
            }
        }
        return chosen;
    }
}
