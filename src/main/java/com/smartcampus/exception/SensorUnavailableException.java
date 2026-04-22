package com.smartcampus.exception;

/**
 * Thrown when a new reading is  posted to a sensor that is currently
 * under MAINTENANCE and cannot accept data.
 * Mapped to HTTP 403 Forbidden by its exception mapper .
 */



public class SensorUnavailableException extends RuntimeException {
    public SensorUnavailableException(String sensorId) {
        
        
        super("Sensor  '" + sensorId + "' is currently under maintenance and cannot accept " +
                "new readings. Please wait until the  sensor status is restored to ACTIVE.");
    }
}