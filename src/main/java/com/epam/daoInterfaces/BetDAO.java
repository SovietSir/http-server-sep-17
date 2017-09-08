package com.epam.daoInterfaces;

import com.epam.model.Bet;

import java.util.List;

public interface BetDAO extends DAOCrud<Bet, Long> {
    List<Bet> getBetsByOfferId(long offerId);

    List<Bet> getBetsByUserId(long userId);
}