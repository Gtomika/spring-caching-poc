package org.caching.poc.controller.house.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.caching.poc.config.Constants;
import org.caching.poc.model.Country;

@Builder
public record HouseRequest(
        @NotNull(message = Constants.NAME_MUST_BE_PROVIDED)
        @Size(min = Constants.MIN_ATTRIBUTE_LENGTH, max = Constants.MAX_ATTRIBUTE_LENGTH, message = Constants.NAME_ALLOWED_LENGTH)
        String name,

        @NotNull(message = Constants.COUNTRY_MUST_BE_PROVIDED)
        Country country,

        @NotNull(message = Constants.CITY_MUST_BE_PROVIDED)
        @Size(min = Constants.MIN_ATTRIBUTE_LENGTH, max = Constants.MAX_ATTRIBUTE_LENGTH, message = Constants.CITY_ALLOWED_LENGTH)
        String city,

        @NotNull(message = Constants.ADDRESS_MUST_BE_PROVIDED)
        @Size(min = Constants.MIN_ATTRIBUTE_LENGTH, max = Constants.MAX_ATTRIBUTE_LENGTH, message = Constants.ADDRESS_ALLOWED_LENGTH)
        String address,

        @NotNull(message = Constants.PRICE_MUST_BE_PROVIDED)
        @Positive(message = Constants.PRICE_MUST_BE_POSITIVE)
        int priceEuro,

        @NotNull(message = Constants.BUILD_YEAR_MUST_BE_PROVIDED)
        @Min(value = Constants.MIN_BUILD_YEAR, message = Constants.BUILD_YEAR_MUST_BE_AFTER)
        @Max(value = Constants.MAX_BUILD_YEAR, message = Constants.BUILD_YEAR_MUST_BE_BEFORE)
        int buildYear,

        @NotNull(message = Constants.SIZE_MUST_BE_PROVIDED)
        @Positive(message = Constants.SIZE_MUST_BE_POSITIVE)
        int sizeSquareMeter
) {
}
