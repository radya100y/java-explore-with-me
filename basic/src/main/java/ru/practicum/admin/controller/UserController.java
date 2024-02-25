package ru.practicum.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.model.UserIn;
import ru.practicum.admin.model.UserOut;
import ru.practicum.admin.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

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
}
