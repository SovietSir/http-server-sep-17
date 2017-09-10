package com.epam.model;

import io.vavr.Tuple2;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class Event {
    private long id;
    private Instant date;
    private String homeTeam;
    private String comingTeam;
    private Tuple2<Byte, Byte> score;
}