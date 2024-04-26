package com.example.demo;

import java.util.*;

public class BoardAndMovement {
    public ArrayList<int[]> activeSpots = new ArrayList<>();
    public ArrayList<Penguin> activePenguins = new ArrayList<>();

    public static void main(String[] args) {
        BoardAndMovement bm = new BoardAndMovement();
        bm.setStartSpots(bm.numbers());

        bm.activePenguins.add(new Penguin(bm));
        bm.activePenguins.add(new Penguin(bm));
        /*bm.activePenguins.add(new Penguin(bm));
        bm.activePenguins.add(new Penguin(bm));
        bm.activePenguins.add(new Penguin(bm));
        bm.activePenguins.add(new Penguin(bm));
        bm.activePenguins.add(new Penguin(bm));
        bm.activePenguins.add(new Penguin(bm));

        bm.activePenguins.get(0).setLocation(new int[]{1,8,8});
        bm.activePenguins.get(1).setLocation(new int[]{5,1,11});
        bm.activePenguins.get(2).setLocation(new int[]{8,8,1});
        bm.activePenguins.get(3).setLocation(new int[]{11,1,5});
        bm.activePenguins.get(4).setLocation(new int[]{3,5,9});
        bm.activePenguins.get(5).setLocation(new int[]{5,8,4});
        bm.activePenguins.get(6).setLocation(new int[]{8,5,4});
        bm.activePenguins.get(7).setLocation(new int[]{6,3,8}); */
        bm.activePenguins.get(0).setLocation(new int[]{6,4,7});
        //bm.activePenguins.get(1).setLocation(new int[]{6,4,7});

        //bm.activeSpots.removeIf(a -> Arrays.equals(a, 0, 3, new int[]{10,1,6}, 0,3));
        //bm.activeSpots.removeIf(a -> Arrays.equals(a, 0, 3, new int[]{10,2,5}, 0,3));
        //bm.activeSpots.removeIf(a -> Arrays.equals(a, 0, 3, new int[]{9,3,5}, 0,3));
        //bm.activeSpots.removeIf(a -> Arrays.equals(a, 0, 3, new int[]{9,4,4}, 0,3));
        //bm.activeSpots.removeIf(a -> Arrays.equals(a, 0, 3, new int[]{10,4,3}, 0,3));
        //System.out.println(bm.activeSpots.size());
        //bm.checkForIsolation();
        //System.out.println(bm.activeSpots.size());


        //bm.activePenguins.get(0).setLocation(new int[]{2,7,8});
        //bm.activePenguins.add(new Penguin(bm));
        //bm.activePenguins.get(1).setLocation(new int[]{1,8,8});
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

    public void addPenguins(Penguin p) {
        activePenguins.add(p);
    }

    //NOTE: TESTING METHOD
    //Used to test the setStartSpots method
    public List<Integer> numbers() {

        List<Integer> fishTiles = new ArrayList<>();
        fishTiles.clear();
        for (int i = 0; i < 60; i++) fishTiles.add(1);
        /*for (int i = 0; i < 30; i++) fishTiles.add(1);
        for (int i = 0; i < 20; i++) fishTiles.add(2);
        for (int i = 0; i < 10; i++) fishTiles.add(3);
        Collections.shuffle(fishTiles);*/
        return fishTiles;
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

        for(int[] is : activeSpots) {
            System.out.println(Arrays.toString(is));
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
        //if(activeSpots.removeIf(a -> Arrays.equals(a, 0, 3, oldLocation, 0,3)))
        //  System.out.println(Arrays.toString(oldLocation) + " removed successfully");
        //else
        //  System.out.println(Arrays.toString(oldLocation) + " is not in the array");
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

    //TO DO: Test more and optimize space (time seems good enough)
    //TO DO: Check with activeSpots now null not removed
    //Tested 1 Worst case (8 penguins full board) (1 penguin full board), 1 cornered case (1 penguin), and 1 true isolated island (1 penguin)
    public void checkForIsolation() {
        ArrayList<Boolean> reachable = new ArrayList<>();
        for (int i = 0; i < activeSpots.size(); i++) reachable.add(false);
        final int[][] directions = {{0,-1,1},{0,1,-1},{-1,0,1},{1,0,-1},{1,-1,0},{-1,1,0}};
        ArrayList<int[]> toVisit = new ArrayList<>();
        ArrayList<Penguin> tempPenguins = new ArrayList<>(activePenguins);
        while(!tempPenguins.isEmpty()) {
            Penguin p = tempPenguins.remove(0);
            toVisit.add(Arrays.copyOf(p.location, 3));
            while(!toVisit.isEmpty()) {
                for (int i = 0; i < directions.length; i++) {
                    int[] nextSpace = addAll(Arrays.copyOf(toVisit.get(0),3), directions[i]);
                    Optional<int[]> accessedSpace = activeSpots.stream().filter(Objects::nonNull).filter(a -> Arrays.equals(a,0,3,nextSpace,0,3)).findFirst();
                    if(accessedSpace.isPresent() && !reachable.get(activeSpots.indexOf(accessedSpace.get()))) {
                        reachable.set(activeSpots.indexOf(accessedSpace.get()), true);
                        toVisit.add(Arrays.copyOf(nextSpace, nextSpace.length));
                        Optional<Penguin> penPlace = activePenguins.stream().filter(Objects::nonNull).filter(a -> Arrays.equals(a.location, 0, 3, nextSpace, 0,3)).findFirst();
                        penPlace.ifPresent(tempPenguins::remove);
                    }
                }
                toVisit.remove(0);
            }
        }
        for(int i = 0; i < reachable.size(); i++){
            if(reachable.get(i))
                continue;
            activeSpots.set(i,null);
        }
//    removeIf(a -> !reachable.get(activeSpots.indexOf(a)));
        /* for (int i = reachable.size()-1; i >= 0; i--) {
            if(reachable.get(i) == false) {
                System.out.println(Arrays.toString(activeSpots.remove(i)));
            }
        } */

    }

    private static int[] addAll(int[] main, int[] b) {
        for(int i = 0; i < 3; i++) {
            main[i] += b[i];
        }
        return main;
    }

    public Penguin penguinPresent(int index) {
        Optional <Penguin> pen = activePenguins.stream().filter(a -> a.location == activeSpots.get(index)).findFirst();
        return pen.orElse(null);
    }
}