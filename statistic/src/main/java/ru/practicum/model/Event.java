package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events", schema = "public")
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "app", nullable = false, length = 255)
    private String app;

    @Column(name = "uri", nullable = false, length = 255)
    private String uri;

    @Column(name = "ip", nullable = false, length = 255)
    private String ip;

    @Column(name = "dt", nullable = false)
    private LocalDateTime dt;

    public Event(String app, String uri, String ip) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.dt = LocalDateTime.now();
    }

}
