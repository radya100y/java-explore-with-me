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

    @NotBlank
    @Size(max = 512)
    private String title;
}
