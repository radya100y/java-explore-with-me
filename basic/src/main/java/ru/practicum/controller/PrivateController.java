package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.message.MessageCreateIn;
import ru.practicum.model.message.MessageCreateOut;
import ru.practicum.model.message.MessageShortOut;
import ru.practicum.model.message.MessageUpdateIn;
import ru.practicum.model.request.RequestConfirmIn;
import ru.practicum.model.request.RequestConfirmOut;
import ru.practicum.model.request.RequestOut;
import ru.practicum.service.service.MessageService;
import ru.practicum.service.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class PrivateController {

    @Autowired
    private final MessageService messageService;

    @Autowired
    private final RequestService requestService;

    @PostMapping("/{id}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageCreateOut saveEvent(@Valid @RequestBody MessageCreateIn messageCreateIn,
                                 @PathVariable("id") long userId) {
        return messageService.add(messageCreateIn, userId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public MessageCreateOut updateEvent(@Valid @RequestBody MessageUpdateIn messageUpdateIn,
                                   @PathVariable("userId") long userId,
                                   @PathVariable("eventId") long eventId) {
        return messageService.update(messageUpdateIn, userId, eventId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestOut saveRequest(@PathVariable("userId") long userId,
                                  @RequestParam long eventId) {
        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public RequestOut cancelRequest(@PathVariable("userId") long userId,
                             @PathVariable("requestId") long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public RequestConfirmOut approveRequests(@PathVariable("userId") long userId,
                                             @PathVariable("eventId") long eventId,
                                             @Valid @RequestBody RequestConfirmIn requestConfirmIn) {
        return requestService.updateRequests(userId, eventId, requestConfirmIn);
    }

    @GetMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<MessageShortOut> getMessagesForOwner(@PathVariable("userId") long userId,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        Pageable reqPage = PageRequest.of(from / size, size);
        return messageService.getForInitiator(userId, reqPage);
    }

    @GetMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestOut> getUserRequests(@PathVariable("userId") long userId) {
        return requestService.getRequestsForRequester(userId);
    }

    @GetMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public MessageCreateOut getMessage(@PathVariable("userId") long userId,
                                       @PathVariable("eventId") long eventId) {
        return messageService.getMessage(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestOut> getRequestsForEvent(@PathVariable("userId") long userId,
                                          @PathVariable("eventId") long eventId) {
        return messageService.getRequestsForMessage(userId, eventId);
    }

}
