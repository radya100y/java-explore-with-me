package ru.practicum.model.like;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "likes", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@IdClass(LikeId.class)
public class Like {

    @Id
    @Column(name = "message_id")
    private Long messageId;

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "rate")
    private Long rate;

    @Column(name = "created_on")
    private LocalDateTime createdOn;
}
