# Configurations

Quarkus provides a flexible and powerful configuration system that allows you to customize various aspects of your
application.
Configuration can be passed as environment variables or
using [application.properties](../src/main/resources/application.properties).

In case the configuration is wanted in `yaml` format, see
[YAML configuration guide](https://quarkus.io/guides/config-yaml).

Some common configuration options are

```properties
# The HTTP server port.
quarkus.http.port=8080
# The HTTP root path
quarkus.http.root-path=/api
# Database configurations
quarkus.datasource.db-kind=
quarkus.datasource.jdbc.url=
quarkus.datasource.username=
quarkus.datasource.password=
# Logging
quarkus.log.level=INFO
quarkus.log.console.enable=true
```

Quarkus allows

- [predefined properties per extension](https://quarkus.io/guides/all-config)
- [defining profile specific configurations](https://quarkus.io/guides/config-reference#profiles)
- [injection or manual access to properties](https://quarkus.io/guides/config-reference#property-expressions)
- [property expressions](https://quarkus.io/guides/config-reference#property-expressions)
- and more

See the [Configuration Reference Guide](https://quarkus.io/guides/config-reference) for a detailed description.

> Note: Some Quarkus configurations only take effect during build time, meaning it is not possible to change them at
> runtime.
> See [here](https://quarkus.io/guides/config-reference#build-time-configuration) for more information.

## Configuration Profiles

Different configuration profiles can be provided either collectively in the `application.properties` or in separated
`application-<profile>.properties` files.

```properties
# within application.properties
%dev.quarkus.http.port=3000
# as separate application-dev.properties file
quarkus.http.port=3000
```

## Providing And Overriding Properties At Runtime

When starting the jar or deploying the application, it is often useful to provide or override properties.
Quarkus accepts properties through environment variables in upper snake case.
A common hierarchy of property sources exists, see [here](https://quarkus.io/guides/config-reference#configuration-sources).

```properties
# activate profile(s)
QUARKUS_PROFILE=dev,dev2
# overriding the quarkus.http.port property
QUARKUS_HTTP_PORT=3001
# overriding profile specific variable %dev.quarkus.http.port
_DEV_QUARKUS_HTTP_PORT=3001
```

## Injecting Properties

To access properties in your code, injection via `@org.eclipse.microprofile.config.inject.ConfigProperty` can be used.

```java
@ConfigProperty(name = "erni.custom")
String prop;
```

[Programmatic access](https://quarkus.io/guides/config-reference#programmatically-access) is possible as well.

# Logging

Quarkus relies on the JBoss Logging API. To declare a logger, simply use the API:
```java
import org.jboss.logging.Logger;
private static final Logger log =  Logger.getLogger(MyService.class);
```

Quarkus simplifies this by providing the `io.quarkus.logging.Log` interface.
Simply use the `Log` class to add logging statements and Quarkus adds a JBoss logger field at build time.
```java
public void doSomething() {
  io.quarkus.logging.Log.info("Simple!"); 
}
```

> **Note:** For extension development, using the JBoss API is required.

Logging behaviour can be configured with the `quarkus.log.*` properties.

For more in depth information, see the [Logging Guide](https://quarkus.io/guides/logging).