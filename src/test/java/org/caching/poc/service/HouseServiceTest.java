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

import static org.caching.poc.TestConstants.HOUSE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HouseServiceTest {

    @Mock
    private HouseDataBackend dataBackend;

    @InjectMocks
    private HouseService houseService;

    @Test
    public void getHouses_returnsAllHouses() {
        List<House> expectedHouses = List.of(HOUSE);

        when(dataBackend.getAllHouses()).thenReturn(expectedHouses);

        List<House> actualHouses = houseService.getHouses();
        assertEquals(expectedHouses, actualHouses);
    }

    @Test
    public void getHouseById_returnsHouse_ifHouseExists() {
        when(dataBackend.getHouseById(HOUSE.id())).thenReturn(Optional.of(HOUSE));

        House actualHouse = houseService.getHouseById(HOUSE.id());
        assertEquals(HOUSE, actualHouse);
    }

    @Test
    public void getHouseById_throwsNotFoundException_ifHouseDoesNotExist() {
        when(dataBackend.getHouseById(HOUSE.id())).thenReturn(Optional.empty());

        HouseNotFoundException expectedException = assertThrows(HouseNotFoundException.class,
                () -> houseService.getHouseById(HOUSE.id()));
        assertEquals(HOUSE.id(), expectedException.getHouseId());
    }

    @Test
    public void createHouse_savedNewHouse() {
        when(dataBackend.createHouse(HOUSE)).thenReturn(HOUSE);

        House savedHouse = houseService.createHouse(HOUSE);
        assertEquals(HOUSE, savedHouse);
    }

    @Test
    public void deleteHouse_removesHouse_ifHouseExists() {
        when(dataBackend.houseWithIdExists(HOUSE.id())).thenReturn(true);

        houseService.deleteHouse(HOUSE.id());

        verify(dataBackend).deleteHouseById(HOUSE.id());
    }

    @Test
    public void deleteHouse_throwsNotFoundException_ifHouseDoesNotExist() {
        when(dataBackend.houseWithIdExists(HOUSE.id())).thenReturn(false);

        HouseNotFoundException expectedException = assertThrows(HouseNotFoundException.class,
                () -> houseService.deleteHouse(HOUSE.id()));
        assertEquals(HOUSE.id(), expectedException.getHouseId());
    }
}