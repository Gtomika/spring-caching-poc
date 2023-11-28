package org.caching.poc.adapter;

import org.caching.poc.adapter.event.HouseEventType;
import org.caching.poc.model.House;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles(value = {"test", "api-backend"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureWireMock(port = 0) //means random port
class ExternalApiBackendTest {

    private static final String MATCHING_EVENT_TYPE_TEMPLATE = "$[?(@.eventType == '%s')]";

    @Value("${external-api.path}")
    private String externalApiPath;

    @Autowired
    private ExternalApiBackend externalApiBackend;

    @Test
    public void getAllHouses() {
        stubFor(post(urlEqualTo(externalApiPath))
                .withRequestBody(matchingJsonPath(MATCHING_EVENT_TYPE_TEMPLATE.formatted(HouseEventType.GET_ALL.name())))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("getAllHouses.json")
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        List<House> houses = externalApiBackend.getAllHouses();

        assertEquals(2, houses.size());
        assertEquals("Test house #1", houses.get(0).name());
        assertEquals("Test house #2", houses.get(1).name());
    }

    @Test
    public void getHouseById() {
        stubFor(post(urlEqualTo(externalApiPath))
                .withRequestBody(matchingJsonPath(MATCHING_EVENT_TYPE_TEMPLATE.formatted(HouseEventType.GET_BY_ID.name())))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("getHouseById.json")
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        String id = "758c1659-d834-4dbe-983f-8f178f5645d3";
        House house = externalApiBackend.getHouseById(UUID.fromString(id))
                .orElseThrow(() -> new AssertionFailedError("House was not found with ID " + id));

        assertEquals("Test house #1", house.name());
    }

    @Test
    public void houseExistsById() {
        stubFor(post(urlEqualTo(externalApiPath))
                .withRequestBody(matchingJsonPath(MATCHING_EVENT_TYPE_TEMPLATE.formatted(HouseEventType.EXISTS_BY_ID.name())))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("houseExistsById.json")
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        boolean exists = externalApiBackend.houseWithIdExists(UUID.randomUUID());

        assertTrue(exists);
    }

    @Test
    public void deleteHouseById() {
        stubFor(post(urlEqualTo(externalApiPath))
                .withRequestBody(matchingJsonPath(MATCHING_EVENT_TYPE_TEMPLATE.formatted(HouseEventType.DELETE_BY_ID.name())))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("deleteHouseById.json")
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        String id = "758c1659-d834-4dbe-983f-8f178f5645d3";
        externalApiBackend.deleteHouseById(UUID.fromString(id));

        verify(postRequestedFor(urlEqualTo(externalApiPath)));
    }

    @Test
    public void createHouse() {
        stubFor(post(urlEqualTo(externalApiPath))
                .withRequestBody(matchingJsonPath(MATCHING_EVENT_TYPE_TEMPLATE.formatted(HouseEventType.CREATE.name())))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("deleteHouseById.json")
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        UUID id = UUID.fromString("758c1659-d834-4dbe-983f-8f178f5645d3");
        House house = externalApiBackend.createHouse(House.builder()
                .id(id)
                .name("Test house #1")
                .build());

        assertEquals("Test house #1", house.name());
    }

}