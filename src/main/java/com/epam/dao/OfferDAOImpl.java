package com.epam.dao;

import com.epam.model.Bet;
import com.epam.model.Offer;
import com.epam.store.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class OfferDAOImpl implements OfferDAO {
    /**
     * SQL queries that calls stored procedures in database
     */
    private static final String SELECT_BY_EVENT_ID =
            "SELECT id, event_id, description, coefficient, result FROM select_offers_by_event_id(?)";
    private static final String SELECT_BY_ID =
            "SELECT id, event_id, description, coefficient, result FROM select_offer_by_id(?)";
    private static final String INSERT =
            "SELECT id, event_id, description, coefficient, result FROM insert_offer(?, ?, ?, ?)";
    private static final String UPDATE =
            "SELECT id, event_id, description, coefficient, result FROM update_offer(?, ?, ?, ?, ?)";
    private static final String DELETE =
            "SELECT delete_offer(?)";

    public static OfferDAO OFFER_DAO = new OfferDAOImpl();

    private OfferDAOImpl() {}

    @Override
    public List<Offer> readOffersByEventId(long eventId) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_EVENT_ID)) {
            preparedStatement.setLong(1, eventId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Offer> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(Offer.getFromResultSet(resultSet));
            }
            return list;
        }
    }

    @Override
    public Offer create(Offer offer) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            preparedStatement.setLong(1, offer.getEventId());
            preparedStatement.setString(2, offer.getDescription());
            preparedStatement.setFloat(3, offer.getCoefficient());
            preparedStatement.setBoolean(4, offer.isResult());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Offer.getFromResultSet(resultSet);
                } else {
                    throw new NoSuchElementException();
                }
            }
        }
    }

    @Override
    public Offer read(Long id) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Offer.getFromResultSet(resultSet);
                } else {
                    throw new NoSuchElementException();
                }
            }
        }
    }

    @Override
    public List<Offer> readAll() throws SQLException {
        throw new BadRequestException();
    }

    @Override
    public List<Bet> readSubLevel(Long id) throws SQLException {
        return BetDAOImpl.BET_DAO.readBetsByOfferId(id);
    }

    @Override
    public Offer update(Long id, Offer offer) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, offer.getEventId());
            preparedStatement.setString(3, offer.getDescription());
            preparedStatement.setFloat(4, offer.getCoefficient());
            preparedStatement.setBoolean(5, offer.isResult());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Offer.getFromResultSet(resultSet);
                } else {
                    throw new NoSuchElementException();
                }
            }
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        }
    }
}
