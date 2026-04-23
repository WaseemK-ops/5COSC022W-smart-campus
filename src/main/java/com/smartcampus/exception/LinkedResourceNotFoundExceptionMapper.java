package com.smartcampus.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;








/**
 * 422 Unprocessable Entity — a value inside the request body
 * references a resource that does not exist.
 *
 * 422 is used instead of 404 because the URL itself is valid.
 * The problem is a bad reference inside the JSON payload, making
 * the request contextually unprocessable despite being syntactically correct.
 */
 

@Provider
public class LinkedResourceNotFoundExceptionMapper 
    implements ExceptionMapper<LinkedResourceNotFoundException> {
    
    @Override
    public Response toResponse(LinkedResourceNotFoundException e) {
        return Response.status(422)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of(
                    "status", 422,
                    "error", "Unprocessable Entity",
                    "message", e.getMessage()
                ))
                .build();
    }
}