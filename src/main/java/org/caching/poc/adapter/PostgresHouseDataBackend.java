package org.caching.poc.adapter;

import lombok.RequiredArgsConstructor;
import org.caching.poc.mapper.HouseMapper;
import org.caching.poc.model.House;
import org.caching.poc.repository.HouseRepository;
import org.caching.poc.repository.entity.HouseEntity;
import org.caching.poc.service.HouseDataBackend;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * A {@link HouseDataBackend} implementation that uses the underlying Postgres database.
 */
@Profile("postgres-backend")
@Component
@RequiredArgsConstructor
public class PostgresHouseDataBackend implements HouseDataBackend {

    private final HouseRepository houseRepository;
    private final HouseMapper houseMapper;

    @Override
    public List<House> getAllHouses() {
        return houseMapper.entitiesToModels(houseRepository.findAll());
    }

    @Override
    public Optional<House> getHouseById(UUID id) {
        return houseRepository.findById(id)
                .map(houseMapper::entityToModel);
    }

    @Override
    public boolean houseWithIdExists(UUID id) {
        return houseRepository.existsById(id);
    }

    @Override
    public House createHouse(House house) {
        HouseEntity houseToSave = houseMapper.modelToEntity(house);
        return houseMapper.entityToModel(houseRepository.save(houseToSave));
    }

    @Override
    public void deleteHouseById(UUID id) {
        houseRepository.deleteById(id);
    }
}
