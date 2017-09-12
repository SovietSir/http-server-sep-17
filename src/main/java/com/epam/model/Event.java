package com.epam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class Event {
    private long id;
    private long leagueId;
    private Date date;
    private String homeTeam;
    private String guestTeam;
    private String score;
}
