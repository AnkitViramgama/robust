package com.modular;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main application class
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.modular.core",
    "com.modular.user",
    "com.modular.role",
    "com.modular.menu",
    "com.modular.plugin"
})
public class ModularApplication {

    private static final Logger logger = LoggerFactory.getLogger(ModularApplication.class);

    public static void main(String[] args) {
        logger.info("Starting Modular Application...");
        SpringApplication.run(ModularApplication.class, args);
        logger.info("Modular Application started successfully!");
    }
}
