package org.caching.poc.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final int MIN_ATTRIBUTE_LENGTH = 3;
    public static final int MAX_ATTRIBUTE_LENGTH = 300;
    public static final int MIN_BUILD_YEAR = 1900;
    public static final int MAX_BUILD_YEAR = 2024;
    
    public static final String NAME_MUST_BE_PROVIDED = "Name must be provided";
    public static final String COUNTRY_MUST_BE_PROVIDED = "Country must be provided";
    public static final String CITY_MUST_BE_PROVIDED = "City must be provided";
    public static final String ADDRESS_MUST_BE_PROVIDED = "Address must be provided";
    public static final String BUILD_YEAR_MUST_BE_PROVIDED = "Build year must be provided";
    public static final String PRICE_MUST_BE_PROVIDED = "Price must be provided";
    public static final String SIZE_MUST_BE_PROVIDED = "Size in square meters must be provided";

    public static final String NAME_ALLOWED_LENGTH =
            "Name must be between " + MIN_ATTRIBUTE_LENGTH + " and " + MAX_ATTRIBUTE_LENGTH + " characters";
    public static final String CITY_ALLOWED_LENGTH =
            "City must be between " + MIN_ATTRIBUTE_LENGTH + " and " + MAX_ATTRIBUTE_LENGTH + " characters";;
    public static final String ADDRESS_ALLOWED_LENGTH =
            "Address must be between " + MIN_ATTRIBUTE_LENGTH + " and " + MAX_ATTRIBUTE_LENGTH + " characters";;

    public static final String PRICE_MUST_BE_POSITIVE = "Price must be positive";
    public static final String SIZE_MUST_BE_POSITIVE = "Size must be positive";

    public static final String BUILD_YEAR_MUST_BE_AFTER = "Build year must be after " + MIN_BUILD_YEAR;
    public static final String BUILD_YEAR_MUST_BE_BEFORE = "Build year must be before " + MAX_BUILD_YEAR;
}
