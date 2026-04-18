package com.smartcampus.model;

import java.util.ArrayList;
import java.util.List;


public class Room {

    // Unique room code
    private String id;

    // name shown to facilities managers
    private String name;

    // Maximum number of people allowed in this room (fire safety)
    private int capacity;

    // List of sensor IDs currently deployed in this room

    private List<String> sensorIds = new ArrayList<>();

    public Room() {}

    public Room(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public List<String> getSensorIds() { return sensorIds; }
    public void setSensorIds(List<String> sensorIds) { this.sensorIds = sensorIds; }
}