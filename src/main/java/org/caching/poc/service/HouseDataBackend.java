package org.caching.poc.service;

import org.caching.poc.model.House;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This interface defines the contract for storing and retrieving {@link House} data.
 * Exactly one of implementing beans must be active.
 *
 * @see org.caching.poc.adapter.PostgresHouseDataBackend
 */
public interface HouseDataBackend {

    List<House> getAllHouses();

    Optional<House> getHouseById(UUID id);

    boolean houseWithIdExists(UUID id);

    House createHouse(House house);

    void deleteHouseById(UUID id);

}
