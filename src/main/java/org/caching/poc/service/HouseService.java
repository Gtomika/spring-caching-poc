package org.caching.poc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.caching.poc.config.AppCachingConfig;
import org.caching.poc.exception.HouseNotFoundException;
import org.caching.poc.model.House;
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

    private final HouseDataBackend dataBackend;

    @Cacheable
    public List<House> getHouses() {
        return dataBackend.getAllHouses();
    }

    @Cacheable
    public House getHouseById(UUID id) {
        return dataBackend.getHouseById(id)
                .orElseThrow(() -> new HouseNotFoundException(id));
    }

    @CachePut(key = "#result.id")
    public House createHouse(House house) {
        House savedHouse = dataBackend.createHouse(house);
        log.info("House created: {}", savedHouse);
        return savedHouse;
    }

    @CacheEvict
    public void deleteHouse(UUID id) {
        if(dataBackend.houseWithIdExists(id)) {
            dataBackend.deleteHouseById(id);
            log.info("House with ID '{}' deleted", id);
        } else {
            throw new HouseNotFoundException(id);
        }
    }

}
