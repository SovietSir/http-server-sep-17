package com.epam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    public static Event getFromResultSet(ResultSet resultSet) throws SQLException {
        return new Event(resultSet.getLong("id"),
                resultSet.getLong("league_id"),
                resultSet.getTimestamp("date").toLocalDateTime(),
                resultSet.getString("home_team"),
                resultSet.getString("guest_team"),
                resultSet.getString("score"));
    }
}
