package com.smartcampus.application;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api/v1")
public class SmartCampusApplication extends ResourceConfig {

    public SmartCampusApplication() {
        packages(
                "com.smartcampus.resource",
                "com.smartcampus.exception",
                "com.smartcampus.filter"
        );
        register(org.glassfish.jersey.jackson.JacksonFeature.class);
    }
}