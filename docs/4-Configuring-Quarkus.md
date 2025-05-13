# Configuring Quarkus

Quarkus provides a flexible and powerful configuration system that allows you to customize various aspects of your
application.
Configuration can be passed as environment variables or using [application.properties](../src/main/resources/application.properties).

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

> Note: Some Quarkus configurations only take effect during build time, meaning it is not possible to change them at runtime.
> See [here](https://quarkus.io/guides/config-reference#build-time-configuration) for more information.

