package ru.practicum.model;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EventMapper {

    public static Event toEvent(EventIn eventIn) {
        return new Event(
                eventIn.getApp(),
                eventIn.getUri(),
                eventIn.getIp()
        );
    }

    public static EventOut toEventOut(Event event) {
        return new EventOut(
                event.getId(),
                event.getApp(),
                event.getUri(),
                event.getIp(),
                event.getDt()
        );
    }
}
