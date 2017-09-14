package com.epam.dao;

import com.epam.model.Bet;

import java.util.List;

public interface BetDAO extends DAOCrud<Bet, Long> {
    List<Bet> readBetsByOfferId(long offerId);

    List<Bet> readBetsByPersonId(long personId);
}
