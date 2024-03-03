package ru.practicum.model.collection;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "collection", schema = "public")
@Getter
@Setter
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "pinned")
    private Boolean pinned;

    @Column(name = "title", nullable = false, unique = true)
    private String title;
}
