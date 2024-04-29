package com.example.demo;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Objects;

public class AI extends Player{
    Random rand = new Random();

    //Places first unplaced penguin onto the board
    public int[] randomStart(Penguin p) {
        List<int[]> allValids;
        penguins.add(p);
        allValids = p.bm.activeSpots.stream().filter(Objects::nonNull).filter(a -> Arrays.equals(a, 3,4,new int[]{0,0,0,1},3,4)).toList();
        //allValids = new ArrayList<>(allValids);
        for (Penguin i : p.bm.activePenguins) {
            allValids = allValids.stream().filter(a -> !Arrays.equals(a, i.location)).toList();
        }

        return allValids.get(rand.nextInt(0,allValids.size()));
    }



    //TWO PROBLEMS:
    //1) Button thinks it needs to set the background as the AI image. It does not appear that the penguin is actually there backend
    //2) Max movement still isn't returning something right. It seems to accurately calculate the max distance,
    //But doesn't know how to convert it to the usable location

    //PROBLEM 2 SOLVED?
    public int[] randomMove(Penguin p) {
        //Penguin p = getPenguins().get(rand.nextInt(0,getPenguins().size()));
        if(p.penguinIsolation()){
            return null;
        }
        System.out.println("Penguin chosen: " + penguins.indexOf(p));
        ArrayList<int[]> validArrayList = new ArrayList<>();
        int[] possibleLocation = new int[3];
        for (int[] i : p.directions) {
            for(int j = 0; j < 3; j++) {
                possibleLocation[j] = p.location[j] + i[j];
            }
            if(p.bm.validMove(p, possibleLocation)) {
                validArrayList.add(i);
            } else {
                System.out.println("Cannot move from " + Arrays.toString(p.location) + " to " + Arrays.toString(possibleLocation));
            }
        }

        int[] direction = validArrayList.get(rand.nextInt(0,validArrayList.size()));
        int max;
        for (max = 1; max < 9; max++) {
            possibleLocation[0] = p.location[0] + (direction[0]*max);
            possibleLocation[1] = p.location[1] + (direction[1]*max);
            possibleLocation[2] = p.location[2] + (direction[2]*max);
            if(!p.bm.validMove(p, possibleLocation)) {
                if(max == 1)
                    System.out.println("Error: Cannot from " + Arrays.toString(p.location) +
                            " to " + Arrays.toString(possibleLocation));
                System.out.println("Max: " + max + "\t" + Arrays.toString(possibleLocation));
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
                System.out.println("Found in activeSpots: " + Arrays.toString(is));
                break;
            } else if(p.bm.activeSpots.indexOf(is) == p.bm.activeSpots.size() - 1) {
                System.out.println(Arrays.toString(possibleLocation) + " not found");
            }
        }
        /*Optional<int[]> chosen = p.bm.activeSpots.stream().filter(Objects::nonNull).
                filter(a -> Arrays.equals(a,0,3, possibleLocation,0,3)).
                findFirst();
        return chosen.orElse(null);
        *///return rand.nextInt(1,max-1);
        return chosen;
    }
}