# Databases

Quarkus supports jdbc and reactive clients with the `quarkus-jdbc-*` and `quarkus-reactive-*-client` extensions.
[JDBC extensions](https://quarkus.io/guides/datasource#configure-a-jdbc-datasource) exist for all established databases
like H2, DB2, PostgreSQL, MySQL, etc..
[Reactive extension](https://quarkus.io/guides/datasource#configure-a-reactive-datasource) are available respectively.

> **"Zero config setup" development mode**
>
> In dev mode, the suggested approach is to let Quarkus handle the database for you.
> Whereas for production, you provide details for an external database.
> This is done via the database [Dev Service](https://quarkus.io/guides/dev-services#databases).
> As soon as an extension for the desired database is added, Quarkus uses in memory, Docker or Podman runtimes to
> provide it on startup.

For more information on connecting to databases, see https://quarkus.io/guides/datasource.

## Configuring a datasource

Whether JDBC or reactive datasource, general parameters can be configured with the `quarkus.datasource.*` application
properties.
JDBC and reactive specific via `quarkus.datasource.jdbc.*` and `quarkus.datasource.reactive.*`.
For an overview about all possible parameters, see
the [reference](https://quarkus.io/guides/datasource#datasource-reference).
URL patterns for the various databases can be found [here](https://quarkus.io/guides/datasource#jdbc-url).

The most common ones to be used will probably be
```properties
# can be omitted if only one driver extension is added
quarkus.datasource.db-kind=pgsql
# usual configuration parameters
quarkus.datasource.username=
quarkus.datasource.password=
quarkus.datasource.jdbc.url=
```

Quarkus provides all relevant beans and connects to the datasource itself.
Quarkus chooses a random port on your machine to bind to the database connection.

> **DevService configuration**
>
> The datasource booted up by Quarkus can be configured with `quarkus.datasource.devservices.*`.
> This is useful in case the local configuration needs to be controlled.
> For example when you want to specify a specific port, the database is listening on so you can connect with a UI
> client.

> **Multiple Datasources**
> 
> Quarkus allows the configuration of multiple data sources that can be used and turned on/off separately.
> For more information on how to do this, see the respective section in
> the [guide](https://quarkus.io/guides/datasource#configure-multiple-datasources).

### TASK 5.1

> Add the quarkus-jdbc-postgresql extension to the project and make sure to have docker running.
> Configure the datasource of the devservices to listen for port 5432 with `quarkus.datasource.devservices.port=5432`.
>
> Observe the logs for a line like
> ```
> ... [io.qua.dat.dep.dev.DevServicesDatasourceProcessor] (build-19) Dev Services for default datasource (postgresql) started ...
> ```
> or your docker environment for the started container.
>
> If you have a UI client, try to connect to the database.

## Managing Data With Hibernate ORM

To read & write data with JDBC and JPA, Hibernate is the de facto standard implementation.
This guide will focus on that, following the respective [Quarkus Guide](https://quarkus.io/guides/hibernate-orm).
For reactive clients, see the [reactive guide](https://quarkus.io/guides/reactive-sql-clients).

As usual, simply add the respective extension `quarkus-hibernate-orm`.
Quarkus will identify and manage entities annotated with `@jakarta.persistence.Entity`.

### TASK 5.2

> Replace the previously created in memory storage of MessageResource with using Hibernate.
> Use the following entity.
> ```java
> @Entity
> @Table(name = "Message")
> public class MessageEntity {
>   @Id
>   @GeneratedValue
>   public int id;
>   @Column
>   public String message;
> }
> ```
> Write a service or DAO to find all, create and delete those messages using the injectable
`jakarta.persistence.EntityManager`.
>
> Feel free to use the code provided in the [solution folder](../docs/solutions/5.2) and configure Hibernate to create
> the tables with `quarkus.hibernate-orm.database.generation=drop-and-create`
> Verify that everything works as expected by creating, fetching and deleting entities.
>
> If you want, you can turn on query logging with `quarkus.hibernate-orm.log.sql=true`.

## Simplifying Hibernate ORM with Panache

Quarkus provides the `quarkus-hibernate-orm-panache` extension to make it easier and more convenient to work with
Hibernate.
It aims at reducing boilerplate code and simplifying queries when dealing Hibernate. Similar to Spring Data.
Panache allows for two approaches, the active record and the repository pattern.

### Active Record

[Guide](https://quarkus.io/guides/hibernate-orm-panache#solution-1-using-the-active-record-pattern)

The idea is to enrich your entities with often used querying and managing methods while providing the option for custom
logic as well.
All on the entity itself. This way, no dedicated data access layer is necessary.
Just extend your entity from `io.quarkus.hibernate.orm.panache.PanacheEntity` to get started.
`PanacheEntity` provides a `Long id` field and recognises all fields as columns directly.

```java

@Entity
@Table(name = "Message")
public class MessageEntity extends PanacheEntity {
    public String message;
}
```

Panache enhances the Entity class with various static and non-static methods to perform operations.
Note that modifying operations still need a provided transaction context.

```java
var msg = new MessageEntity();
msg.message = "Hello World";
msg.persist();
msg.delete();
// -- STATIC OPERATIONS --
MessageEntity.listAll();
MessageEntity.list("message", "Hello"); // query for field with value. allows jpql queries
// all list methods are available as streams as well. they require transactions and should get closed properly (try-with-resource or .close() call)
MessageEntity.findById(1L); // or findByIdOptional
MessageEntity.count(); // or with field query
MessageEntity.update("message = 'Goodbye' where message = ?1","Hello");
MessageEntity.deleteById(1L);
MessageEntity.delete("message", "Hello");
MessageEntity.deleteAll(); // takes optional parameters to delete by field value. like delete("message", "Hello")
...
```

The entities can be [enriched with custom query methods](https://quarkus.io/guides/hibernate-orm-panache#adding-entity-methods) as well, making it easy to reuse common operations.

### TASK 5.3
> Adapt the previous solution to use an active record.

### Repository Pattern

[Guide](https://quarkus.io/guides/hibernate-orm-panache#solution-2-using-the-repository-pattern)

Alternatively to active records is the repository pattern.
By implementing the `PanacheRepository<T>` interface, Panache generates the operations into the implementing class.

The repository can be injected and used as usual.
Custom operations can be defined within the repository similar to the active record approach.

```java
@ApplicationScoped
public class MessageRepository implements PanacheRepository<MessageEntity> {
}
```