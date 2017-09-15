package com.epam.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Event {
    private long id;
    private long leagueId;
    private LocalDateTime date;
    private String homeTeam;
    private String guestTeam;
    private String score;

    public static Event getFromResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return extract(resultSet);
        } else {
            throw new NoSuchElementException();
        }
    }

    public static List<Event> getAllFromResultSet(ResultSet resultSet) throws SQLException {
        List<Event> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(extract(resultSet));
        }
        return list;
    }

    private static Event extract(ResultSet resultSet) throws SQLException {
        return new Event(resultSet.getLong("id"),
                resultSet.getLong("league_id"),
                resultSet.getTimestamp("date").toLocalDateTime(),
                resultSet.getString("home_team"),
                resultSet.getString("guest_team"),
                resultSet.getString("score"));
    }
}
