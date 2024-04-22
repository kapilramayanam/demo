package com.example.demo;

public interface Observer {
    //Source: https://www.digitalocean.com/community/tutorials/observer-design-pattern-in-java

    //Method to update the observer, sued by subect.
    public void update();
    //attach with subject to observe
    public void setSubject(Subject sub);
}
