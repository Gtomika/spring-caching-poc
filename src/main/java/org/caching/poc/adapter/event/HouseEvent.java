package org.caching.poc.adapter.event;

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

}
