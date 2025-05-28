# Packaging and Deploying
 
## Build and Packaging
The application can be built and packaged using:

```
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory and is runnable via
`java -jar .\target\quarkus-app\quarkus-run.jar`.

Be aware that it’s not an _uber-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

> **Note:** To run the application in a docker container, the whole `target/quarkus-app` needs to be copied over.

Alternatively an _uber-jar_ can be created (includes all dependencies) by executing the following command:

```
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

It includes all dependencies and is now runnable using

```shell
java -jar target/learning-quarkus-1.0.0-SNAPSHOT-runner.jar
```

### Creating a native executable

You can create a native executable using:

```
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can execute it by dockerizing it (see below).

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Docker

Quarkus provides [docker files](../src/main/docker) when scaffolding. They come with detailed descriptions on how to
build, configure and run them.

### Using Container Extension
You can use one of the [Container Image Extensions](https://quarkus.io/guides/container-image) for building a Jib, Docker, Podman or other container.
For Docker it is `container-image-docker`.
This lets you build a docker file directly via Maven with the parameter `-Dquarkus.container-image.build=true`

```shell
# regular
./mvnw package "-Dquarkus.container-image.build=true"
# native
./mvnw package -Dnative "-Dquarkus.native.container-build=true" "-Dquarkus.container-image.build=true"
```

### Manual JVM variant  
```shell
./mvnw package
docker build -f src/main/docker/Dockerfile.jvm -t quarkus/learning-quarkus-jvm .
docker run -i --rm -e "QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://host.docker.internal/postgres" --name quarkus-jvm quarkus/learning-quarkus-jvm
```

### Manual native variant without GraalVM
**Note:** 
```shell
./mvnw package -Dnative "-Dquarkus.native.container-build=true"
docker build -f src/main/docker/Dockerfile.native -t quarkus/learning-quarkus-native .
docker run -i --rm -e "QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://host.docker.internal/postgres" --name quarkus-native quarkus/learning-quarkus-native
```

When observing both containers via `docker stats`, the following can be observed:

| Variant        | Startup Time  | Memory  |
|----------------|---------------|---------|
| quarkus-jvm    | ~3.5 seconds  | ~240 MB |
| quarkus-native | ~0.15 seconds | ~20 MB  |

### TASK 7.1
> Build and run both a JVM and a native image. Observe the differences.

## Health and Metrics

When deploying in cloud environments, health and metric information is crucial for orchestration.
Both can be added easily via the [smallrye-health](https://quarkus.io/guides/smallrye-health) and [Micrometer](https://quarkus.io/guides/telemetry-micrometer) extensions.