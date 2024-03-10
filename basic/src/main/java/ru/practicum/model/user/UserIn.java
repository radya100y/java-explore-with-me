package ru.practicum.model.user;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class UserIn {

    @Size(min = 2, max = 250, message = "name минимальное кол-во 2 , максимальное 256")
    @NotBlank(message = "поле name должно быть заполнено")
    private String name;

    @Size(min = 6, max = 254, message = "email - превышено максимальное количество символов")
    @NotBlank(message = "поле email должно быть заполнено")
    @Email
    private String email;
}
