package house.processor;

public interface EventProcessor<EventBodyType, EventResponseType> {

    EventBodyType parseRawEventBody(String rawEventBody);

    EventResponseType processEvent(EventBodyType eventBody);

}
