package com.smartcampus.resource;

import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.ApiError;
import com.smartcampus.model.Room;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Comparator;

/**
 * Resource class responsible for room-related API operations.
 * Supports listing rooms, creating rooms, fetching one room,
 * and deleting a room when business rules allow it.
 */
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    /**
     * Returns all rooms currently stored in memory.
     *
     * @return list of rooms
     */
    @GET
    public Response getAllRooms() {
        List<Room> rooms = new ArrayList<>(DataStore.getRooms().values());
        rooms.sort(Comparator.comparing(Room::getId, String.CASE_INSENSITIVE_ORDER));
        return Response.ok(rooms).build();
    }

    /**
     * Creates a new room after validating the request body.
     *
     * @param room room to create
     * @param uriInfo request URI context used to build Location header
     * @return created room with HTTP 201
     */
    @POST
    public Response createRoom(Room room, @Context UriInfo uriInfo) {
        validateRoomPayload(room);

        room.setId(room.getId().trim());
        room.setName(room.getName().trim());

        Map<String, Room> rooms = DataStore.getRooms();

        // Prevent duplicate room IDs
        if (rooms.containsKey(room.getId())) {
            ApiError error = new ApiError(
                    409,
                    "RoomAlreadyExists",
                    "A room with ID '" + room.getId() + "' already exists."
            );

            return Response.status(Response.Status.CONFLICT)
                    .entity(error)
                    .build();
        }

        // Ensure sensor ID list is initialized
        if (room.getSensorIds() == null) {
            room.setSensorIds(new CopyOnWriteArrayList<>());
        }

        rooms.put(room.getId(), room);

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(room.getId())
                .build();

        return Response.created(location)
                .entity(room)
                .build();
    }

    /**
     * Returns one room by its ID.
     *
     * @param roomId room identifier
     * @return room if found
     */
    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = DataStore.getRooms().get(roomId);

        if (room == null) {
            throw new NotFoundException("Room with ID '" + roomId + "' was not found.");
        }

        return Response.ok(room).build();
    }

    /**
     * Deletes a room if it exists and has no assigned sensors.
     *
     * @param roomId room identifier
     * @return HTTP 204 if deleted successfully
     */
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Map<String, Room> rooms = DataStore.getRooms();
        Room room = rooms.get(roomId);

        if (room == null) {
            throw new NotFoundException("Room with ID '" + roomId + "' was not found.");
        }

        // Business rule: do not allow deleting a room that still has assigned sensors
        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                    "Room '" + roomId + "' cannot be deleted because it still has sensors assigned."
            );
        }

        rooms.remove(roomId);

        return Response.noContent().build();
    }

    /**
     * Validates the incoming room payload.
     *
     * @param room room request body
     */
    private void validateRoomPayload(Room room) {
        if (room == null) {
            throw new BadRequestException("Request body is required.");
        }

        if (isBlank(room.getId())) {
            throw new BadRequestException("Room ID is required.");
        }

        if (isBlank(room.getName())) {
            throw new BadRequestException("Room name is required.");
        }

        if (room.getCapacity() <= 0) {
            throw new BadRequestException("Room capacity must be greater than 0.");
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