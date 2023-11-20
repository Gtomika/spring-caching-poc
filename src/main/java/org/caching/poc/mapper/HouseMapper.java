package org.caching.poc.mapper;

import org.caching.poc.controller.dto.HouseRequest;
import org.caching.poc.controller.dto.HouseResponse;
import org.caching.poc.model.House;
import org.caching.poc.repository.entity.HouseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HouseMapper {

    HouseEntity modelToEntity(House house);

    House entityToModel(HouseEntity houseEntity);

    List<House> entitiesToModels(List<HouseEntity> houseEntities);

    House requestToModel(HouseRequest houseRequest);

    HouseResponse modelToResponse(House house);

    List<HouseResponse> modelsToResponses(List<House> houses);

    default BigDecimal mapPrice(int price) {
        return BigDecimal.valueOf(price);
    }

    default double mapPrice(BigDecimal price) {
        return price.doubleValue();
    }
}
