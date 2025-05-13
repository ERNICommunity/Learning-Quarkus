package org.erni.quarkus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.*;

@Path("/messages")
public class MessageResource {

    private final Map<Integer, Message> messages = new HashMap<>();

    @GET
    public Collection<Message> getMessages() {
        return messages.values();
    }

    @POST
    public Response postMessage(@Valid Message message) {
        messages.put(message.id, message);
        return Response.status(Response.Status.CREATED).entity(message).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") @Valid @Min(0) int id) {
        messages.remove(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    public static class Message {
        @Min(0)
        public int id;
        @NotBlank
        @Size(min = 2, max = 10)
        public String message;
    }
}
