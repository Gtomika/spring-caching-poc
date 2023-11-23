package house.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import house.event.EmptyEventBody;
import house.response.HouseEventResponse;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class GetAllHousesProcessor implements EventProcessor<EmptyEventBody, List<HouseEventResponse>> {

    private final ObjectMapper objectMapper;

    @Override
    public EmptyEventBody parseRawEventBody(String rawEventBody) {
        try {
            return objectMapper.readValue(rawEventBody, EmptyEventBody.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse body of GetAllHousesEvent: " + rawEventBody);
        }
    }

    @Override
    public List<HouseEventResponse> processEvent(EmptyEventBody eventBody) {
        //normally this would be checked in DynamoDB, but this is a demo
        HouseEventResponse house1 = HouseEventResponse.builder()
                .id(UUID.randomUUID().toString())
                .name("House in city center")
                .country("ROMANIA")
                .city("Bukarest")
                .address("Some street 15")
                .priceEuro(10000)
                .buildYear(2001)
                .sizeSquareMeter(86)
                .creationTimestamp("2023-11-20T14:36:25.516653Z")
                .build();
        HouseEventResponse house2 = HouseEventResponse.builder()
                .id(UUID.randomUUID().toString())
                .name("Remote farm")
                .country("HUNGARY")
                .city("Remote village")
                .address("Remote street 13")
                .priceEuro(3000)
                .buildYear(1954)
                .sizeSquareMeter(300)
                .creationTimestamp("2023-11-20T14:36:25.516653Z")
                .build();
        return List.of(house1, house2);
    }
}
