package ru.practicum.service.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByEvent_IdAndStatusIn(long messageId, List<RequestStatus> statuses);

    List<Request> findAllByEvent_Id(long messageId);

    List<Request> findAllByRequester_Id(long userId);
}
