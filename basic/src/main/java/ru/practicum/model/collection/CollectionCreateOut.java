package ru.practicum.model.collection;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.practicum.model.message.MessageShortOut;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
public class CollectionCreateOut {

    private Set<MessageShortOut> events;
    private Long id;
    private Boolean pinned;
    private String title;
}
