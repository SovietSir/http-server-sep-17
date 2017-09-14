package com.epam.dao;

import com.epam.model.Event;

import java.sql.SQLException;
import java.util.List;

public interface EventDAO extends AbstractDAO<Event> {
    List<Event> readAll() throws SQLException;

    List<Event> readEventsByLeagueId(long leagueId) throws SQLException;

    @Override
    default Class<Event> getModelClass() {
        return Event.class;
    }
}
