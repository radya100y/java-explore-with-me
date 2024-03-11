package ru.practicum.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserOut {

    private long id;
    private String name;
    private String email;
    private Long rating;
}
