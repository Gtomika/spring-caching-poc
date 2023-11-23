package house.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import house.event.HouseIdEventBody;
import house.response.HouseEventResponse;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
public class GetHouseByIdProcessor implements EventProcessor<HouseIdEventBody, HouseEventResponse> {

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
    public HouseEventResponse processEvent(HouseIdEventBody eventBody) {
        //normally this would be checked in DynamoDB, but this is a demo
        return HouseEventResponse.builder()
                .id(eventBody.getId())
                .name("House with ID")
                .country("HUNGARY")
                .city("Budapest")
                .address("Some street 15")
                .priceEuro(10000)
                .buildYear(2005)
                .sizeSquareMeter(70)
                .creationTimestamp("2023-11-20T14:36:25.516653Z")
                .build();
    }
}
