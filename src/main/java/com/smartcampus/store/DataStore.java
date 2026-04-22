package com.smartcampus.store;

import com.smartcampus.model.Room;
import com.smartcampus.model.CampusSensor;
import com.smartcampus.model.SensorReading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory data store for the Smart Campus API.
 * Uses ConcurrentHashMap to safely handle concurrent requests.
 *
 * Pre-loaded with rooms and sensors across three Westminster buildings:
 *   SP = Spencer Building  |  GP = GP Building  |  JB = Java Building
 *
 * Sensor format: [TYPE]-[BUILDING]-[FLOOR] e.g. TEMP-SP-1
 */





public class DataStore {

    // All campus rooms stored by their unique room ID
    private static final Map<String, Room> roomStorage = new ConcurrentHashMap<>();

    // All sensors stored by their unique sensor ID
    private static final Map<String, CampusSensor> sensorStorage = new ConcurrentHashMap<>();

    // Historical readings per sensor, stored by sensor ID
    private static final Map<String, List<SensorReading>> readingLog = new ConcurrentHashMap<>();

    static {

        // ══════════════════════════════════════════════════════
        //  SPENCER BUILDING (SP) — Floors 1 to 7
        // ══════════════════════════════════════════════════════
        Room sp1La = new Room("SP-1LA", "Spencer Building - Floor 1 Lecture Area", 80);
        Room sp2La = new Room("SP-2LA", "Spencer Building - Floor 2 Lecture Area", 75);
        Room sp3La = new Room("SP-3LA", "Spencer Building - Floor 3 Lecture Area", 70);
        Room sp4La = new Room("SP-4LA", "Spencer Building - Floor 4 Lecture Area", 60);
        Room sp5La = new Room("SP-5LA", "Spencer Building - Floor 5 Lecture Area", 60);
        Room sp6La = new Room("SP-6LA", "Spencer Building - Floor 6 Lecture Area", 50);
        Room sp7La = new Room("SP-7LA", "Spencer Building - Floor 7 Lecture Area", 50);

        roomStorage.put(sp1La.getId(), sp1La);
        roomStorage.put(sp2La.getId(), sp2La);
        roomStorage.put(sp3La.getId(), sp3La);
        roomStorage.put(sp4La.getId(), sp4La);
        roomStorage.put(sp5La.getId(), sp5La);
        roomStorage.put(sp6La.getId(), sp6La);
        roomStorage.put(sp7La.getId(), sp7La);

        // ══════════════════════════════════════════════════════
        //   (GP) — Floors 1 to 7 + Auditorium
        // ═════════════════════════════════════════════════════
        Room gp1La = new Room("GP-1LA", "GP - Floor 1 Lecture Area", 90);
        Room gp2La = new Room("GP-2LA", "GP- Floor 2 Lecture Area", 85);
        Room gp3La = new Room("GP-3LA", "GP- Floor 3 Lecture Area", 80);
        Room gp4La = new Room("GP-4LA", "GP - Floor 4 Lecture Area", 75);
        Room gp5La = new Room("GP-5LA", "GP - Floor 5 Lecture Area", 70);
        Room gp6La = new Room("GP-6LA", "GP - Floor 6 Lecture Area", 65);
        Room gp7La = new Room("GP-7LA", "GP - Floor 7 Lecture Area", 60);
        Room gpAud = new Room("GP-AUD", "GP - Main Auditorium",      400);

        roomStorage.put(gp1La.getId(), gp1La);
        roomStorage.put(gp2La.getId(), gp2La);
        roomStorage.put(gp3La.getId(), gp3La);
        roomStorage.put(gp4La.getId(), gp4La);
        roomStorage.put(gp5La.getId(), gp5La);
        roomStorage.put(gp6La.getId(), gp6La);
        roomStorage.put(gp7La.getId(), gp7La);
        roomStorage.put(gpAud.getId(), gpAud);




        // ══════════════════════════════════════════════════════
        //  JAVA BUILDING (JB) — Floors 1 to 7
        // ══════════════════════════════════════════════════════
        Room jb1La = new Room("JB-1LA", "Java Building - Floor 1 Lecture Area", 70);
        Room jb2La = new Room("JB-2LA", "Java Building - Floor 2 Lecture Area", 65);
        Room jb3La = new Room("JB-3LA", "Java Building - Floor 3 Lecture Area", 60);
        Room jb4La = new Room("JB-4LA", "Java Building - Floor 4 Lecture Area", 55);
        Room jb5La = new Room("JB-5LA", "Java Building - Floor 5 Lecture Area", 55);
        Room jb6La = new Room("JB-6LA", "Java Building - Floor 6 Lecture Area", 50);
        Room jb7La = new Room("JB-7LA", "Java Building - Floor 7 Lecture Area", 45);

        roomStorage.put(jb1La.getId(), jb1La);
        roomStorage.put(jb2La.getId(), jb2La);
        roomStorage.put(jb3La.getId(), jb3La);
        roomStorage.put(jb4La.getId(), jb4La);
        roomStorage.put(jb5La.getId(), jb5La);
        roomStorage.put(jb6La.getId(), jb6La);
        roomStorage.put(jb7La.getId(), jb7La);





        // ══════════════════════════════════════════════════════
        //  SENSORS — Spencer Building
        // ══════════════════════════════════════════════════════
        CampusSensor tempSp1  = new CampusSensor("TEMP-SP-1",  "Temperature", "ACTIVE",      22.1, "SP-1LA");
        CampusSensor co2Sp2   = new CampusSensor("CO2-SP-2",   "CO2",         "ACTIVE",      415.0,"SP-2LA");
        CampusSensor occSp3   = new CampusSensor("OCC-SP-3",   "Occupancy",   "ACTIVE",      35.0, "SP-3LA");
        CampusSensor lightSp4 = new CampusSensor("LIGHT-SP-4", "Light",       "MAINTENANCE", 0.0,  "SP-4LA");
        CampusSensor humSp5   = new CampusSensor("HUM-SP-5",   "Humidity",    "ACTIVE",      55.0, "SP-5LA");
        CampusSensor tempSp6  = new CampusSensor("TEMP-SP-6",  "Temperature", "ACTIVE",      21.8, "SP-6LA");
        CampusSensor co2Sp7   = new CampusSensor("CO2-SP-7",   "CO2",         "OFFLINE",     0.0,  "SP-7LA");

        sensorStorage.put(tempSp1.getId(),  tempSp1);
        sensorStorage.put(co2Sp2.getId(),   co2Sp2);
        sensorStorage.put(occSp3.getId(),   occSp3);
        sensorStorage.put(lightSp4.getId(), lightSp4);
        sensorStorage.put(humSp5.getId(),   humSp5);
        sensorStorage.put(tempSp6.getId(),  tempSp6);
        sensorStorage.put(co2Sp7.getId(),   co2Sp7);


        sp1La.getDeployedSensorIds().add("TEMP-SP-1");
        sp2La.getDeployedSensorIds().add("CO2-SP-2");
        sp3La.getDeployedSensorIds().add("OCC-SP-3");
        sp4La.getDeployedSensorIds().add("LIGHT-SP-4");
        sp5La.getDeployedSensorIds().add("HUM-SP-5");
        sp6La.getDeployedSensorIds().add("TEMP-SP-6");
        sp7La.getDeployedSensorIds().add("CO2-SP-7");

        // ══════════════════════════════════════════════════════
        //  SENSORS — Gregory Peck Building (including Auditorium)
        // ══════════════════════════════════════════════════════
        CampusSensor tempGp1  = new CampusSensor("TEMP-GP-1",  "Temperature", "ACTIVE",      23.0, "GP-1LA");
        CampusSensor co2Gp2   = new CampusSensor("CO2-GP-2",   "CO2",         "ACTIVE",      400.0,"GP-2LA");
        CampusSensor occGp3   = new CampusSensor("OCC-GP-3",   "Occupancy",   "ACTIVE",      42.0, "GP-3LA");
        CampusSensor lightGp4 = new CampusSensor("LIGHT-GP-4", "Light",       "ACTIVE",      320.0,"GP-4LA");
        CampusSensor humGp5   = new CampusSensor("HUM-GP-5",   "Humidity",    "MAINTENANCE", 0.0,  "GP-5LA");
        CampusSensor tempGp6  = new CampusSensor("TEMP-GP-6",  "Temperature", "ACTIVE",      22.5, "GP-6LA");
        CampusSensor co2Gp7   = new CampusSensor("CO2-GP-7",   "CO2",         "ACTIVE",      390.0,"GP-7LA");
        // Auditorium has multiple sensors due to its large capacity
        CampusSensor tempAud  = new CampusSensor("TEMP-GP-AUD",  "Temperature", "ACTIVE",    20.0, "GP-AUD");
        CampusSensor occAud   = new CampusSensor("OCC-GP-AUD",   "Occupancy",   "ACTIVE",   250.0, "GP-AUD");
        CampusSensor co2Aud   = new CampusSensor("CO2-GP-AUD",   "CO2",         "ACTIVE",   430.0, "GP-AUD");
        CampusSensor lightAud = new CampusSensor("LIGHT-GP-AUD", "Light",       "ACTIVE",   500.0, "GP-AUD");

        sensorStorage.put(tempGp1.getId(),  tempGp1);
        sensorStorage.put(co2Gp2.getId(),   co2Gp2);
        sensorStorage.put(occGp3.getId(),   occGp3);
        sensorStorage.put(lightGp4.getId(), lightGp4);
        sensorStorage.put(humGp5.getId(),   humGp5);
        sensorStorage.put(tempGp6.getId(),  tempGp6);
        sensorStorage.put(co2Gp7.getId(),   co2Gp7);
        sensorStorage.put(tempAud.getId(),  tempAud);
        sensorStorage.put(occAud.getId(),   occAud);
        sensorStorage.put(co2Aud.getId(),   co2Aud);
        sensorStorage.put(lightAud.getId(), lightAud);

        gp1La.getDeployedSensorIds().add("TEMP-GP-1");
        gp2La.getDeployedSensorIds().add("CO2-GP-2");
        gp3La.getDeployedSensorIds().add("OCC-GP-3");
        gp4La.getDeployedSensorIds().add("LIGHT-GP-4");
        gp5La.getDeployedSensorIds().add("HUM-GP-5");
        gp6La.getDeployedSensorIds().add("TEMP-GP-6");
        gp7La.getDeployedSensorIds().add("CO2-GP-7");
        gpAud.getDeployedSensorIds().add("TEMP-GP-AUD");
        gpAud.getDeployedSensorIds().add("OCC-GP-AUD");
        gpAud.getDeployedSensorIds().add("CO2-GP-AUD");
        gpAud.getDeployedSensorIds().add("LIGHT-GP-AUD");

        // ══════════════════════════════════════════════════════
        //  SENSORS — Java Building
        // ══════════════════════════════════════════════════════
        CampusSensor tempJb1  = new CampusSensor("TEMP-JB-1",  "Temperature", "ACTIVE",      21.0, "JB-1LA");
        CampusSensor co2Jb2   = new CampusSensor("CO2-JB-2",   "CO2",         "ACTIVE",      405.0,"JB-2LA");
        CampusSensor occJb3   = new CampusSensor("OCC-JB-3",   "Occupancy",   "ACTIVE",      28.0, "JB-3LA");
        CampusSensor lightJb4 = new CampusSensor("LIGHT-JB-4", "Light",       "ACTIVE",      340.0,"JB-4LA");
        CampusSensor humJb5   = new CampusSensor("HUM-JB-5",   "Humidity",    "ACTIVE",      60.0, "JB-5LA");
        CampusSensor tempJb6  = new CampusSensor("TEMP-JB-6",  "Temperature", "MAINTENANCE", 0.0,  "JB-6LA");
        CampusSensor co2Jb7   = new CampusSensor("CO2-JB-7",   "CO2",         "ACTIVE",      420.0,"JB-7LA");

        sensorStorage.put(tempJb1.getId(),  tempJb1);
        sensorStorage.put(co2Jb2.getId(),   co2Jb2);
        sensorStorage.put(occJb3.getId(),   occJb3);
        sensorStorage.put(lightJb4.getId(), lightJb4);
        sensorStorage.put(humJb5.getId(),   humJb5);
        sensorStorage.put(tempJb6.getId(),  tempJb6);
        sensorStorage.put(co2Jb7.getId(),   co2Jb7);

        jb1La.getDeployedSensorIds().add("TEMP-JB-1");
        jb2La.getDeployedSensorIds().add("CO2-JB-2");
        jb3La.getDeployedSensorIds().add("OCC-JB-3");
        jb4La.getDeployedSensorIds().add("LIGHT-JB-4");
        jb5La.getDeployedSensorIds().add("HUM-JB-5");
        jb6La.getDeployedSensorIds().add("TEMP-JB-6");
        jb7La.getDeployedSensorIds().add("CO2-JB-7");

        // ══════════════════════════════════════════════════════
        //  INITIALISE EMPTY READING HISTORY FOR ALL SENSORS
        // ══════════════════════════════════════════════════════
        sensorStorage.keySet().forEach(id -> readingLog.put(id, new ArrayList<>()));
    }

    public static Map<String, Room> getRooms()    { return roomStorage;  }
    public static Map<String, CampusSensor> getSensors() { return sensorStorage; }
    public static Map<String, List<SensorReading>> getReadings() { return readingLog; }
}