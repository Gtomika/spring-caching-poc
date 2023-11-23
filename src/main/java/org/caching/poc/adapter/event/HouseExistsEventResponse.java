package org.caching.poc.adapter.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseExistsEventResponse {
    private boolean exists;
}
