package org.caching.poc.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.caching.poc.adapter.event.EmptyEventBody;
import org.caching.poc.adapter.event.HouseEvent;
import org.caching.poc.adapter.event.HouseEventResponse;
import org.caching.poc.adapter.event.HouseEventType;
import org.caching.poc.adapter.event.HouseExistsEventResponse;
import org.caching.poc.adapter.event.HouseIdEventBody;
import org.caching.poc.config.ExternalApiConfig;
import org.caching.poc.mapper.ExternalApiMapper;
import org.caching.poc.model.House;
import org.caching.poc.service.HouseDataBackend;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * A {@link HouseDataBackend} implementation that uses an AWS serverless stack
 * to store and get house data.
 */
@Slf4j
@Profile("api-backend")
@Component
@RequiredArgsConstructor
public class ExternalApiBackend implements HouseDataBackend {

    private static final String ERROR_TEMPLATE = "Failed to make '%s' request to external API!";

    private final ExternalApiConfig externalApiConfig;
    private final WebClient externalApiClient;
    private final ObjectMapper objectMapper;
    private final ExternalApiMapper externalApiMapper;

    @Override
    public List<House> getAllHouses() {
        try {
            HouseEvent event = createEvent(HouseEventType.GET_ALL, EmptyEventBody.create());

            ParameterizedTypeReference<List<HouseEventResponse>> typeRef = new ParameterizedTypeReference<>() {};
            List<HouseEventResponse> response = makeRequestToExternalApi(event, typeRef);

            return externalApiMapper.externalApiResponsesToModels(response);
        } catch (Exception e) {
            log.error(ERROR_TEMPLATE.formatted("GET ALL HOUSES"), e);
            return List.of();
        }
    }

    @Override
    public Optional<House> getHouseById(UUID id) {
        try {
            HouseEvent event = createEvent(HouseEventType.GET_BY_ID, HouseIdEventBody.fromId(id));

            HouseEventResponse response = makeRequestToExternalApi(event, HouseEventResponse.class);

            return Optional.of(externalApiMapper.externalApiResponseToModel(response));
        } catch (Exception e) {
            log.error(ERROR_TEMPLATE.formatted("GET HOUSE BY ID"), e);
            return Optional.empty();
        }
    }

    @Override
    public boolean houseWithIdExists(UUID id) {
        try {
            HouseEvent event = createEvent(HouseEventType.EXISTS_BY_ID, HouseIdEventBody.fromId(id));

            HouseExistsEventResponse response = makeRequestToExternalApi(event, HouseExistsEventResponse.class);

            return response.isExists();
        } catch (Exception e) {
            log.error(ERROR_TEMPLATE.formatted("HOUSE EXISTS BY ID"), e);
            return false;
        }
    }

    @Override
    public House createHouse(House house) {
        try {
            HouseEvent event = createEvent(HouseEventType.CREATE, externalApiMapper.modelToExternalApiRequest(house));

            HouseEventResponse response = makeRequestToExternalApi(event, HouseEventResponse.class);

            return externalApiMapper.externalApiResponseToModel(response);
        } catch (Exception e) {
            log.error(ERROR_TEMPLATE.formatted("CREATE HOUSE"), e);
            return House.builder().build();
        }

    }

    @Override
    public void deleteHouseById(UUID id) {
        try {
            HouseEvent event = createEvent(HouseEventType.DELETE_BY_ID, HouseIdEventBody.fromId(id));

            makeRequestToExternalApi(event, HouseEventResponse.class);
        } catch (Exception e) {
            log.error(ERROR_TEMPLATE.formatted("DELETE HOUSE BY ID"), e);
        }
    }

    private <T> HouseEvent createEvent(HouseEventType eventType, T eventBody) throws Exception {
        String serializedBody = objectMapper.writeValueAsString(eventBody);
        return new HouseEvent(eventType, serializedBody);
    }

    private <T> T makeRequestToExternalApi(HouseEvent event, ParameterizedTypeReference<T> responseType) {
        return commonRequest(event)
                .bodyToMono(responseType)
                .block();
    }

    private <T> T makeRequestToExternalApi(HouseEvent event, Class<T> responseClass) {
        return commonRequest(event)
                .bodyToMono(responseClass)
                .block();
    }

    private WebClient.ResponseSpec commonRequest(HouseEvent event) {
        return externalApiClient.post()
                .uri(externalApiConfig.getExternalApiPath())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(event)
                .retrieve();
    }
}
