package com.epam.dao;

import com.epam.model.Offer;

import java.sql.SQLException;
import java.util.List;

public interface OfferDAO extends AbstractDAO<Offer> {
    List<Offer> readOffersByEventId(long eventId) throws SQLException;

    @Override
    default Class<Offer> getModelClass() {
        return Offer.class;
    }
}
