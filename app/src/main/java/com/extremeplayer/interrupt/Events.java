package com.extremeplayer.interrupt;

public class Events {

    private int number;
    private String eventsName;

    Events(int num, String name) {
        this.number = num;
        this.eventsName = name;
    }

    public int getNumber() {
        return number;
    }

    public String getEventsName() {
        return eventsName;
    }
}
