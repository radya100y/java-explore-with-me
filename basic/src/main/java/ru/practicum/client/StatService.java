package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.model.hit.HitIn;
import ru.practicum.model.hit.HitStat;

@Service
@RequiredArgsConstructor
public class StatService {

    private final WebClient webClient;

    public Mono<HitStat> addHitAndGetStat(HitIn hit) {
        return webClient.post()
                .uri(String.join("", "/hit/add"))
                .bodyValue(hit)
                .retrieve()
                .bodyToMono(HitStat.class);
    }
}
