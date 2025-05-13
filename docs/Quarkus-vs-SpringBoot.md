# Quarkus vs Spring Boot

Quarkus and Spring Boot are both popular frameworks for building Java applications, but they have different philosophies
and features tailored for specific use cases.

Quarkus is designed with a cloud-native, microservices-first philosophy, focusing on performance and developer
experience in modern environments.
Spring Boot, while also capable of supporting cloud-native applications, has a broader scope and a more established
ecosystem, catering to a wide variety of application types and development styles.

## Key Differences

| Feature                    | Quarkus                                                         | Spring Boot                                                      |
|----------------------------|-----------------------------------------------------------------|------------------------------------------------------------------|
| **Startup Time**           | Extremely fast (milliseconds)                                   | Slower (seconds)                                                 |
| **Memory Footprint**       | Low memory usage                                                | Higher memory usage                                              |
| **Native Image Support**   | Built-in support via GraalVM                                    | Requires additional configuration for native images              |
| **Development Experience** | Live reload support                                             | DevTools for hot swapping                                        |
| **Configuration**          | Configuration via `application.properties` or `application.yml` | Configuration via `application.properties` or `application.yml`  |
| **Dependency Injection**   | Uses CDI (Contexts and Dependency Injection)                    | Uses Spring's own DI framework                                   |
| **Reactive Programming**   | Strong support with Vert.x                                      | Reactive support via Spring WebFlux                              |
| **Microservices**          | Designed for microservices and serverless                       | Also suitable for microservices                                  |
| **Ecosystem**              | Growing ecosystem with extensions                               | Mature ecosystem with a wide range of libraries and integrations |
| **Community**              | Newer community, rapidly growing                                | Established community with extensive resources                   |
| **Learning Curve**         | Steeper for those unfamiliar with CDI                           | Generally easier for those familiar with Spring                  |
| **Testing Support**        | Built-in testing support                                        | Comprehensive testing support                                    |

## Design Philosophy

### Spring Boot

Built on the full Spring ecosystem, it favors broad feature coverage and backward compatibility.
It’s designed for productivity and convention-over-configuration, suitable for many types of Java applications.

### Quarkus

Designed for cloud-native and container-first environments, especially Kubernetes and serverless.
It emphasizes compile-time optimization for performance and memory efficiency.
It's better suited for serverless or scaling microservices.

## Startup Time, Memory Usage And Dependency Injection

### Spring Boot

Uses runtime reflection and classpath scanning, which makes it heavier at startup and higher in memory use.

Most dependency injection and configuration is resolved at runtime.

### Quarkus

Uses compile-time metadata processing and GraalVM native image support, leading to:

- Faster startup times
- Lower memory footprint

It resolves much of its configuration and injection logic at build-time to improve runtime performance.

## API Definition and Dependency Injection

### Spring Boot

Uses Spring DI (annotation-based) and the full power of Spring Core.
Defining an API relies on [Spring MVC](https://docs.spring.io/spring-framework/reference/web.html)
or [Spring WebFlux](https://docs.spring.io/spring-framework/reference/web-reactive.html), but supports JAX-RS.

```java
import org.springframework.stereotype.Service;

@Service
public class GreetingService {
    public String greet(String name) {
        return "Hello, " + name + "!";
    }
}
```

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class GreetingController {
    @Autowired
    private final GreetingService greetingService;

    @GetMapping
    public String greet() {
        this.greetingService.greet("John Doe");
    }
}
```

### Quarkus

Uses CDI (Contexts and Dependency Injection) as defined by Jakarta EE, with extensions for build-time enhancement.

```java
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {
    public String greet(String name) {
        return "Hello, " + name + "!";
    }
}
```

```java
import jakarta.ws.rs.*;

import javax.inject.Inject;

@Path("/")
public class GreetingResource {
    @Inject
    GreetingService greetingService;

    @GET
    public String greet() {
        this.greetingService.greet("John Doe");
    }
}
```

### Spring Boot <> JAX-RS

| **Functionality**             | **Spring Web Annotations** | **JAX-RS Annotations**             |
|-------------------------------|----------------------------|------------------------------------|
| **Controller/Resource Class** | `@Controller`              | `@Path`                            |
| **Request Mapping**           | `@RequestMapping`          | `@Path` (with HTTP method)         |
| **GET Request**               | `@GetMapping`              | `@GET`                             |
| **POST Request**              | `@PostMapping`             | `@POST`                            |
| **PUT Request**               | `@PutMapping`              | `@PUT`                             |
| **DELETE Request**            | `@DeleteMapping`           | `@DELETE`                          |
| **Request Parameter**         | `@RequestParam`            | `@QueryParam`                      |
| **Path Variable**             | `@PathVariable`            | `@PathParam`                       |
| **Request Body**              | `@RequestBody`             | `@Consumes` (with `@POST`/`@PUT`)  |
| **Response Body**             | `@ResponseBody`            | `@Produces`                        |
| **Exception Handling**        | `@ControllerAdvice`        | `@Provider` (with ExceptionMapper) |
| **Session Attributes**        | `@SessionAttributes`       | Not directly supported             |
| **Model Attribute**           | `@ModelAttribute`          | Not directly supported             |
| **Response Status**           | `@ResponseStatus`          | `Response.status()` in method      |

## Databases
