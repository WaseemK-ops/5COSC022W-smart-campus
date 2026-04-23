package com.smartcampus;

import com.smartcampus.application.SmartCampusApplication;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.net.URI;
import java.util.logging.Logger;



/**
 * 5COSC022W -   Client Server Architectures Coursework
 * Smart Campus Sensor and Room Management API - IIT CAMPUS has been used an example.
 *
 * Author: Mohamed Waseem  Mohamed Kaleel
 * Student ID : w2120585 | 20232722
 * This is the main entry point of the application.
 * 
 * 
 * It starts an embedded Grizzly HTTP server and deploys
 * the JAX-RS application on port 8080 .
 */


public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    public static final String BASE_URI = "http://0.0.0.0:8080/";

    public static void main(String[] args) throws Exception {
        final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create(BASE_URI),
                new SmartCampusApplication(),
                false
        );
        
               
        server.start();
        LOGGER.info("=================================================");
        LOGGER.info("  Smart Campus API - Mohamed Waseem");
        LOGGER.info("  Running at http://localhost:8080/api/v1");
        LOGGER.info("=================================================");
        Thread.currentThread().join();
    }
}