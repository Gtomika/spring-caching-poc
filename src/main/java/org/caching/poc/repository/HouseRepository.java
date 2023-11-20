package org.caching.poc.repository;

import org.caching.poc.repository.entity.HouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HouseRepository extends JpaRepository<HouseEntity, UUID> {
}
