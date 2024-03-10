package ru.practicum.model.collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "collection", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "pinned")
    private Boolean pinned;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @OneToMany
    @JoinColumn(name = "collection_id")
    private Set<CollectionMessage> events = new HashSet<>();

    public Collection(Boolean pinned, String title) {
        this.pinned = pinned;
        this.title = title;
    }
}
