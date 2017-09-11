package com.epam.dao;

import com.epam.daoInterfaces.OfferDAO;
import com.epam.model.Offer;

import java.util.List;

public class OfferDAOImpl implements OfferDAO{
    @Override
    public List<Offer> readOffersByEventId(long eventId) {
        return null;
    }

    @Override
    public void create(Offer offer) {

    }

    @Override
    public Offer readById(Long aLong) {
        return null;
    }

    @Override
    public void update(Long aLong, Offer offer) {

    }

    @Override
    public void deleteById(Long aLong) {

    }
}
