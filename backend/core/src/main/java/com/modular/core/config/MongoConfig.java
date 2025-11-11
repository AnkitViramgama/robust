package com.modular.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB configuration
 */
@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.modular")
public class MongoConfig {
}
