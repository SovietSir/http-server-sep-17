package com.epam.daoInterfaces;

import com.epam.model.League;

import java.util.List;

public interface LeagueDAO extends DAOCrud<League, Long> {
    List<League> readAll();
}