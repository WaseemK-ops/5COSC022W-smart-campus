package com.smartcampus.resource;

import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.Room;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collection;
import java.util.Map;

/**
 * Handles all room management operations for the Smart Campus API.
 *
 *
 *
 *
 * Rooms represent physical spaces across Westminster campus buildings.
 * A room cannot be deleted if it still has sensors assigned to it -
 * this prevents orphaned sensors with no valid location reference.
 *
 * DELETE is idempotent - repeated requests leave the server in the
 * same final state (room absent),  even if the status code differs
 * between first call (204) and subsequent calls (404).
 *
 *
 */




@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    // Reference to the shared static room storage ...
    private final Map<String, Room> roomStorage = DataStore.getRooms();

    /**
     * GET /api/v1/rooms
     * Returns a list of all rooms across campus.
     * Full room objects are returned so clients get all details in one request.
     */
    @GET
    public Response getAllRooms() {
        Collection<Room> allRooms = roomStorage.values();
        return Response.ok(allRooms).build();
    }

    /**
     * POST /api/v1/rooms
     * Creates and registers a new room in the system.
     * Returns 201 Created  with a Location header pointing to the new room.
     */
    @POST
    public Response createRoom(Room room) {
        if (room == null || room.getId() == null || room.getId().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of(

                            "status", 400,
                            "error", "Bad Request",
                            "message", "Room ID is required and cannot be blank."
                    ))
                    .build();
        }
        if (roomStorage.containsKey(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of(
                            "status", 409,
                            "error", "Conflict",
                            "message", "A room with ID '" + room.getId() + "' is already registered in the system."
                    ))
                    .build();
        }





        roomStorage.put(room.getId(), room);
        return Response.status(Response.Status.CREATED)
                .header("Location", "/api/v1/rooms/" + room.getId())
                .entity(room)
                .build();
    }





    /**
     * GET /api/v1/rooms/{roomId}
     * Fetches full details for a specific room by its unique ID.
     * Returns 404 if the room does not exist.
     */
    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = roomStorage.get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of(
                            "status", 404,
                            "error", "Not Found",
                            "message", "No room found with ID '" + roomId + "'. Please verify the room ID and try again."
                    ))
                    .build();
        }
        return Response.ok(room).build();
    }



    /**
     * DELETE /api/v1/rooms/{roomId}
     * Decommissions a room from the campus system.
     *
     * Business rule: rooms with active sensors cannot be deleted.
     * This prevents sensor records from becoming orphaned with no
     * valid room reference. A RoomNotEmptyException is thrown which
     * the exception mapper converts to a 409 Conflict response.
     */
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = roomStorage.get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of(
                            "status", 404,
                            "error", "Not Found",
                            "message", "No room found with ID '" + roomId + "'. It may have already been decommissioned."
                    ))
                    .build();
        }









        // Block deletion if room still has sensors assigned
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(roomId);
        }


        roomStorage.remove(roomId);

        return Response.noContent().build(); // 204 No Content
    }
}