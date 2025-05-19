package org.erni;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Message")
public class MessageEntity extends PanacheEntity {
    public String message;
}
