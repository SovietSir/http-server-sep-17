package com.epam.dao;

import com.epam.model.League;

import java.sql.SQLException;
import java.util.List;

public interface LeagueDAO extends AbstractDAO<League> {
    List<League> readAll() throws SQLException;

    @Override
    default Class<League> getModelClass() {
        return League.class;
    }
}
