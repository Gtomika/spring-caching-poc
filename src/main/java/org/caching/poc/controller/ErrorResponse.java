package org.caching.poc.controller;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

public record ErrorResponse(
        MultiValueMap<String, String> errors
) {

    public static ErrorResponse singleError(String key, String value) {
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put(key, List.of(value));
        return new ErrorResponse(map);
    }

}
