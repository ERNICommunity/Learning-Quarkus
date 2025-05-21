# Testing

When scaffolding a Quarkus project, it comes preconfigured and ready for tests.
The `quarkus-junit5` dependency and the `maven-surefire-plugin` plugin are already set up.

General information about testing with Quarkus can be found [here](https://quarkus.io/guides/getting-started-testing).

## Test Watch

Just as with the live reload in dev mode, Quarkus provides a mode to watch for and execute tests on changes with
`quarkus test` / `./mvnw quarkus:test`.

## Unit testing

Since JUnit is available already, any kind of regular unit testing works as expected.

```java
public class GreetingServiceTest {
    private final GreetingService greetingService = new GreetingService();

    @Test
    public void testHello() {
        assertEquals("Hello from Quarkus REST", greetingService.greet());
    }
}
```

Mockito support can be added with the `quarkus-junit5-mockito` extension and works as usual.

```java
@ExtendWith(MockitoExtension.class)
public class GreetingServiceTest {
    @Mock
    OtherService serviceMock;

    @InjectMocks
    GreetingService greetingService;

    @Test
    public void testHello() {
        assertEquals("Hello from Quarkus REST", greetingService.greet());
    }
}
```

### TASK 6.1

> Start the Quarkus test mode in your terminal and add the test without Mockito above. Observe Quarkus executing it.
> Change the code and observe Quarkus rerunning the tests.

## @QuarkusTest

Tests annotated with `@QuarkusTest` are useful for integration testing.
Quarkus starts the application and lets you use all features of dependency injection.

One example test is provided already with
the [GreetingResourceTest](../src/test/java/org/erni/quarkus/GreetingResourceTest.java), accessing the API.

### Test Profile

Per default, Quarkus activates the configuration profile `test`.
The configuration of this profile can be manipulated using the `application.properties` or a separate
`application-test.properties`.

```properties
# within application.properties
%test.quarkus...
# as separate application-test.properties file, e.g. in src/test/resources
quarkus...
```

> **Note:**
> When providing `*.properties` files in the `test/resources` folder, an `application.properties` file is required. Even
> if it's left empty.

### TASK 6.2

> Add a new @QuarkusTest called `PropertyTest`. Define a property in the _regular_ `application.properties`
`erni.custom=main`.
> Inject it into your test and verify that it is equals to `main`.
>
> Override it with a test profile property. Verify that the assertion fails now with the value you configured for the
> test profile.

To change the used profile, implement the `io.quarkus.test.junit.QuarkusTestProfile` and override `getConfigProfile`.

```java
@QuarkusTest
public class GreetingServiceTest implements QuarkusTestProfile {
    @Override
    public String getConfigProfile() {
        return "custom-test";
    }
}
```

A reusable implementation of the `QuarkusTestProfile` can be added to a test with the
`@io.quarkus.test.junit.TestProfile` annotation.

```java
@QuarkusTest
@TestProfile(MyProfile.class)
public class MyProfileTest {
}
```

### Mocking

With the mentioned Mockito extension in place, mocking within a QuarkusTest is very easy. It can be achieved either via
direct mock injection or by providing a mock implementation.

**@InjectMock**

Simply add and annotate the to be mocked bean. It can then be manipulated as usual via methods like `Mockito.when`.

```java
@QuarkusTest
public class GreetingResourceTest {
    @io.quarkus.test.InjectMock
    GreetingService greetingService;
    
    Mockito.when(greetingService.greet()).

    thenReturn("Hello from mock");
}
```

**Mock implementation**

To provide a mock implementation of a class to be used, simply extend the original service or provide an equivalent
interface implementation in the test class path or as a static class within the test. Annotate it with
`@io.quarkus.test.Mock` and `@ApplicationScoped`. Quarkus will recognise it as replacement and inject it accordingly.

This can be very useful for mocking database repositories or code for external API calls.

`@io.quarkus.test.Mock` is just a shortcut for the CDI annotations `@jakarta.enterprise.inject.Alternative` and
`@javax.annotation.Priority(1)`.

```java
@io.quarkus.test.Mock
@ApplicationScoped
public class MockGreetingService extends GreetingService {
    @Override
    public String greet() {
        return "Hello from mock";
    }
}
```

### TASK 6.3

> Append the assertions in the existing `GreetingResourceTest` with asserting the body by
`.body(Matchers.equalTo("Hello from Quarkus REST"))`.
>
> Mock the `GreetingService` either by `@InjectMock` or a (static) mock implementation.
> Observe how it is being picked up and adjust the assertion accordingly.

### Databases

Databases in tests are spun up by Quarkus just like in the dev mode. If not specified otherwise, Quarkus uses
testcontainers to start the configured database.

#### Adjusting Configuration
If not altered, the database configuration is taken from the main configuration. To change it, simply adjust the
properties of the used profile (`test` as default).
In section [5 - Databases](./5-Databases.md) we configured earlier to start up a PostgreSQL on port `5432`. This might
clash when you want to run tests and dev mode in parallel.
To avoid clashes, simply configure the test property `quarkus.datasource.devservices.port=0`, so that a random port is
being used for tests.

To adjust the used database entirely (for example an in memory H2), simply add the respective driver to the `pom.xml` in
`<scope>test</scope>`.
Quarkus will pick it up and configure the datasource by itself. Further configuration can be done manually of course.

#### Mocking Database Access Layer

Mocking or exchanging the database layer is made easy as well. When using the repository pattern, a simple
`@io.quarkus.test.Mock` or `@InjectMock` approach as discussed earlier works as expected.

```java
@QuarkusTest
public class MessageResourceTest {
    @InjectMock
    MessageRepository messageRepository;
}
```

Providing a `@Mock` implementation allows for easy provisioning of existing dummy data but requires a working datasource.
```java
@Mock
@ApplicationScoped
public class MockMessageRepository extends MessageRepository {
    @PostConstruct
    public void init() {
        QuarkusTransaction.requiringNew().run(() -> persist(new Message("Hi"), new Message("Bye")));
    }
}
```

More information can be found [here](https://quarkus.io/guides/hibernate-orm-panache#using-the-repository-pattern).

#### Mocking Active Records

When using the active record pattern, Panache provides mocking support via the `quarkus-panache-mock` extension. 
After adding the dependency, mocks can be activated using the `PanacheMock.mock(<Entity>.class)` method.

```java
@Test
public void mockEntity() {
    PanacheMock.mock(MessageResource.Message.class);
    Mockito.when(MessageResource.Message.count())
            .thenReturn(5L);

    Assertions.assertEquals(5, MessageResource.Message.count());
    Assertions.assertTrue(MessageResource.Message.listAll().isEmpty());
}
```

More information can be found [here](https://quarkus.io/guides/hibernate-orm-panache#using-the-active-record-pattern).

## @QuarkusIntegrationTest

`@QuarkusIntegrationTest` should be used to launch and test the artifact produced by the Quarkus build, and supports
testing a jar (of whichever type), a native image or container image.

More information can be found [here](https://quarkus.io/guides/getting-started-testing#quarkus-integration-test).