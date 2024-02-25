package ru.practicum.admin.model;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class UserIn {

    @Size(max = 256, message = "name - превышено максимальное количество символов")
    @NotBlank(message = "поле name должно быть заполнено")
    private String name;

    @Size(max = 512, message = "email - превышено максимальное количество символов")
    @NotBlank(message = "поле email должно быть заполнено")
    private String email;
}
