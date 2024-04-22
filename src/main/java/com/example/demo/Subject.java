package com.example.demo;

public interface Subject {
    //Source: https://www.digitalocean.com/community/tutorials/observer-design-pattern-in-java
    //Methods to register and unregister observer
    public void register(Observer obj);
    public void unregister(Observer obj);
    //Method to notify observers of change.
    public void notifyObservers();
    //Method to get updates from subject
    public Object getUpdate(Observer obj);
}
