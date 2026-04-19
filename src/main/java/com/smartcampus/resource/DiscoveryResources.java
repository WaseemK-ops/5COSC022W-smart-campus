package com.smartcampus.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Root discovery endpoint for the Smart Campus API.
 *
 * Returns API metadata and navigational links to all available
 * resources. This follows the HATEOAS principle - clients discover
 * available endpoints from this response rather than relying on
 * hard-coded URLs or external documentation.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResources {

    @GET
    public Response discover() {

        // The Core API information
        Map<String, Object> apiInfo = new LinkedHashMap<>();

        apiInfo.put("api", "Smart Campus Sensor & Room Management API");
        apiInfo.put("version", "1.0.0");
        apiInfo.put("status", "healthy");
        apiInfo.put("university", "University of Westminster");
        apiInfo.put("description", "RESTful API for managing campus rooms and IoT sensor infrastructure.");

        // HATEOAS links - clients navigate the API using these
        Map<String, String> resourceLinks = new LinkedHashMap<>();
        resourceLinks.put("rooms",          "/api/v1/rooms");
        resourceLinks.put("sensors",        "/api/v1/sensors");
        resourceLinks.put("sensorReadings", "/api/v1/sensors/{sensorId}/readings");
        apiInfo.put("resources", resourceLinks);

        return Response.ok(apiInfo).build();
    }
}