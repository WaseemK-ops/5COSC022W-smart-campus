package com.smartcampus.resource;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles all sensor operations for the Smart Campus API.
 *
 * Sensors must always be linked to a valid room - the POST method
 * validates the roomId exists before registering the sensor.
 *
 * Filtering by type uses @QueryParam rather than a path segment
 * because query parameters are semantically correct for narrowing
 * a collection. The resource identity (/sensors) stays the same
 * regardless of what filter is applied.
 *
 * So if a client sends the wrong Content-Type (e.g. text/plain instead
 * of application/json), JAX-RS automatically rejects the request
 * with HTTP 415 Unsupported Media Type before this code even runs.
 */
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResources {

    // References to shared static storage
    private final Map<String, Sensor> sensorStorage = DataStore.getSensors();
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
        Collection<Sensor> allSensors = sensorStorage.values();

        // If type filter provided, return only matching sensors
        if (type != null && !type.isBlank()) {
            List<Sensor> filteredList = allSensors.stream()
                    .filter(s -> s.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
            return Response.ok(filteredList).build();
        }

        // No filter - return everything
        return Response.ok(new ArrayList<>(allSensors)).build() ;
    }





    /**
     * POST /api/v1/sensors
     * Registers a new sensor in the system.
     *
     * Validates that the roomId in the request body actually exists.
     * If not, throws LinkedResourceNotFoundException -> 422 response.
     * This is 422 not 404 because the URL is valid - the problem is
     * a bad reference inside the JSON payload.
     */
    @POST
    public Response createSensor(Sensor sensor) {
        if (sensor == null || sensor.getId() == null || sensor.getId().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of(
                            "status", 400,
                            "error", "Bad Request",
                            "message", "Sensor ID is required and cannot be blank."
                    ))
                    .build();
        }

        // Validate the referenced room actually exists
        if (sensor.getRoomId() == null || !roomStorage.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException(
                    "Cannot register sensor '" + sensor.getId() + "': the roomId '" +
                            sensor.getRoomId() + "' does not exist in the system. " +
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

        // Save sensor and link it to its room .
        sensorStorage.put(sensor.getId(), sensor);
        roomStorage.get(sensor.getRoomId()).getSensorIds().add(sensor.getId());

        // Initialise empty reading history for this sensor
        DataStore.getReadings().put(sensor.getId(), new ArrayList<>());

        return Response.status(Response.Status.CREATED)
                .header("Location", "/api/v1/sensors/" + sensor.getId())
                .entity(sensor)
                .build();
    }

    /**
     * GET /api/v1/sensors/{sensorId}
     * Fetches full details for a specific sensor by its unique ID.
     */


    @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = sensorStorage.get(sensorId);
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
     * Sub-resource locator for sensor readings.
     *
     * This method does NOT handle the request itself - it returns a
     * SensorReadingResource instance which JAX-RS then uses to handle
     * any /sensors/{sensorId}/readings/* requests.
     *
     * This is  the Sub-Resource Locator pattern - it keeps reading logic
     * in a dedicated class rather than bloating this controller.
     */





    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        Sensor sensor = sensorStorage.get(sensorId);
        if (sensor == null) {
            throw new NotFoundException("Sensor '" + sensorId + "' not found.");
        }
        return new SensorReadingResource(sensor);
    }
}