package org.caching.poc.config;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Cache related configurations. Spring Boot registers a ConcurrentMapCacheManager by default
 * when the {@link EnableCaching} annotation is present.
 */
@Configuration
@EnableCaching
public class AppCachingConfig implements CacheManagerCustomizer<ConcurrentMapCacheManager> {

    public static final String HOUSES_CACHE = "houses";

    /**
     * This method allows for the customization of a cache manager.
     * @param cacheManager the {@code CacheManager} to customize
     */
    @Override
    public void customize(ConcurrentMapCacheManager cacheManager) {
        cacheManager.setCacheNames(List.of(HOUSES_CACHE));
    }
}
