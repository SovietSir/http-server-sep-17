package com.epam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Event {
    private long id;
    private long leagueId;
    private LocalDateTime date;
    private String homeTeam;
    private String guestTeam;
    private String score;
}
