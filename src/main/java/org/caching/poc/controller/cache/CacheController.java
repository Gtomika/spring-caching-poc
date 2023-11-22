package org.caching.poc.controller.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.caching.poc.exception.CacheNotFoundException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Profile("caching")
@RestController
@RequestMapping("/api/v1/cache")
@RequiredArgsConstructor
public class CacheController {

    private final CacheManager cacheManager;

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCache(@RequestParam String cacheName) {
        /*
        Some cache providers, such as Redis, are capable of creating cache
        as needed. In case of these providers, the returned value will never be null.
         */
        Cache cache = cacheManager.getCache(cacheName);
        if(cache == null) {
            throw new CacheNotFoundException(cacheName);
        }
        cache.clear();
        log.info("Cache '{}' has been cleared", cacheName);
    }

}
