package com.smartcampus.model;

import java.util.UUID;


public class SensorReading {

    // Auto-generated unique ID for this specific reading event
    private String id;

    // Exact time this reading was captured in epoch milliseconds
    private long timestamp;

    // The actual measurement value e.g. 22.5 for temperature
    private double value;

    public SensorReading() {}

    // Constructor auto-generates id and timestamp on creation
    public SensorReading(double value) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.value = value;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
}