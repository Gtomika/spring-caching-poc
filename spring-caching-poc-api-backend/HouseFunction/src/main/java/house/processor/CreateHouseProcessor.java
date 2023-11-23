package house.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import house.event.HouseRequestEventBody;
import house.response.HouseEventResponse;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class CreateHouseProcessor implements EventProcessor<HouseRequestEventBody, HouseEventResponse> {

    private final ObjectMapper objectMapper;

    @Override
    public HouseRequestEventBody parseRawEventBody(String rawEventBody) {
        try {
            return objectMapper.readValue(rawEventBody, HouseRequestEventBody.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse body of CreateHouseEvent: " + rawEventBody);
        }
    }

    @Override
    public HouseEventResponse processEvent(HouseRequestEventBody eventBody) {
        //normally this would also be saved to dynamoDB, but this is a demo
        return HouseEventResponse.builder()
                .id(UUID.randomUUID().toString())
                .name(eventBody.getName())
                .country(eventBody.getCountry())
                .city(eventBody.getCity())
                .address(eventBody.getAddress())
                .priceEuro(eventBody.getPriceEuro())
                .buildYear(eventBody.getBuildYear())
                .sizeSquareMeter(eventBody.getSizeSquareMeter())
                .creationTimestamp("2023-11-20T14:36:25.516653Z")
                .build();
    }
}
