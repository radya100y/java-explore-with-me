package ru.practicum.model.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.practicum.model.category.Category;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.RequestStatus;
import ru.practicum.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "message", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "annotation")
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;

    @Column(name = "description")
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "lat")
    private Float lat;

    @Column(name = "lon")
    private Float lon;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Column(name = "title")
    private String title;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @ManyToOne
    @JoinColumn(name = "initiator")
    private User initiator;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageStatus state;

    @OneToMany//(mappedBy = "event")
    @JoinColumn(name = "message_id")
    private Set<Request> requests = new HashSet<>();

    @Column(name = "views")
    private Long views;

    public long getConfirmedRequestQty() {
        return requests.stream().filter(x -> x.getStatus().equals(RequestStatus.CONFIRMED)).count();
    }

}
