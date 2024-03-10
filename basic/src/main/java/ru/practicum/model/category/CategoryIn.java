package ru.practicum.model.category;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class CategoryIn {

    @Size(min = 1, max = 50, message = "name минимальное кол-во 1 , максимальное 50")
    @NotBlank(message = "поле name должно быть заполнено")
    private String name;
}
