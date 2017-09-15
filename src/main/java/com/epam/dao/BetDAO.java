package com.epam.dao;

import com.epam.model.Bet;

import java.sql.SQLException;
import java.util.List;

public interface BetDAO extends DAOCrud<Bet, Long, Void> {
    List<Bet> readBetsByOfferId(long offerId) throws SQLException;

    List<Bet> readBetsByPersonId(long personId) throws SQLException;

    @Override
    default Class<Bet> getModelClass() {
        return Bet.class;
    }
}
