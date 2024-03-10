package ru.practicum.model.hit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class HitStat {

    private String app;

    private String uri;

    private Long hits;
}
