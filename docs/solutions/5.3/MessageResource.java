@Path("/messages")
public class MessageResource {

    @GET
    public Collection<MessageEntity> getMessages() {
        return MessageEntity.listAll();
    }

    @POST
    @Transactional
    public Response postMessage(@Valid Message msg) {
        var entity = new MessageEntity();
        entity.message = msg.message;
        entity.persist();
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") int id) {
        MessageEntity.deleteById(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    public static class Message {
        @NotBlank
        public String message;
    }
}
