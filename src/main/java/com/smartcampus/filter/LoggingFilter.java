package com.smartcampus.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Logs every incoming HTTP request and outgoing HTTP response.
 *
 * This is useful for API observability and debugging.
 */
@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

    /**
     * Logs method and URI for every incoming request.
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        LOGGER.info(() -> "Incoming Request -> Method: "
                + requestContext.getMethod()
                + ", URI: "
                + requestContext.getUriInfo().getRequestUri());
    }

    /**
     * Logs method, URI, and status for every outgoing response.
     */
    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {
        LOGGER.info(() -> "Outgoing Response -> Method: "
                + requestContext.getMethod()
                + ", URI: "
                + requestContext.getUriInfo().getRequestUri()
                + ", Status: "
                + responseContext.getStatus());
    }
}