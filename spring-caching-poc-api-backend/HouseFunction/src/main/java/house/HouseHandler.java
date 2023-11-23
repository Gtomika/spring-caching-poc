package house;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import house.event.HouseEvent;

import java.util.HashMap;
import java.util.Map;

public class HouseHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent request, final Context context) {
        var headers = prepareHeaders();
        try {
            HouseEvent houseEvent = objectMapper.readValue(request.getBody(), HouseEvent.class);
            var eventProcessor = houseEvent.getProcessor(objectMapper);

            Object parsedEventBody = eventProcessor.parseRawEventBody(houseEvent.getRawEventBody());
            Object eventResponse = eventProcessor.processEvent(parsedEventBody);

            String serializedEventResponse = objectMapper.writeValueAsString(eventResponse);

            return new APIGatewayProxyResponseEvent()
                    .withHeaders(headers)
                    .withStatusCode(200)
                    .withBody(serializedEventResponse);
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent()
                    .withHeaders(headers)
                    .withStatusCode(500)
                    .withBody("{\"error\": \"Failed to process request\"}");
        }
    }

    private Map<String, String> prepareHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }

}
