package org.caching.poc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Collections;

import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

@Profile("caching")
@Configuration
@EnableCaching
public class AppCachingConfig {

    public static final String HOUSES_CACHE = "houses";

    private final int redisTtlMinutes;

    public AppCachingConfig(@Value("${cache.redis.ttl-minutes}") int redisTtlMinutes) {
        this.redisTtlMinutes = redisTtlMinutes;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration redisConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(redisTtlMinutes))
                .serializeKeysWith(fromSerializer(new StringRedisSerializer()))
                //all cached values must implement Serializable
                .serializeValuesWith(fromSerializer(new JdkSerializationRedisSerializer()));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(redisConfig)
                .transactionAware()
                .withCacheConfiguration(HOUSES_CACHE, redisConfig)
                .build();
    }
}
