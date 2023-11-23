package house.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import house.processor.CreateHouseProcessor;
import house.processor.DeleteHouseByIdProcessor;
import house.processor.EventProcessor;
import house.processor.GetAllHousesProcessor;
import house.processor.GetHouseByIdProcessor;
import house.processor.HouseExistsByIdProcessor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is the model class for the JSON data sent to the API gateway.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseEvent {

    /**
     * Determines the event type, such as get all houses or delete house by id.
     */
    private HouseEventType eventType;

    /**
     * The event data, the format is dependent on the type of event.
     */
    private String rawEventBody;

    public EventProcessor getProcessor(ObjectMapper objectMapper) {
        switch (eventType) {
            case GET_ALL -> {
                return new GetAllHousesProcessor(objectMapper);
            }
            case GET_BY_ID -> {
                return new GetHouseByIdProcessor(objectMapper);
            }
            case CREATE -> {
                return new CreateHouseProcessor(objectMapper);
            }
            case EXISTS_BY_ID -> {
                return new HouseExistsByIdProcessor(objectMapper);
            }
            case DELETE_BY_ID -> {
                return new DeleteHouseByIdProcessor(objectMapper);
            }
            default -> throw new IllegalStateException("Unexpected value: " + eventType);
        }
    }

}
