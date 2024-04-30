package com.example.demo;

import java.util.*;

public class BoardAndMovement {
    public ArrayList<int[]> activeSpots = new ArrayList<>();
    public ArrayList<Penguin> activePenguins = new ArrayList<>();

    public void addPenguins(Penguin p) {
        activePenguins.add(p);
    }

    public void setStartSpots(List<Integer> fishTiles) {
        activeSpots.clear();
        activePenguins.clear();
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
        //If user clicks on a missing spot
        if(desired == null)
            return null;
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
        activeSpots.stream().filter(Objects::nonNull).filter(a -> a == oldLocation).findFirst().ifPresent(a -> activeSpots.set(activeSpots.indexOf(a), null));
    }

    public boolean validMove(Penguin p, int[] desired) {
        int[] current = Arrays.copyOf(p.location, p.location.length);
        int[] direction = getDirection(p.location, desired);
        if(direction == null)
            return false;

        //while there is an int[] in activeSpots matching current
        while (activeSpots.stream().filter(Objects::nonNull).anyMatch(a -> Arrays.equals(a, 0, 3, current, 0,3))) {//Code via https://stackoverflow.com/questions/4849051/using-contains-on-an-arraylist-with-integer-arrays

            //if the space is already occupied by a different penguin
            if(!(activePenguins.stream().filter(Objects::nonNull).filter(a -> Arrays.equals(a.location, 0, 3, current, 0,3)).allMatch(a -> a == p)))
                return false;

            //If the desired has been located
            if(Arrays.equals(current, 0, 3, desired, 0,3)) {
                return true;
            }
            else {
                addAll(current, direction);
            }
        }

        return false;
    }

    public ArrayList<Integer> checkForIsolation() {
        ArrayList<Boolean> reachable = new ArrayList<>();
        ArrayList<Integer> remove = new ArrayList<>();
        for (int i = 0; i < activeSpots.size(); i++) reachable.add(false);
        final int[][] directions = {{0,-1,1},{0,1,-1},{-1,0,1},{1,0,-1},{1,-1,0},{-1,1,0}};
        ArrayList<int[]> toVisit = new ArrayList<>();
        ArrayList<Penguin> tempPenguins = new ArrayList<>(activePenguins);

        //While there are still penguins to examine
        while(!tempPenguins.isEmpty()) {
            //Pop first penguin off the list
            Penguin p = tempPenguins.remove(0);

            //Add its location to the list of items to visit
            toVisit.add(Arrays.copyOf(p.location, 3));

            //While there are still spaces to visit
            while(!toVisit.isEmpty()) {

                //Look at all the directions the penguin can go
                for (int i = 0; i < directions.length; i++) {

                    //Go one spot in that direction
                    int[] nextSpace = addAll(Arrays.copyOf(toVisit.get(0),3), directions[i]);

                    //check activeSpots for a non-null matching location
                    //if such a space exists, then set accessedSpace to activeSpots int[]
                    Optional<int[]> accessedSpace = activeSpots.stream().filter(Objects::nonNull).
                            filter(a -> Arrays.equals(a,0,3,nextSpace,0,3)).findFirst();

                    //if there is such a spot and the spot has not yet been visited
                    if(accessedSpace.isPresent() && !reachable.get(activeSpots.indexOf(accessedSpace.get()))) {

                        //Set the spot to reachable
                        reachable.set(activeSpots.indexOf(accessedSpace.get()), true);

                        //Add the space to the queue to see if it has any other spots that it can access
                        toVisit.add(Arrays.copyOf(nextSpace, nextSpace.length));
                    }
                }
                toVisit.remove(0);
            }
        }
        for(int i = 0; i < reachable.size(); i++){
            if(reachable.get(i))
                continue;
            else {
                remove.add(i);
            }
        }

        for(Integer i : remove) {
            System.out.println("\tBM: Remove requested: " + i);
        }
        return remove;
    }

    private static int[] addAll(int[] main, int[] b) {
        for(int i = 0; i < 3; i++) {
            main[i] += b[i];
        }
        return main;
    }

    public Penguin penguinPresent(int index) {
        Optional<Penguin> pen = activePenguins.stream().
                filter(Objects::nonNull).filter(a -> a.location == activeSpots.get(index)).findFirst();
        return pen.orElse(null);

        //ERROR HERE ^
        //activeSpots set to null when penguin is placed there
        //Rework to only set to null after penguin leaves or something
        //4/29: Above likely resolved; not receiving any errors

    }
}