@Path("/messages")
public class MessageResource {

    private final Map<Integer, Message> messages = new HashMap<>();

    @GET
    public Collection<Message> getMessages() {
        return messages.values();
    }

    @POST
    public Response postMessage(Message message) {
        messages.put(message.id, message);
        return Response.status(Response.Status.CREATED).entity(message).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        messages.remove(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    public static class Message {
        public int id;
        public String message;
    }
}
