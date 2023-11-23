package house.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import house.event.HouseIdEventBody;
import house.response.HouseExistsEventResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HouseExistsByIdProcessor implements EventProcessor<HouseIdEventBody, HouseExistsEventResponse> {

    private final ObjectMapper objectMapper;

    @Override
    public HouseIdEventBody parseRawEventBody(String rawEventBody) {
        try {
            return objectMapper.readValue(rawEventBody, HouseIdEventBody.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse body of HouseExistsByIdEvent: " + rawEventBody);
        }
    }

    @Override
    public HouseExistsEventResponse processEvent(HouseIdEventBody eventBody) {
        //normally this would be checked in DynamoDB, but this is a demo
        return new HouseExistsEventResponse(true);
    }
}
