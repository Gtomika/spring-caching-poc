package house.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record HouseEventResponse(
        String id,
        String name,
        String country,
        String city,
        String address,
        double priceEuro,
        int buildYear,
        int sizeSquareMeter,
        String creationTimestamp
) {
}
