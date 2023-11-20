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

/**
 * This service is advised with Spring caching. The {@link AppCachingConfig#HOUSES_CACHE} is
 * set as the default cache to be used.
 */
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

    /**
     * The house with the selected ID will be first looked up in the cache.
     * - If not found in the cache, the method will execute, looking for the house in the database.
     *      The result will be cached using the parameter as key, in this case: house ID.
     * - If found in the cache, the method will not execute and the cached value is returned.
     */
    @Cacheable
    public House getHouseById(UUID id) {
        return houseRepository.findById(id)
                .map(houseMapper::entityToModel)
                .orElseThrow(() -> new HouseNotFoundException(id));
    }

    /**
     * Saves the house in the database and caches it using the ID of the house.
     */
    @CachePut(key = "#result.id")
    public House createHouse(House house) {
        HouseEntity savedHouse = houseRepository.save(houseMapper.modelToEntity(house));
        log.info("House created: {}", savedHouse);
        return houseMapper.entityToModel(savedHouse);
    }

    /**
     * Deletes the house from the database and also from the cache.
     */
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
