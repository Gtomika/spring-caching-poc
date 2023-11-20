package org.caching.poc.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

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
) {
}
