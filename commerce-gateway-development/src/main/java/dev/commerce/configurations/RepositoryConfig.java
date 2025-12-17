package dev.commerce.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "dev.commerce.repositories.jpa")
@EnableRedisRepositories(basePackages = "dev.commerce.repositories.redis")
public class RepositoryConfig {
}
