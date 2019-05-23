package com.dias.services.mnemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.dias.services.mnemo"})
public class SchemasApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchemasApplication.class, args);
    }
}
