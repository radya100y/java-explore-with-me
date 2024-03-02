package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.category.CategoryIn;
import ru.practicum.model.category.CategoryOut;
import ru.practicum.model.message.MessageCreateOut;
import ru.practicum.model.message.MessageStatus;
import ru.practicum.model.message.MessageUpdateIn;
import ru.practicum.model.user.UserIn;
import ru.practicum.model.user.UserOut;
import ru.practicum.service.service.CategoryService;
import ru.practicum.service.service.MessageService;
import ru.practicum.service.service.UserService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private final MessageService messageService;


    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserOut save(@Valid @RequestBody UserIn user) {
        return userService.add(user);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserOut delete(@PathVariable("id") long userId) {
        return userService.delete(userId);
    }

    @GetMapping("/users")
    public List<UserOut> gets(@RequestParam(required = false) String ids,
                              @RequestParam(defaultValue = "0") int from,
                              @RequestParam(defaultValue = "10") int size) {

        Pageable reqPage = PageRequest.of(from / size, size, Sort.by("id").ascending());

        List<Long> idUsers = new ArrayList<>();
        if (ids != null) idUsers = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        return userService.gets(idUsers, reqPage);
    }


    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryOut save(@Valid @RequestBody CategoryIn categoryIn) {
        return categoryService.add(categoryIn);
    }

    @PatchMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryOut modify(@PathVariable("id") long categoryId, @Valid @RequestBody CategoryIn categoryIn) {
        return categoryService.update(categoryId, categoryIn);
    }

    @DeleteMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CategoryOut deleteCategory(@PathVariable("id") long categoryId) {
        return categoryService.delete(categoryId);
    }

    @PatchMapping("/events/{eventId}") //админ редактирует событие
    @ResponseStatus(HttpStatus.OK)
    public MessageCreateOut update(@Valid @RequestBody MessageUpdateIn messageUpdateIn,
                                   @PathVariable("eventId") long eventId) {
        return messageService.updateAdmin(messageUpdateIn, eventId);
    }

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<MessageCreateOut> getAdminMessages(
            @RequestParam(required = false) String users,
            @RequestParam(required = false) String states,
            @RequestParam(required = false) String categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {

        log.warn("User -> {} States -> {} Categories -> {} PB -> {} PE -> {} FROM -> {} SIZE -> {}", users,
                states, categories, rangeStart, rangeEnd, from, size);

        List<Long> userList = new ArrayList<>();
        if (users != null) userList.addAll(Arrays.stream(users.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList()));

        List<MessageStatus> statusList = new ArrayList<>();
        if (states != null) statusList.addAll(Arrays.stream(states.split(","))
                .map(MessageStatus::valueOf)
                .collect(Collectors.toList()));

        List<Long> categoryList = new ArrayList<>();
        if (categories != null) categoryList.addAll(Arrays.stream(categories.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList()));

        return messageService.findAdminMessages(userList, statusList, categoryList, rangeStart, rangeEnd, from, size);
    }


}
