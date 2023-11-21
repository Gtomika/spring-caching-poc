package org.caching.poc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.caching.poc.config.AppCachingConfig;
import org.caching.poc.exception.HouseNotFoundException;
import org.caching.poc.mapper.HouseMapper;
import org.caching.poc.model.House;
import org.caching.poc.repository.HouseRepository;
import org.caching.poc.repository.entity.HouseEntity;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@CacheConfig(cacheNames = AppCachingConfig.HOUSES_CACHE)
@RequiredArgsConstructor
public class HouseService {

    private final HouseRepository houseRepository;
    private final HouseMapper houseMapper;

    public List<House> getHouses() {
        return houseMapper.entitiesToModels(houseRepository.findAll());
    }

    @Cacheable
    public House getHouseById(UUID id) {
        return houseRepository.findById(id)
                .map(houseMapper::entityToModel)
                .orElseThrow(() -> new HouseNotFoundException(id));
    }

    @CachePut(key = "#result.id")
    public House createHouse(House house) {
        HouseEntity savedHouse = houseRepository.save(houseMapper.modelToEntity(house));
        log.info("House created: {}", savedHouse);
        return houseMapper.entityToModel(savedHouse);
    }

    @CacheEvict
    public void deleteHouse(UUID id) {
        if(houseRepository.existsById(id)) {
            houseRepository.deleteById(id);
            log.info("House with ID '{}' deleted", id);
        } else {
            throw new HouseNotFoundException(id);
        }
    }

}
