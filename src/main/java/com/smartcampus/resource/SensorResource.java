package com.smartcampus.resource;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.model.ApiError;
import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.store.DataStore;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Comparator;

/**
 * Resource class responsible for sensor-related API operations.
 * Supports listing sensors, filtering by type, creating sensors,
 * fetching one sensor, deleting a sensor, and locating nested readings.
 */
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    /**
     * Returns all sensors, or filters them by type if a query parameter is provided.
     *
     * @param type optional sensor type filter
     * @return list of sensors
     */
    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> sensors = new ArrayList<>(DataStore.getSensors().values());

        if (type != null && !type.trim().isEmpty()) {
            sensors = sensors.stream()
                    .filter(sensor -> sensor.getType() != null
                            && sensor.getType().equalsIgnoreCase(type.trim()))
                    .collect(Collectors.toList());
        }

        sensors.sort(Comparator.comparing(Sensor::getId, String.CASE_INSENSITIVE_ORDER));

        return Response.ok(sensors).build();
    }

    /**
     * Creates a new sensor after validating the payload and linked room.
     *
     * @param sensor sensor request body
     * @param uriInfo request URI context used to build Location header
     * @return created sensor with HTTP 201
     */
    @POST
    public Response createSensor(Sensor sensor, @Context UriInfo uriInfo) {
        validateSensorPayload(sensor);

        sensor.setId(sensor.getId().trim());
        sensor.setType(sensor.getType().trim());
        sensor.setStatus(sensor.getStatus().trim().toUpperCase());
        sensor.setRoomId(sensor.getRoomId().trim());

        Map<String, Sensor> sensorMap = DataStore.getSensors();
        Map<String, Room> roomMap = DataStore.getRooms();

        // Prevent duplicate sensor IDs
        if (sensorMap.containsKey(sensor.getId())) {
            ApiError error = new ApiError(
                    409,
                    "SensorAlreadyExists",
                    "A sensor with ID '" + sensor.getId() + "' already exists."
            );

            return Response.status(Response.Status.CONFLICT)
                    .entity(error)
                    .build();
        }

        // Validate that the referenced room exists
        Room room = roomMap.get(sensor.getRoomId());

        if (room == null) {
            throw new LinkedResourceNotFoundException(
                    "Cannot create sensor because room ID '" + sensor.getRoomId() + "' does not exist."
            );
        }

        sensorMap.put(sensor.getId(), sensor);
        DataStore.getOrCreateReadingsList(sensor.getId());

        // Add the sensor ID to the linked room if not already present
        if (!room.getSensorIds().contains(sensor.getId())) {
            room.getSensorIds().add(sensor.getId());
        }

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(sensor.getId())
                .build();

        return Response.created(location)
                .entity(sensor)
                .build();
    }

    /**
     * Returns one sensor by ID.
     *
     * @param sensorId sensor identifier
     * @return sensor if found
     */
    @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.getSensors().get(sensorId);

        if (sensor == null) {
            throw new NotFoundException("Sensor with ID '" + sensorId + "' was not found.");
        }

        return Response.ok(sensor).build();
    }

    /**
     * Deletes a sensor and removes its references from the room and reading history.
     *
     * @param sensorId sensor identifier
     * @return HTTP 204 if deleted successfully
     */
    @DELETE
    @Path("/{sensorId}")
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        Map<String, Sensor> sensors = DataStore.getSensors();
        Sensor sensor = sensors.get(sensorId);

        if (sensor == null) {
            throw new NotFoundException("Sensor with ID '" + sensorId + "' was not found.");
        }

        Room room = DataStore.getRooms().get(sensor.getRoomId());

        // Remove sensor reference from its linked room
        if (room != null && room.getSensorIds() != null) {
            room.getSensorIds().remove(sensorId);
        }

        sensors.remove(sensorId);
        DataStore.getReadingsBySensor().remove(sensorId);

        return Response.noContent().build();
    }

    /**
     * Sub-resource locator for sensor readings.
     *
     * This delegates nested reading logic to SensorReadingResource.
     *
     * @param sensorId sensor identifier
     * @return nested resource for readings
     */
    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }

    /**
     * Validates the incoming sensor payload.
     *
     * @param sensor sensor request body
     */
    private void validateSensorPayload(Sensor sensor) {
        if (sensor == null) {
            throw new BadRequestException("Request body is required.");
        }

        if (isBlank(sensor.getId())) {
            throw new BadRequestException("Sensor ID is required.");
        }

        if (isBlank(sensor.getType())) {
            throw new BadRequestException("Sensor type is required.");
        }

        if (isBlank(sensor.getStatus())) {
            throw new BadRequestException("Sensor status is required.");
        }

        if (isBlank(sensor.getRoomId())) {
            throw new BadRequestException("roomId is required.");
        }

        String status = sensor.getStatus().trim().toUpperCase();

        if (!status.equals("ACTIVE")
                && !status.equals("MAINTENANCE")
                && !status.equals("OFFLINE")) {
            throw new BadRequestException("Sensor status must be ACTIVE, MAINTENANCE, or OFFLINE.");
        }
    }

    /**
     * Checks whether a string is null or empty after trimming.
     *
     * @param value input string
     * @return true if blank
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}