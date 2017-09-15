package com.epam.dao;

import com.epam.model.Event;
import com.epam.model.League;

public interface LeagueDAO extends DAOCrud<League, Long, Event> {
    @Override
    default Class<League> getModelClass() {
        return League.class;
    }
}
