package com.smartcampus;

import com.smartcampus.application.SmartCampusApplication;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.net.URI;
import java.util.logging.Logger;

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