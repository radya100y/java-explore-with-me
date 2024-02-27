package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.category.CategoryIn;
import ru.practicum.model.category.CategoryOut;
import ru.practicum.model.message.MessageCreateOut;
import ru.practicum.model.message.MessageUpdateIn;
import ru.practicum.model.user.UserIn;
import ru.practicum.model.user.UserOut;
import ru.practicum.service.service.CategoryService;
import ru.practicum.service.service.MessageService;
import ru.practicum.service.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
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
}
