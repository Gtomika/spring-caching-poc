package org.caching.poc.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.caching.poc.model.Country;

public record HouseRequest(
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String name,

        @NotNull(message = "Country must be provided")
        Country country,

        @Size(min = 3, max = 100, message = "City must be between 3 and 100 characters")
        String city,

        @Size(min = 3, max = 100, message = "Address must be between 3 and 100 characters")
        String address,

        @Positive(message = "Price must be positive")
        int priceEuro,

        @Min(value = 1900, message = "Build year must be after 1900")
        @Max(value = 2024, message = "Build year must be before 2024")
        int buildYear,

        @Positive(message = "House size in square meters must be positive")
        int sizeSquareMeter
) {
}
