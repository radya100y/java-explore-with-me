package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.error.BadRequestException;
import ru.practicum.model.category.CategoryOut;
import ru.practicum.model.collection.CollectionCreateOut;
import ru.practicum.model.message.MessageCreateOut;
import ru.practicum.model.message.MessageShortOut;
import ru.practicum.service.service.CategoryService;
import ru.practicum.service.service.CollectionService;
import ru.practicum.service.service.MessageService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PublicController {

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private final MessageService messageService;

    @Autowired
    private final CollectionService collectionService;

    @GetMapping("/categories")
    public List<CategoryOut> gets(@RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {

        Pageable reqPage = PageRequest.of(from / size, size, Sort.by("id").ascending());
        return categoryService.gets(reqPage);
    }

    @GetMapping("categories/{id}")
    public CategoryOut get(@PathVariable("id") long catId) {
        return categoryService.getCategory(catId);
    }

    @GetMapping("events/{eventId}")
    public MessageCreateOut getMessage(@PathVariable("eventId") long messageId) {
        return messageService.getMessageForPublic(messageId);
    }

    @GetMapping("/events")
    public List<MessageShortOut> getMessages(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {

        List<Long> categoryList = new ArrayList<>();
        if (categories != null) categoryList.addAll(Arrays.stream(categories.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList()));

        if ((rangeStart != null) && (rangeEnd != null)) if (rangeStart.isAfter(rangeEnd))
            throw new BadRequestException("Неапваильно указаны даты начала и окончания события");

        return messageService.findPublicMessages(text, categoryList, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
    }

    @GetMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CollectionCreateOut getCollection(@PathVariable("compId") long collectionId) {
        return collectionService.getCollectionOut(collectionId);
    }
}
