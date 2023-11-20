package org.caching.poc.controller.dto;

import org.caching.poc.model.Country;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record HouseResponse(
        UUID id,
        String name,
        Country country,
        String city,
        String address,
        BigDecimal priceEuro,
        int buildYear,
        int sizeSquareMeter,
        Instant creationTimestamp
) {
}
