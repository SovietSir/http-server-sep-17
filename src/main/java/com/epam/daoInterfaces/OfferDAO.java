package com.epam.daoInterfaces;

import com.epam.model.Offer;

import java.util.List;

public interface OfferDAO extends DAOCrud<Offer, Long> {
    List<Offer> getOffersByEventId(long eventId);
}