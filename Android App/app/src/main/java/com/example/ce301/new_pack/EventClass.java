package com.example.ce301.new_pack;

public class EventClass {

    public final String message;
    public final String fromClass;
    public final int[][] points;

    public EventClass(String fromClass, String message) {
        this.fromClass = fromClass;
        this.message = message;
        points = null;
    }

    public EventClass(String fromClass,String message, int[][] points) {
        this.fromClass = fromClass;
        this.points = points;
        this.message = message;
    }
}
