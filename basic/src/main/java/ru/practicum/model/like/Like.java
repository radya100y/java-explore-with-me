package ru.practicum.model.like;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.practicum.model.message.Message;
import ru.practicum.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "likes", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Like {

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "rate")
    private Integer rate;

    @Column(name = "created_on")
    private LocalDateTime createdOn;
}
