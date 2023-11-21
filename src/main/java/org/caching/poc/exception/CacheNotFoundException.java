package org.caching.poc.exception;

import lombok.Getter;

@Getter
public class CacheNotFoundException extends RuntimeException {

    private final String cacheName;

    public CacheNotFoundException(String cacheName) {
        super("Cache with name '%s' not found".formatted(cacheName));
        this.cacheName = cacheName;
    }
}
