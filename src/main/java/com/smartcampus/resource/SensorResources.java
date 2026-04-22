package com.smartcampus.resource;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.model.Room;
import com.smartcampus.model.CampusSensor;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles all the sensor operations for the Smart Campus API.
 *
 * Sensors must always be linked to a valid room - the POST method
 * validates this - the roomId exists before registering the sensor.
 *
 * Filtering by type uses @QueryParam rather than a path segment
 * because query parameters are contextually correct for narrowing
 * a  collection.
 *
 * So if a client inputs  the wrong Content-Type , JAX-RS automatically rejects the request
 * with HTTP 415 Unsupported Media Type before this code even runs.
 */
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResources {

    // References to shared static storage
    private final Map<String, CampusSensor> sensorStorage = DataStore.getSensors();
    private final Map<String, Room> roomStorage = DataStore.getRooms();

    /**
     * GET /api/v1/sensors
     * GET /api/v1/sensors?type=CO2
     *
     * Returns all sensors or filters by type if query param is provided.
     * The type filter is case-insensitive so "co2" matches "CO2".
     */
    @GET
    public Response getSensors(@QueryParam("type") String type) {
        Collection<CampusSensor> sensorLists = sensorStorage.values();

        // If a type filter is provided, returns only matching sensors
        if (type != null && !type.isBlank()) {
            List<CampusSensor> matchedSensors = sensorLists.stream()
                    .filter(s -> s.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
            return Response.ok(matchedSensors).build();
        }

        // No filter - return everything
        return Response.ok(new ArrayList<>(sensorLists)).build() ;
    }





    /**
     * POST /api/v1/sensors
     * Registers a new sensor in the system.
     *
     * checks that the roomId in the request body actually exists.
     * If not, throws LinkedResourceNotFoundException -> 422 response.
     * 
     * This is 422 not 404 because the URL is valid - the problem is
     * a bad reference inside the JSON payload.
     */
    @POST
    public Response createSensor(CampusSensor sensor) {
        if (sensor == null || sensor.getId() == null || sensor.getId().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of(
                            "status", 400,
                            "error", "Bad Request",
                            "message", "Sensor ID is required and cannot be blank."
                    ))
                    .build();
        }

        // Validates the referenced room actually exists..
        
        if (sensor.getAssignedRoomId() == null || !roomStorage.containsKey(sensor.getAssignedRoomId())) {
            throw new LinkedResourceNotFoundException(
                    
                    "Cannot register sensor '" + sensor.getId() + "': the roomId '" +
                            sensor.getAssignedRoomId() + "' does not exist in the system. " +
                            "Please create the room first before assigning sensors to it.");
        }

        if (sensorStorage.containsKey(sensor.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of(
                            "status", 409,
                            "error", "Conflict",
                            "message", "Sensor '" + sensor.getId() + "' is already registered in the system."
                    ))
                    .build();
        }

        
        
        
        
        
        // Save sensor and links it to its very own room .
        sensorStorage.put(sensor.getId(), sensor);
        roomStorage.get(sensor.getAssignedRoomId()).getDeployedSensorIds().add(sensor.getId());

        // Initialises empty reading history for this sensor
        DataStore.getReadings().put(sensor.getId(), new ArrayList<>());

        return Response.status(Response.Status.CREATED)
                .header("Location", "/api/v1/sensors/" + sensor.getId())
                .entity(sensor)
                .build();
    }

    
    
    
    
    /**
     * GET /api/v1/sensors/{sensorId}
     * Fetches full details for the specific sensor by its unique ID.
     */


    @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        CampusSensor sensor = sensorStorage.get(sensorId);
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of(
                            "status", 404,
                            "error", "Not Found",
                            "message", "No sensor found with ID '" + sensorId + "'."
                    ))
                    .build() ;
        }
        return Response.ok(sensor).build() ;
    }



    /**
     
     * This is a method which does NOT handle the request itself - it returns a
     * SensorReadingResource instance which JAX-RS then uses it to handle
     * any /sensors/{sensorId}/readings/* requests.
     *

     */





    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        CampusSensor sensor = sensorStorage.get(sensorId);
        if (sensor == null) {
            throw new NotFoundException("Sensor '" + sensorId + "' not found.");
        }
        return new SensorReadingResource(sensor);
    }
}