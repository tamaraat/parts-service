package com.autocare.parts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PartsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PartsServiceApplication.class, args);
    }
}