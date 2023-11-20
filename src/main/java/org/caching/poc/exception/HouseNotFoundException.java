package org.caching.poc.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class HouseNotFoundException extends RuntimeException {

    private final UUID houseId;

    public HouseNotFoundException(UUID houseId) {
        super("House with ID '%s' not found".formatted(houseId));
        this.houseId = houseId;
    }

}
