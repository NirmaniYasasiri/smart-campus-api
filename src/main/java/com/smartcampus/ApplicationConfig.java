package com.smartcampus;

import com.smartcampus.filter.LoggingFilter;
import com.smartcampus.mapper.GlobalExceptionMapper;
import com.smartcampus.mapper.LinkedResourceNotFoundExceptionMapper;
import com.smartcampus.mapper.RoomNotEmptyExceptionMapper;
import com.smartcampus.mapper.SensorUnavailableExceptionMapper;
import com.smartcampus.resource.DiscoveryResource;
import com.smartcampus.resource.RoomResource;
import com.smartcampus.resource.SensorResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Main JAX-RS application configuration class for the Smart Campus API.
 * This class defines the base path of the API and registers
 * all resource classes, exception mappers, and filters
 * that should be managed by the JAX-RS runtime.
 */
@ApplicationPath("/api/v1")
public class ApplicationConfig extends Application {

    /**
     * Registers all classes that should be part of the API.
     *
     * These include:
     * - resource classes for handling endpoints
     * - exception mapper classes for custom error responses
     * - filter classes for logging requests and responses
     *
     * @return set of classes managed by JAX-RS
     */
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        // Register main API resource classes
        classes.add(DiscoveryResource.class);
        classes.add(RoomResource.class);
        classes.add(SensorResource.class);

        // Register custom exception mappers
        classes.add(RoomNotEmptyExceptionMapper.class);
        classes.add(LinkedResourceNotFoundExceptionMapper.class);
        classes.add(SensorUnavailableExceptionMapper.class);
        classes.add(GlobalExceptionMapper.class);

        // Register request/response logging filter
        classes.add(LoggingFilter.class);

        return classes;
    }
}