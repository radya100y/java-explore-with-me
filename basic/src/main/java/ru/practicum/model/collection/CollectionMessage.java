package ru.practicum.model.collection;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.model.message.Message;

import javax.persistence.*;

@Entity
@Table(name = "collection_message", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class CollectionMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private Collection collection;


    public CollectionMessage(Message message, Collection collection) {
        this.message = message;
        this.collection = collection;
    }
}
