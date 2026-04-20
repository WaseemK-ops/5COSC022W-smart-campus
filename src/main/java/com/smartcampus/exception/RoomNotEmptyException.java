package com.smartcampus.exception;

/**
 * Thrown when a deletion is attempted on a room that still
 * has sensors assigned to it.
 * Mapped to HTTP 409 Conflict by RoomNotEmptyExceptionMapper.
 */
public class RoomNotEmptyException extends RuntimeException {
    public RoomNotEmptyException(String roomId) {
        super("Room '" + roomId + "' cannot be decommissioned: it still has sensors actively " +
                "assigned to it. Please reassign or remove all sensors before deleting this room.");
    }
}