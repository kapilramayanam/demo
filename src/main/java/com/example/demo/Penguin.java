package com.example.demo;

import javafx.scene.image.ImageView;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class Penguin implements Observer{
    BoardAndMovement bm;
    int[] location = new int[4];
    final int[][] directions = {{0,-1,1},{0,1,-1},{-1,0,1},{1,0,-1},{1,-1,0},{-1,1,0}};
    private Subject topic;
    private int pengScore;
    private ImageView imageView;
    Penguin(BoardAndMovement bam, ImageView imageView) {
        bm = bam;
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }
    //If a penguin is completely isolated, returns true. Else, returns false
    public boolean penguinIsolation() {
        int[] desired = new int[3];
        for(int[] i : directions) {
            desired[0] = location[0] + i[0];
            desired[1] = location[1] + i[1];
            desired[2] = location[2] + i[2];
            if(bm.validMove(this, desired))
                return false;
        }
        return true;
    }

    //Sets a new location and returns true if the move can be made
    //Returns false the move cannot be made
    public boolean setLocation(int[] newLocation) {

        //if a location hasn't been set yet
        if(location[0] == 0) {
            //if (bm.activeSpots.stream().filter(Objects::nonNull).anyMatch(a -> Arrays.equals(a, 0, 3, newLocation, 0,3))
            //&& bm.activePenguins.stream().filter(Objects::nonNull).noneMatch(a -> Arrays.equals(a.location, 0, 3, newLocation, 0,3)))
            if (bm.activeSpots.stream().filter(Objects::nonNull).anyMatch(a -> a == newLocation)
                    && bm.activePenguins.stream().filter(Objects::nonNull).noneMatch(a -> a.location == newLocation))
            {
                //int[] aSpot = bm.activeSpots.stream().filter(Objects::nonNull).filter(a -> Arrays.equals(a, 0, 3, newLocation, 0,3)).findFirst().get();
                int[] aSpot = bm.activeSpots.stream().filter(Objects::nonNull).filter(a -> a == newLocation).findFirst().get();
                if(aSpot[3] == 1) {
                    location = newLocation;
                    bm.addPenguins(this);
                    return true;
                }
            }

            //All below for printing tests
            //Can be deleted once testing is complete
            Optional<int[]> b = bm.activeSpots.stream().filter(Objects::nonNull).filter(a -> Arrays.equals(a, 0, 3, newLocation, 0,3)).findFirst();
            if(b.isPresent()) {
                System.out.println(Arrays.toString(b.get())+ " is occupied or not 1-fish");
            } else {
                System.out.println(Arrays.toString(newLocation) + " is missing");
            }

            return false;
        }

        if(bm.validMove(this, newLocation)) {
            bm.removeSpot(location);
            location = newLocation;
            return true;
        }
        System.out.println(Arrays.toString(newLocation) + " is not a valid location");
        return false;
    }

    public int getScore(){
        return this.pengScore;
    }

    @Override
    public void update() {
        this.pengScore = (int) topic.getUpdate(this);
    }

    @Override
    public void setSubject(Subject sub) {
        topic = sub;
    }
}