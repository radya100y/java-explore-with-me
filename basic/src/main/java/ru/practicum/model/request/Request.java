package ru.practicum.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.practicum.model.message.Message;
import ru.practicum.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "request", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message event;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User requester;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}