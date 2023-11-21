package org.caching.poc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {

    private final String redisHost;
    private final int redisPort;
    private final String redisPassword;

    public RedisConfig(
            @Value("${cache.redis.host}") String redisHost,
            @Value("${cache.redis.port}") int redisPort,
            @Value("${cache.redis.password}") String redisPassword
    ) {
        this.redisHost = redisHost;
        this.redisPort = redisPort;
        this.redisPassword = redisPassword;
    }

    @Bean
    public RedisConnectionFactory lettuceConnectionFactory() {
        var redisStandaloneConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        redisStandaloneConfig.setPassword(redisPassword);
        return new LettuceConnectionFactory(redisStandaloneConfig);
    }

}
