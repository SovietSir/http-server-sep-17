package com.epam.daoInterfaces;

import com.epam.model.Event;

import java.util.List;

public interface EventDAO extends DAOCrud<Event, Long> {
    List<Event> readAll();

    List<Event> readEventsByLeagueId(long leagueId);
}
