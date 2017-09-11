package com.epam.dao;

import com.epam.daoInterfaces.BetDAO;
import com.epam.model.Bet;

import java.util.List;

public class BetDAOImpl implements BetDAO {
    @Override
    public List<Bet> readBetsByOfferId(long offerId) {
        return null;
    }

    @Override
    public List<Bet> readBetsByPersonId(long personId) {
        return null;
    }

    @Override
    public void create(Bet bet) {

    }

    @Override
    public Bet readById(Long aLong) {
        return null;
    }

    @Override
    public void update(Long aLong, Bet bet) {

    }

    @Override
    public void deleteById(Long aLong) {

    }
}
