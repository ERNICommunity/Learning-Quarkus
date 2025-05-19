@ApplicationScoped
public class MessageDAO {

    @Inject
    EntityManager em;

    public List<MessageEntity> find() {
        var query = em.createQuery("SELECT m FROM MessageEntity m", MessageEntity.class);
        return query.getResultList();
    }

    @Transactional
    public MessageEntity save(MessageResource.Message msg) {
        var entity = new MessageEntity(msg.message);
        em.persist(entity);
        return entity;
    }

    @Transactional
    public void delete(int id) {
        var entity = em.find(MessageEntity.class, id);
        if (entity != null) {
            em.remove(entity);
        }
    }
}