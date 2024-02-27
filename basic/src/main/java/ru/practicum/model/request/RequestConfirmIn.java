package ru.practicum.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RequestConfirmIn {
    private List<Long> requestIds;
    private RequestStatus status;
}
