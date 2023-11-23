package org.caching.poc.adapter.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HouseIdEventBody {
    String id;

    public static HouseIdEventBody fromId(UUID id) {
        return new HouseIdEventBody(id.toString());
    }
}
