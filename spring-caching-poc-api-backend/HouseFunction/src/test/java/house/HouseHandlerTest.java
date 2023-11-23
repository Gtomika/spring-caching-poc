package house;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import house.event.EmptyEventBody;
import house.event.HouseEvent;
import house.event.HouseEventType;
import house.event.HouseIdEventBody;
import house.event.HouseRequestEventBody;
import house.response.HouseEventResponse;
import house.response.HouseExistsEventResponse;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HouseHandlerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HouseHandler handler = new HouseHandler();

    @Test
    public void getAllHousesEvent() throws Exception {
        EmptyEventBody eventBody = EmptyEventBody.create();
        HouseEvent event = createEvent(HouseEventType.GET_ALL, eventBody);
        APIGatewayProxyRequestEvent request = createRequest(event);

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, null);
        List<HouseEventResponse> houses = objectMapper.readValue(response.getBody(), new TypeReference<>() {});

        assertEquals(200, response.getStatusCode());
        assertFalse(houses.isEmpty());
    }

    @Test
    public void getHouseByIdEvent() throws Exception  {
        String id = UUID.randomUUID().toString();
        HouseIdEventBody eventBody = new HouseIdEventBody(id);
        HouseEvent event = createEvent(HouseEventType.GET_BY_ID, eventBody);
        APIGatewayProxyRequestEvent request = createRequest(event);

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, null);
        HouseEventResponse house = objectMapper.readValue(response.getBody(), HouseEventResponse.class);

        assertEquals(200, response.getStatusCode());
        assertEquals(id, house.id());
    }

    @Test
    public void createHouseEvent() throws Exception  {
        HouseRequestEventBody eventBody = createHouseRequestEventBody();
        HouseEvent event = createEvent(HouseEventType.CREATE, eventBody);
        APIGatewayProxyRequestEvent request = createRequest(event);

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, null);
        HouseEventResponse house = objectMapper.readValue(response.getBody(), HouseEventResponse.class);

        assertEquals(200, response.getStatusCode());
        assertEquals(eventBody.getName(), house.name());
    }

    @Test
    public void houseExistsByIdEvent() throws Exception  {
        String id = UUID.randomUUID().toString();
        HouseIdEventBody eventBody = new HouseIdEventBody(id);
        HouseEvent event = createEvent(HouseEventType.EXISTS_BY_ID, eventBody);
        APIGatewayProxyRequestEvent request = createRequest(event);

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, null);
        HouseExistsEventResponse existsData = objectMapper.readValue(response.getBody(), HouseExistsEventResponse.class);

        assertEquals(200, response.getStatusCode());
        assertTrue(existsData.isExists());
    }

    @Test
    public void deleteHouseByIdEvent() throws Exception  {
        String id = UUID.randomUUID().toString();
        HouseIdEventBody eventBody = new HouseIdEventBody(id);
        HouseEvent event = createEvent(HouseEventType.DELETE_BY_ID, eventBody);
        APIGatewayProxyRequestEvent request = createRequest(event);

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, null);
        HouseEventResponse house = objectMapper.readValue(response.getBody(), HouseEventResponse.class);

        assertEquals(200, response.getStatusCode());
        assertEquals(id, house.id());
    }

    private APIGatewayProxyRequestEvent createRequest(HouseEvent event) throws Exception {
        String serializedBody = objectMapper.writeValueAsString(event);
        return new APIGatewayProxyRequestEvent()
                .withBody(serializedBody)
                .withPath("/external-api/v1/house-events")
                .withHttpMethod("post")
                .withHeaders(Map.of("Accept", "application/json"));
    }

    private <T> HouseEvent createEvent(HouseEventType eventType, T eventBody) throws Exception {
        String serializedBody = objectMapper.writeValueAsString(eventBody);
        return new HouseEvent(eventType, serializedBody);
    }

    private HouseRequestEventBody createHouseRequestEventBody() {
        return HouseRequestEventBody.builder()
                .name("Test house")
                .country("HUNGARY")
                .city("Pécs")
                .address("Test road 1")
                .buildYear(1995)
                .sizeSquareMeter(200)
                .priceEuro(14000)
                .build();
    }

}