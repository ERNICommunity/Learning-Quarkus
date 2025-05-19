@Entity
@Table(name = "Message")
public class MessageEntity {
    @Id
    @GeneratedValue
    public int id;
    @Column
    public String message;
}