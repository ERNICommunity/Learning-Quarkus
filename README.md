# Learning Quarkus

## What's Quarkus?

[Quarkus](https://quarkus.io/) is a Kubernetes-native Java framework designed for building cloud-native applications.
It optimizes Java specifically for containers and provides features that enhance the development experience.

Quarkus focuses on topics like

- fast startup time
- low memory footprint
- developer experience with features like live reloading
- an [extension ecosystem](https://quarkus.io/extensions/) for integrating other frameworks
- GraalVM support for native image generation

Quarkus tries to stay out of your way and relies on existing standards and frameworks.
It doesn't require developers to learn quarkus specific code except for specific things.

While Quarkus is built and is relying on a reactive framework ([Vert.x](https://vertx.io)), it does not require the code
to be reactive as well.
For more infos, see https://quarkus.io/versatility/.

## How to use this repository

1. [Quarkus vs Spring Boot](docs/Quarkus-vs-SpringBoot.md)
2. [Getting Started with Quarkus](docs/0-Getting-Started.md)
3. [Building a Web API with Dependency Injection](docs/1-Web-API.md)