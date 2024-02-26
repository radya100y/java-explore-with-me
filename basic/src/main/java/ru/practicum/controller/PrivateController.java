package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.message.MessageCreateIn;
import ru.practicum.model.message.MessageCreateOut;
import ru.practicum.service.service.MessageService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class PrivateController {

    @Autowired
    private final MessageService messageService;

    @PostMapping("/{id}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageCreateOut save(@Valid @RequestBody MessageCreateIn messageCreateIn,
                                 @PathVariable("id") long userId) {

        return messageService.add(messageCreateIn, userId);
    }
}
