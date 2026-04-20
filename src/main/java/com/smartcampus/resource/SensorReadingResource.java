package com.smartcampus.resource;

import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Sub-resource class for managing sensor reading history.
 *
 * This class is not registered directly with JAX-RS - it is returned
 * by the sub-resource locator method in SensorResource. JAX-RS then
 * uses this instance to handle all /sensors/{sensorId}/readings requests.
 *
 *
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    // The parent sensor this resource is operating on
    private final Sensor parentSensor;

    public SensorReadingResource(Sensor parentSensor) {
        this.parentSensor = parentSensor;
    }

    /**
     * GET /api/v1/sensors/{sensorId}/readings
     * Returns the full historical log of readings for this sensor.
     */
    @GET
    public Response getReadings() {
        List<SensorReading> history = DataStore.getReadings()
                .getOrDefault(parentSensor.getId(), new ArrayList<>());
        return Response.ok(history).build();
    }

    /**
     * POST /api/v1/sensors/{sensorId}/readings
     * Records a new reading for this sensor.
     *
     * Rejects requests if the sensor is under MAINTENANCE - a physically
     * disconnected sensor cannot reliably capture measurements.
     * Throws SensorUnavailableException -> HTTP 403 Forbidden.
     *
     * Side effect: updates the parent sensor's currentValue to the
     * newly recorded value to maintain consistency across the API.
     */
    @POST
    public Response addReading(SensorReading reading) {

        // Block readings from sensors under maintenance
        if ("MAINTENANCE".equalsIgnoreCase(parentSensor.getStatus())) {
            throw new SensorUnavailableException(parentSensor.getId());
        }

        if (reading == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of(
                            "status", 400,
                            "error", "Bad Request",
                            "message", "Reading body is required. Please provide a value field."
                    ))
                    .build();
        }

        // Create a new reading with auto-generated ID and timestamp
        SensorReading newReading = new SensorReading(reading.getValue());

        // Store in the reading log
        DataStore.getReadings()
                .computeIfAbsent(parentSensor.getId(), k -> new ArrayList<>())
                .add(newReading);

        // Side effect: keep parent sensor currentValue in sync
        parentSensor.setCurrentValue(newReading.getValue());

        return Response.status(Response.Status.CREATED)
                .entity(newReading)
                .build();
    }
}