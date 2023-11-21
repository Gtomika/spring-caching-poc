package org.caching.poc.model;

import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record House(
        UUID id,
        String name,
        Country country,
        String city,
        String address,
        BigDecimal priceEuro,
        int buildYear,
        int sizeSquareMeter,
        Instant creationTimestamp
) implements Serializable {
}
