package ru.practicum.model.collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
public class CollectionCreateIn {

    private List<Long> events;

    private Boolean pinned;

    @Size(max = 50)
    @NotBlank
    private String title;
}
