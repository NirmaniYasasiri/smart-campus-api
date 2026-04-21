package com.smartcampus.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Discovery endpoint for the API root.
 * This provides basic metadata about the API and links
 * to the main top-level resources.
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    /**
     * Returns API metadata including version and top-level links.
     *
     * @return JSON response with API information
     */
    @GET
    public Response getDiscovery() {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("name", "Smart Campus Sensor & Room Management API");
        response.put("version", "v1");
        response.put("adminContact", "w2120471@westminster.ac.uk");

        Map<String, String> resources = new LinkedHashMap<>();
        resources.put("self", "/api/v1");
        resources.put("rooms", "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");

        response.put("resources", resources);

        return Response.ok(response).build();
    }
}