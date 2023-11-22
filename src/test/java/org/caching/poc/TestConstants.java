package org.caching.poc;

import org.caching.poc.controller.house.dto.HouseResponse;
import org.caching.poc.model.Country;
import org.caching.poc.model.House;
import org.caching.poc.repository.entity.HouseEntity;

import java.math.BigDecimal;
import java.util.UUID;

public class TestConstants {

    private TestConstants() {}

    public static final House HOUSE = House.builder()
            .id(UUID.randomUUID())
            .name("TEST HOUSE")
            .country(Country.AUSTRIA)
            .city("Wien")
            .address("Some avenue 5")
            .buildYear(2012)
            .priceEuro(BigDecimal.valueOf(40000))
            .sizeSquareMeter(60)
            .build();

    public static final HouseEntity HOUSE_ENTITY = HouseEntity.builder()
            .id(HOUSE.id())
            .name(HOUSE.name())
            .country(HOUSE.country())
            .city(HOUSE.city())
            .address(HOUSE.address())
            .buildYear(HOUSE.buildYear())
            .priceEuro(HOUSE.priceEuro())
            .sizeSquareMeter(HOUSE.sizeSquareMeter())
            .build();

    public static final HouseResponse HOUSE_RESPONSE = HouseResponse.builder()
            .id(HOUSE.id())
            .name(HOUSE.name())
            .country(HOUSE.country())
            .city(HOUSE.city())
            .address(HOUSE.address())
            .buildYear(HOUSE.buildYear())
            .priceEuro(HOUSE.priceEuro())
            .sizeSquareMeter(HOUSE.sizeSquareMeter())
            .build();

}
