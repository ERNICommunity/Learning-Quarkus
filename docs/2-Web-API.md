# Building a Web API
To build an API, the following extensions are necessary:
- [quarkus-rest](https://quarkus.io/extensions/io.quarkus/quarkus-rest/) - support for JAX-RS endpoint definitions 
- [quarkus-rest-jackson](https://quarkus.io/extensions/io.quarkus/quarkus-rest-jackson/) - JSON support

To add extensions, either just add the dependency to the pom manually or use either of the following commands:
```
./mvnw quarkus:add-extension -Dextensions="io.quarkus:quarkus-rest-jackson"
```
```
quarkus ext add io.quarkus:quarkus-rest-jackson
```

# Defining endpoints
Since Quarkus relies on JAX-RS definitions, all usual annotations can be used. Excerpt:

| Annotation               | Description                                                                                   |
|-------------------------|-----------------------------------------------------------------------------------------------|
| `@Path`                 | Specifies the URI path for a resource class or method.                                       |
| `@GET`                  | Indicates that the method responds to HTTP GET requests.                                     |
| `@POST`                 | Indicates that the method responds to HTTP POST requests.                                    |
| `@PUT`                  | Indicates that the method responds to HTTP PUT requests.                                     |
| `@DELETE`               | Indicates that the method responds to HTTP DELETE requests.                                  |
| `@PATCH`                | Indicates that the method responds to HTTP PATCH requests.                                   |
| `@PathParam`           | Binds a method parameter to a URI template variable.                                         |
| `@QueryParam`           | Binds a method parameter to a query string parameter.                                        |
| `@DefaultValue`         | Specifies a default value for a parameter if it is not provided in the request.              |

### TASK (1.1)
> Create a JAX-RS resource on the path `/messages` for the following POJO:
> ```java
> public static class Message {
>    public int id;
>    public String message;
> }
> ```
> Create the following endpoints:
> - GET to fetch all messages
> - POST to create a message (respond with 201)
> - DELETE to delete a message by id.
> Ignore any persistence and store created messages in an in memory collection of your choice.
> Solution: [./solutions/1.1](solutions/2.1)

## Validation
Quarkus suggests Hibernate Validator for validating inputs.
It's supported via the [quarkus-hibernate-validator](https://quarkus.io/extensions/io.quarkus/quarkus-hibernate-validator/) extension.

To activate validation, annotate the JAX-RS content (request body, query or path parameter) with `@jakarta.validation.Valid`.
Enrich the parameters or body fields with specific [constraints](https://docs.jboss.org/hibernate/validator/9.0/reference/en-US/html_single/#section-builtin-constraints) depending on the rules you want.
Multiple constraints can be added to one element as usual. 

Example:
```java
@GET
public String hello(@QueryParam("name") @Valid @NotNull String name) {
    return "Hello "+ name +" from Quarkus REST";
}

@POST
public String postGreeting(@Valid @NotNull String message) {
    return message;
}
```

### TASK (1.2)
> Validate the POST body as follows:
> - id should be >= 0 (use @Min)
> - message should not be blank (use @NotBlank) and between 2 and 10 symbols (use @Size)
> 
> Validate the path parameter of the delete method to be >= 0