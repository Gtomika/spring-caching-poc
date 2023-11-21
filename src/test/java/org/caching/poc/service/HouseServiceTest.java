package org.caching.poc.service;

import org.caching.poc.exception.HouseNotFoundException;
import org.caching.poc.mapper.HouseMapper;
import org.caching.poc.model.Country;
import org.caching.poc.model.House;
import org.caching.poc.repository.HouseRepository;
import org.caching.poc.repository.entity.HouseEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HouseServiceTest {

    private static final House HOUSE = House.builder()
            .id(UUID.randomUUID())
            .name("TEST HOUSE")
            .country(Country.AUSTRIA)
            .city("Wien")
            .address("Some avenue 5")
            .buildYear(2012)
            .priceEuro(BigDecimal.valueOf(40000))
            .sizeSquareMeter(60)
            .build();

    private static final HouseEntity HOUSE_ENTITY = HouseEntity.builder()
            .id(HOUSE.id())
            .name(HOUSE.name())
            .country(HOUSE.country())
            .city(HOUSE.city())
            .address(HOUSE.address())
            .buildYear(HOUSE.buildYear())
            .priceEuro(HOUSE.priceEuro())
            .sizeSquareMeter(HOUSE.sizeSquareMeter())
            .build();

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private HouseMapper houseMapper;

    @InjectMocks
    private HouseService houseService;

    @Test
    public void getHouses_returnsAllHouses() {
        List<HouseEntity> housesInDb = List.of(HOUSE_ENTITY);
        List<House> expectedHouses = List.of(HOUSE);

        when(houseRepository.findAll()).thenReturn(housesInDb);
        when(houseMapper.entitiesToModels(housesInDb)).thenReturn(expectedHouses);

        List<House> actualHouses = houseService.getHouses();
        assertEquals(expectedHouses, actualHouses);
    }

    @Test
    public void getHouseById_returnsHouse_ifHouseExists() {
        when(houseRepository.findById(HOUSE.id())).thenReturn(Optional.of(HOUSE_ENTITY));
        when(houseMapper.entityToModel(HOUSE_ENTITY)).thenReturn(HOUSE);

        House actualHouse = houseService.getHouseById(HOUSE.id());
        assertEquals(HOUSE, actualHouse);
    }

    @Test
    public void getHouseById_throwsNotFoundException_ifHouseDoesNotExist() {
        when(houseRepository.findById(HOUSE.id())).thenReturn(Optional.empty());

        HouseNotFoundException expectedException = assertThrows(HouseNotFoundException.class,
                () -> houseService.getHouseById(HOUSE.id()));
        assertEquals(HOUSE.id(), expectedException.getHouseId());
    }

    @Test
    public void createHouse_savedNewHouse() {
        when(houseMapper.modelToEntity(HOUSE)).thenReturn(HOUSE_ENTITY);
        when(houseRepository.save(HOUSE_ENTITY)).thenReturn(HOUSE_ENTITY);
        when(houseMapper.entityToModel(HOUSE_ENTITY)).thenReturn(HOUSE);

        House savedHouse = houseService.createHouse(HOUSE);
        assertEquals(HOUSE, savedHouse);
    }

    @Test
    public void deleteHouse_removesHouse_ifHouseExists() {
        when(houseRepository.existsById(HOUSE.id())).thenReturn(true);

        houseService.deleteHouse(HOUSE.id());

        verify(houseRepository).deleteById(HOUSE.id());
    }

    @Test
    public void deleteHouse_throwsNotFoundException_ifHouseDoesNotExist() {
        when(houseRepository.existsById(HOUSE.id())).thenReturn(false);

        HouseNotFoundException expectedException = assertThrows(HouseNotFoundException.class,
                () -> houseService.deleteHouse(HOUSE.id()));
        assertEquals(HOUSE.id(), expectedException.getHouseId());
    }
}