@Path("/messages")
public class MessageResource {
    @Inject
    MessageDAO messageDAO;

    @GET
    public Collection<MessageEntity> getMessages() {
        return messageDAO.find();
    }

    @POST
    public Response postMessage(@Valid Message msg) {
        var entity = messageDAO.save(msg.message);
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        messageDAO.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    public static class Message {
        @NotBlank
        public String message;
    }
}