package ru.practicum.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RequestConfirmOut {
    private List<RequestOut> confirmedRequests;
    private List<RequestOut> rejectedRequests;

    public RequestConfirmOut() {
        confirmedRequests = new ArrayList<>();
        rejectedRequests = new ArrayList<>();
    }
}
