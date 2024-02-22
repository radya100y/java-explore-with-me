package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.EventIn;
import ru.practicum.model.EventOut;
import ru.practicum.model.EventsOut;
import ru.practicum.service.EventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
public class StatController {

    @Autowired
    private final EventService eventService;

    @PostMapping(path = "/hit")
    public EventOut save(@Valid @RequestBody EventIn eventIn) {
        return eventService.save(eventIn);
    }

    @GetMapping(path = "/stats")
    public List<EventsOut> getEvents(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) String uris,
            @RequestParam(defaultValue = "false") Boolean unique) {

        List<String> urisList = new ArrayList<>();
        if (uris != null) {
            urisList.addAll(Arrays.asList(uris.split(",")));
        }

        return eventService.getEvents(start, end, urisList, unique);
    }
}
