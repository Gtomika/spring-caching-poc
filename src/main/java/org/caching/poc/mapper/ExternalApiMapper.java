package org.caching.poc.mapper;

import org.caching.poc.adapter.event.HouseEventResponse;
import org.caching.poc.adapter.event.HouseRequestEventBody;
import org.caching.poc.model.House;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExternalApiMapper {

    House externalApiResponseToModel(HouseEventResponse eventResponse);

    List<House> externalApiResponsesToModels(List<HouseEventResponse> eventResponses);

    HouseRequestEventBody modelToExternalApiRequest(House house);

}
