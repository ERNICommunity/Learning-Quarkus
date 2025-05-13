package org.erni.quarkus;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {
    public String greet() {
        return "Hello from Quarkus REST";
    }
}
