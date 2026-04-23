package com.smartcampus.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;
import java.util.logging.Logger;

/**
 * All exception mappers for the Smart Campus API.
 *
 * Each mapper catches a specific exception and converts it into
 * a clean JSON error response. This means the API never leaks
 * raw Java errors to the client - everything is handled cleanly.
 *
 * 409 - Room still has sensors (RoomNotEmptyException)
 * 422 - Referenced roomId doesn't exist (LinkedResourceNotFoundException)
 * 403 - Sensor is under maintenance (SensorUnavailableException)
 * 500 - Anything unexpected (GlobalExceptionMapper catches everything)
 */




@Provider
class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {
    @Override
    public Response toResponse(RoomNotEmptyException e) {
        return Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of(
                        "status", 409,
                        "error", "Conflict",
                        "message", e.getMessage()
                ))
                .build();
    }
}

/**
 * 422 Unprocessable Entity — a value inside the request body
 * references a resource that does not exist.
 *
 * 422 is used instead of 404 because the URL itself is valid.
 * The problem is a bad reference inside the JSON payload, making
 * the request contextually unprocessable despite being syntactically correct.
 */


/**
 * 403 Forbidden — The  sensor is under maintenance.
 */
@Provider
class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {
    
    @Override
    public Response toResponse(SensorUnavailableException e) {
        return Response.status(Response.Status.FORBIDDEN)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of(
                        "status", 403,
                        "error", "Forbidden",
                        "message", e.getMessage()
                ))
                .build();
    }
}

/**
 * 500 Global Safety Net — catches ALL unexpected exceptions.

 * This mapper intercepts everything and returns a clean generic 500
 * so no internal detail ever reaches the client.
 */




@Provider
class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    
    

    private static final Logger ERROR_LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable e) {
        // Log full detail server-side only - never expose to client
        ERROR_LOGGER.severe("Unhandled exception: " + e.getClass().getName() + " — " + e.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of(
                        "status", 500,
                        "error", "Internal Server Error",
                        "message", "An unexpected error occurred. Please contact the system administrator."
                ))
                .build();
    }
}