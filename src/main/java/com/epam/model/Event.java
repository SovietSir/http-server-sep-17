package com.epam.model;

import io.vavr.Tuple2;
import lombok.AllArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
public class Event {
    private long id;
    private Instant date;
    private String homeTeam;
    private String comingTeam;
    private Tuple2<Byte, Byte> score;
}