package com.smartcampus.model;


public class CampusSensor {

    // Unique sensor code
    
    private String id;

    // Category of sensors: Temperature, CO2, Occupancy, Light etc.
    private String type;

    // The Current operational state: ACTIVE, MAINTENANCE, or OFFLINE

    private String status; // Only ACTIVE sensors can receive new readings ...

    // The most recent measurement recorded by this sensor
    // Gets updated every time a new reading is posted ...
    private double currentValue;


    // Foreign key linking this sensor to its parent room
    
    private String assignedRoomId;

    public CampusSensor() {}
    

    public CampusSensor(String id, String type, String status, double currentValue, String roomId) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.currentValue = currentValue;
        this.assignedRoomId = roomId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getCurrentValue() { return currentValue; }
    public void setCurrentValue(double currentValue) { this.currentValue = currentValue; }

    public String getAssignedRoomId() { return assignedRoomId; }
    public void setAssignedRoomId(String assignedRoomId) { this.assignedRoomId = assignedRoomId; }
}