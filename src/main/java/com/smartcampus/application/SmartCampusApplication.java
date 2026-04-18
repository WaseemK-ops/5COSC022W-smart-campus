package com.smartcampus.application;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
/**
 * JAX-RS Application Configuration
 *
 * This class bootstraps the JAX-RS application.
 * The @ApplicationPath annotation sets the base URL
 * for all API endpoints to /api/v1.
 *
 */


@ApplicationPath("/api/v1")
public class SmartCampusApplication extends ResourceConfig {

    public SmartCampusApplication() {
        // Scans these packages for JAX-RS resources, filters and providers .
        packages(
                "com.smartcampus.resource",
                "com.smartcampus.exception",
                "com.smartcampus.filter"
        );
        // Registers Jackson for JSON serialisation and deserialization.
        register(org.glassfish.jersey.jackson.JacksonFeature.class);
    }
}