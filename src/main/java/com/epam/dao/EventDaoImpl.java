package com.epam.dao;

import com.epam.daoInterfaces.EventDAO;
import com.epam.model.Event;

import java.util.List;

public class EventDaoImpl implements EventDAO {
    @Override
    public List<Event> readAll() {
        return null;
    }

    @Override
    public List<Event> readEventsByLeagueId(long leagueId) {
        return null;
    }

    @Override
    public void create(Event event) {

    }

    @Override
    public Event readById(Long aLong) {
        return null;
    }

    @Override
    public void update(Long aLong, Event event) {

    }

    @Override
    public void deleteById(Long aLong) {

    }
}
