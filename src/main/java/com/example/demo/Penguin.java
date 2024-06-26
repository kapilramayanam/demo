package com.example.demo;

import javafx.scene.image.ImageView;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class Penguin implements Observer{
    BoardAndMovement bm;
    int[] location = new int[4];
    final int[][] directions = {{0,-1,1},{0,1,-1},{-1,0,1},{1,0,-1},{1,-1,0},{-1,1,0}};
    private Subject topic;
    private int pengScore;
    private ImageView image;
    Penguin(BoardAndMovement bam, ImageView image) {
        bm = bam;
        this.image = image;
    }

    public ImageView getImageView() {
        return image;
    }

    //Sets a new location and returns true if the move can be made
    //Returns false the move cannot be made
    public boolean setLocation(int[] newLocation) {

        //if a location hasn't been set yet
        if(location[0] == 0) {
            if (bm.activeSpots.stream().filter(Objects::nonNull).anyMatch(a -> a == newLocation)
                    && bm.activePenguins.stream().filter(Objects::nonNull).noneMatch(a -> a.location == newLocation))
            {
                int[] aSpot = bm.activeSpots.stream().filter(Objects::nonNull).filter(a -> a == newLocation).findFirst().get();
                if(aSpot[3] == 1) {
                    location = newLocation;
                    bm.addPenguins(this);
                    return true;
                }
            }

            Optional<int[]> b = bm.activeSpots.stream().filter(Objects::nonNull).filter(a -> Arrays.equals(a, 0, 3, newLocation, 0,3)).findFirst();
            if(b.isPresent()) {
                System.out.println("\tPenguin: " + Arrays.toString(b.get())+ " is occupied or not 1-fish");
            } else {
                System.out.println("\tPenguin: " + Arrays.toString(newLocation) + " is missing");
            }

            return false;
        }

        if(bm.validMove(this, newLocation)) {
            bm.removeSpot(location);
            location = newLocation;
            return true;
        }
        System.out.println("\tPenguin: " + Arrays.toString(newLocation) + " is not a valid location");
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
