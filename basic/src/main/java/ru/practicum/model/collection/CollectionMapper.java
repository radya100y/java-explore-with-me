package ru.practicum.model.collection;

import lombok.experimental.UtilityClass;
import ru.practicum.model.message.MessageMapper;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CollectionMapper {

    public static Collection toCollection(CollectionCreateIn collectionCreateIn) {
        return new Collection (
                collectionCreateIn.getPinned(),
                collectionCreateIn.getTitle()
        );
    }

    public static CollectionCreateOut toCollectionCreateOut(Collection collection) {
        return CollectionCreateOut.builder()
                .events(collection.getEvents().stream()
                        .map(x -> MessageMapper.toMessageShortOut(x.getMessage()))
                        .collect(Collectors.toSet()))
                .id(collection.getId())
                .pinned(collection.getPinned())
                .title(collection.getTitle())
                .build();
    }

}
