package org.caching.poc.adapter;

import org.caching.poc.mapper.HouseMapper;
import org.caching.poc.model.House;
import org.caching.poc.repository.HouseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.caching.poc.TestConstants.HOUSE;
import static org.caching.poc.TestConstants.HOUSE_ENTITY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostgresHouseDataBackendTest {

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private HouseMapper houseMapper;

    @InjectMocks
    private PostgresHouseDataBackend postgresBackend;

    @Test
    public void getAllHouses_returnsAll() {
        when(houseRepository.findAll()).thenReturn(List.of(HOUSE_ENTITY));

        List<House> expectedHouses = List.of(HOUSE);
        when(houseMapper.entitiesToModels(List.of(HOUSE_ENTITY))).thenReturn(expectedHouses);

        List<House> actualHouses = postgresBackend.getAllHouses();

        assertEquals(expectedHouses, actualHouses);
    }

    @Test
    public void getHouseById_returnsOptional() {
        when(houseRepository.findById(HOUSE.id())).thenReturn(Optional.of(HOUSE_ENTITY));
        when(houseMapper.entityToModel(HOUSE_ENTITY)).thenReturn(HOUSE);

        Optional<House> expectedOptional = Optional.of(HOUSE);
        Optional<House> actualOptional = postgresBackend.getHouseById(HOUSE.id());

        assertEquals(expectedOptional, actualOptional);
    }

    @Test
    public void houseExistsById_returnsUnderlyingResult() {
        when(houseRepository.existsById(HOUSE.id())).thenReturn(true);

        boolean actual = postgresBackend.houseWithIdExists(HOUSE.id());

        assertTrue(actual);
    }

    @Test
    public void createHouse_savesNewHouse_andReturnsSavedHouse() {
        when(houseMapper.modelToEntity(HOUSE)).thenReturn(HOUSE_ENTITY);
        when(houseRepository.save(HOUSE_ENTITY)).thenReturn(HOUSE_ENTITY);
        when(houseMapper.entityToModel(HOUSE_ENTITY)).thenReturn(HOUSE);

        House savedHouse = postgresBackend.createHouse(HOUSE);

        assertEquals(HOUSE, savedHouse);
    }

    @Test
    public void deleteHouseById_deletesHouse() {
        postgresBackend.deleteHouseById(HOUSE.id());

        verify(houseRepository).deleteById(HOUSE.id());
    }

}