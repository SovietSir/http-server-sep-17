package com.epam.model;

import io.vavr.Tuple2;
import lombok.Value;

import java.time.Instant;

@Value
public class Event {
    private Instant date;
    private String homeTeam;
    private String comingTeam;
    private Tuple2<Byte, Byte> score;
}