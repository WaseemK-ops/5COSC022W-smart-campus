package com.smartcampus.model;

import java.util.UUID;


public class SensorReading {

    // An Auto-generated unique ID for this specific reading event
    
    private String id;

    // Exact time this reading  was  captured in epoch milliseconds
    private long recordedAt;

    // The  actual measurement measuredValue e.g. 22.5 for temperature .
    
    private double measuredValue;

    public SensorReading() {}
    

    // Constructor auto-generates id  and recordedAt on creation
    public SensorReading(double value) {
        this.id = UUID.randomUUID().toString();
        this.recordedAt = System.currentTimeMillis();
        this.measuredValue = value;
    }
    
    
    

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    

    public long getRecordedAt() { return recordedAt; }
    public void setRecordedAt(long recordedAt) { this.recordedAt = recordedAt; }
    
    

    public double getMeasuredValue() { return measuredValue; }
    public void setMeasuredValue(double measuredValue) { this.measuredValue = measuredValue; }
}