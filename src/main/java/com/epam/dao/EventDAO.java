package com.epam.dao;

import com.epam.model.Event;
import com.epam.model.Offer;

import java.sql.SQLException;
import java.util.List;

public interface EventDAO extends DAOCrud<Event, Long, Offer> {
    List<Event> readEventsByLeagueId(long leagueId) throws SQLException;

    @Override
    default Class<Event> getModelClass() {
        return Event.class;
    }
}
