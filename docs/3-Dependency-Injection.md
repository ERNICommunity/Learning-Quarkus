# Dependency Injection

Dependency Injection (DI) is a design pattern that allows developers to create loosely coupled applications.
In Quarkus, DI is achieved through the use of CDI, which provides a way to manage the lifecycle of beans (objects) and their dependencies.

>**Note:** Other than frameworks like Spring, Quarkus resolves injections and dependencies at **build time** instead of **run time**.
This is one of the main mechanisms to allow faster startup time.

Quarkus fully leverages the Jakarta standard for defining and injecting beans.
`@jakarta.enterprise.context.ApplicationScoped` and `@jakarta.inject.Inject` will be the most used annotations here.
Quarkus advertises to use package private visibility for injected properties.
However, constructor injection and setter injection are fully supported as well.

For more and deeper information, see [Quarkus CDI Guide](https://quarkus.io/guides/cdi)

```java
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class InjectableBean {
    public String hello() {
        return "Hello";
    }
}
@ApplicationScoped
public class TargetBean {
    @Inject
    InjectableBean bean;
    public void hello() {
        System.out.println(bean.hello());
    }
}
```
### TASK 3.1
> Extend the existing `GreetingResource` with an injected `GreetingService`.
> Annotate the resource and service accordingly. Verify that everything works as before.
> 
> **Note:** JAX-RS resources are handled as beans by quarkus already. There is no need to annotate them for CDI.
>
> ```java
> public class GreetingService {
>   public String greet() {
>     return "Hello from Quarkus REST";
>   }
> }
> ```