package com.smartcampus.mapper;

import com.smartcampus.model.ApiError;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Global safety-net exception mapper.
 *
 * This ensures the API never exposes raw stack traces or default server error pages.
 * It converts unexpected errors into safe JSON responses.
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        // Log the full exception internally for debugging
        LOGGER.log(Level.SEVERE, "Unhandled exception in API", exception);

        // Preserve known HTTP errors but still return them as JSON
        if (exception instanceof WebApplicationException) {
            WebApplicationException webException = (WebApplicationException) exception;

            int status = webException.getResponse() != null
                    ? webException.getResponse().getStatus()
                    : 500;

            String reason = Response.Status.fromStatusCode(status) != null
                    ? Response.Status.fromStatusCode(status).getReasonPhrase()
                    : "Error";

            ApiError error = new ApiError(
                    status,
                    reason.replace(" ", ""),
                    webException.getMessage() != null ? webException.getMessage() : reason
            );

            return Response.status(status)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error)
                    .build();
        }

        // Return a generic safe message for unexpected server-side failures
        ApiError error = new ApiError(
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                "InternalServerError",
                "An unexpected error occurred. Please contact the administrator."
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}