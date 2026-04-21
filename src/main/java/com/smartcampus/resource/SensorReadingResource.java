package com.smartcampus.resource;

import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.store.DataStore;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Resource class responsible for reading history of a specific sensor.
 * This class is reached through the nested route:
 * /sensors/{sensorId}/readings
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    /** Sensor ID from the parent resource path */
    private final String sensorId;

    /**
     * Creates a reading resource for one specific sensor.
     *
     * @param sensorId sensor identifier
     */
    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    /**
     * Returns all readings for the current sensor.
     *
     * @return list of readings
     */
    @GET
    public Response getReadings() {
        ensureSensorExists();

        List<SensorReading> readings = new ArrayList<>(DataStore.getOrCreateReadingsList(sensorId));
        readings.sort(Comparator.comparingLong(SensorReading::getTimestamp));

        return Response.ok(readings).build();
    }

    /**
     * Adds a new reading for the current sensor.
     * This method blocks readings for maintenance sensors,
     * auto-generates missing reading IDs and timestamps,
     * prevents duplicate reading IDs for the same sensor,
     * and updates the parent sensor's currentValue.
     *
     * @param reading reading request body
     * @param uriInfo request URI context used to build Location header
     * @return created reading with HTTP 201
     */
    @POST
    public Response addReading(SensorReading reading, @Context UriInfo uriInfo) {
        Sensor sensor = ensureSensorExists();

        // Block readings for sensors that are currently under maintenance
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(
                    "Sensor '" + sensorId + "' is in MAINTENANCE mode and cannot accept new readings."
            );
        }

        if (reading == null) {
            throw new BadRequestException("Request body is required.");
        }

        List<SensorReading> readings = DataStore.getOrCreateReadingsList(sensorId);

        // Generate a unique ID automatically if the client does not provide one
        if (reading.getId() == null || reading.getId().trim().isEmpty()) {
            reading.setId(UUID.randomUUID().toString());
        } else {
            reading.setId(reading.getId().trim());

            // Prevent duplicate reading IDs for the same sensor
            boolean duplicateId = readings.stream()
                    .anyMatch(existing -> existing.getId().equals(reading.getId()));

            if (duplicateId) {
                throw new BadRequestException("Reading ID already exists for this sensor.");
            }
        }

        // Generate current time if timestamp is missing or invalid
        if (reading.getTimestamp() <= 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        readings.add(reading);

        // Update the parent sensor's latest value
        sensor.setCurrentValue(reading.getValue());

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(reading.getId())
                .build();

        return Response.created(location)
                .entity(reading)
                .build();
    }

    /**
     * Ensures the parent sensor exists before handling reading operations.
     *
     * @return sensor if found
     */
    private Sensor ensureSensorExists() {
        Sensor sensor = DataStore.getSensors().get(sensorId);

        if (sensor == null) {
            throw new NotFoundException("Sensor with ID '" + sensorId + "' was not found.");
        }

        return sensor;
    }
}