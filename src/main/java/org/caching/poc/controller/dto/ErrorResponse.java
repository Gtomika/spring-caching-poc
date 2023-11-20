package org.caching.poc.controller.dto;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors
) {

    public static ErrorResponse singleError(String key, String value) {
        return new ErrorResponse(Map.of(key, value));
    }

}
