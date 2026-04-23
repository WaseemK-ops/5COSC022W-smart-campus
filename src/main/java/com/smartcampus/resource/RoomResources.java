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
 
 *Handles all the  room management operations for the Smart Campus API.
 * campusRooms represent physical spaces across the campus buildings.
 * A room cannot be deleted if it still has sensors assigned to it -
 * this prevents the left out sensors with no valid location references.
 *
 * DELETE is unchanged   - It is just repeated requests which leave the server in the
 * same final state (room absent),  even if the status code differs
 * between first call (204) and subsequent calls (404).

 */




@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResources {
    

    // References to the shared static room storage ...
    private final Map<String, Room> roomStorage = DataStore.getRooms();

    /**
     * GET /api/v1/rooms
     * Returns a list of all rooms across campus.
     * Full room objects are returned so clients get all details in one request.
     */
    
    @GET
    public Response getAllRooms() {
        Collection<Room> campusRooms = roomStorage.values();
        return Response.ok(campusRooms).build();
    }

    /**
     * POST /api/v1/rooms
     * Creates and registers a new room in the system.
     * Returns 201 Created  with a Location header pointing to the new room.
     */
    @POST
    public Response createRoom(Room campusRoom) {
        if (campusRoom == null || campusRoom.getId() == null || campusRoom.getId().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of(

                            "status", 400,
                            "error", "Bad Request",
                            "message", "Room ID is required and cannot be blank."
                    ))
                    .build();
        }
        if (roomStorage.containsKey(campusRoom.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("status", 409,
                            "error", "Conflict",
                            "message", "A room with ID '" + campusRoom.getId() + "' is already registered in the system."
                    ))
                    .build();
        }





        roomStorage.put(campusRoom.getId(), campusRoom);
        return Response.status(Response.Status.CREATED)
                .header("Location", "/api/v1/rooms/" + campusRoom.getId())
                .entity(campusRoom)
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
     * The rooms with active sensors cannot be deleted.
     * This prevents sensor records from becoming left out with no
     * valid room reference. 
     * so,a  RoomNotEmptyException is thrown which
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









        // Block deletion if room still has sensors assigned...
        if (!room.getDeployedSensorIds().isEmpty()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of(
                        "status", 409,
                        "error", "Conflict",
                        "message", "Room '" + roomId + "' cannot be deleted because it still has active sensors. Please remove or reassign sensors first."
                    ))
                    .build();
        }

        // If no sensors, proceed with removal
        roomStorage.remove(roomId);
        
        return Response.noContent().build(); // 204 No Content
    }
}