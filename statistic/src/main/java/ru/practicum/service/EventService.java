package ru.practicum.service;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.*;
import ru.practicum.model.QEvent;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    @Autowired
    private final EventRepository eventRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public EventOut save(EventIn eventIn) {
        return EventMapper.toEventOut(eventRepository.save(EventMapper.toEvent(eventIn)));
    }

    public EventOut getById(long id) {
        return EventMapper.toEventOut(eventRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Событие с идентификатором " + id + " не найдено")));
    }

    public List<EventsOut> getEvents(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        QEvent event = QEvent.event;

        StringExpression appExpression = event.app;
        StringExpression uriExpression = event.uri;
        NumberExpression<Long> hitsExpression = unique ? event.ip.countDistinct() : event.id.count();
        BooleanExpression whereExpression = event.dt.after(start)
                .and(event.dt.before(end))
                .and(uris.size() == 0 ? Expressions.asBoolean(true).isTrue() : event.uri.in(uris));

        List<Tuple> tuples = new JPAQueryFactory(entityManager)
                .select(appExpression, uriExpression, hitsExpression)
                .from(event)
                .where(whereExpression)
                .groupBy(appExpression, uriExpression)
                .orderBy(hitsExpression.desc())
                .fetch();

        return tuples.stream()
                .map(x -> new EventsOut(x.toArray()[0].toString(),
                        x.toArray()[1].toString(),
                        Long.parseLong(x.toArray()[2].toString())))
                .collect(Collectors.toList());
    }
}
