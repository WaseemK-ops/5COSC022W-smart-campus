package com.smartcampus.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.logging.Logger;




/**
 * Cross-cutting logging filter for the Smart Campus API.
 *
 * Implements both ContainerRequestFilter and ContainerResponseFilter
 * to log every incoming request and every outgoing response.
 *
 * Why use a filter instead of Logger.info() in every resource method?
 * - One class covers the entire API - no chance of missing an endpoint
 * - Resource classes stay focused on business logic only
 * - Filters run even when exceptions are thrown and caught by mappers
 * - Easy to modify log format in one place without touching any resource
 */
@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger API_LOGGER = Logger.getLogger(LoggingFilter.class.getName());

    /**
     * It logs every incoming HTTP request.
     * Captures the method (GET, POST etc.) and  the  full request URI .
     */
    
    
    
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        API_LOGGER.info(String.format(">> REQUEST  [%s] %s",
                requestContext.getMethod(),
                requestContext.getUriInfo().getRequestUri()));
    }

    /**
     * Logs  every outgoing HTTP response.
     * It captures the final status code returned to the client .
     */
    
    
    
    @Override
    
    public void filter(ContainerRequestContext httpRequest,
                       ContainerResponseContext httpResponse) throws IOException {
        API_LOGGER.info(String.format("<< RESPONSE [%s] %d %s",
                httpRequest.getMethod(),
                httpResponse.getStatus(),
                httpRequest.getUriInfo().getRequestUri()));
    }
    
}