package ru.practicum.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserOutWithoutEmail {
    private long id;
    private String name;
}
