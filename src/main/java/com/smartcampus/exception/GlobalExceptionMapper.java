
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 500 Global Safety Net — catches ALL unexpected exceptions.

 * This mapper intercepts everything and returns a clean generic 500
 * so no internal detail ever reaches the client.
 */




@Provider
class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    
    

    private static final Logger ERROR_LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable e) {
        // Log full detail server-side only - never expose to client
        ERROR_LOGGER.severe("Unhandled exception: " + e.getClass().getName() + " — " + e.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of(
                        "status", 500,
                        "error", "Internal Server Error",
                        "message", "An unexpected error occurred. Please contact the system administrator."
                ))
                .build();
    }
}