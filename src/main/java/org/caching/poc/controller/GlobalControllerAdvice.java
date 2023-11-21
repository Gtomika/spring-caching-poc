package org.caching.poc.controller;

import org.caching.poc.exception.CacheNotFoundException;
import org.caching.poc.exception.HouseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        LinkedMultiValueMap<String, String> errors = new LinkedMultiValueMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            putIntoMultiMap(errors, fieldName, errorMessage);
        });
        return new ErrorResponse(errors);
    }

    private void putIntoMultiMap(MultiValueMap<String, String> map, String key, String value) {
        if(map.containsKey(key)) {
            var currentValues = map.get(key);
            currentValues.add(value);
            map.put(key, currentValues);
        } else {
            map.put(key, List.of(value));
        }
    }

    @ExceptionHandler(HouseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleHouseNotFoundException(HouseNotFoundException e) {
        return ErrorResponse.singleError("house", e.getMessage());
    }

    @ExceptionHandler(CacheNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCacheNotFoundException(CacheNotFoundException e) {
        return ErrorResponse.singleError("cache", e.getMessage());
    }

}
