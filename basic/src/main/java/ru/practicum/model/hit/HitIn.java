package ru.practicum.model.hit;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class HitIn {

    @Size(max = 255, message = "APP - превышено максимальное количество символов")
    @NotBlank(message = "поле APP должно быть заполнено")
    private String app;

    @Size(max = 255, message = "URI - превышено максимальное количество символов")
    @NotBlank(message = "поле URI должно быть заполнено")
    private String uri;

    @Size(max = 255, message = "IP - превышено максимальное количество символов")
    @NotBlank(message = "поле IP должно быть заполнено")
    private String ip;
}
