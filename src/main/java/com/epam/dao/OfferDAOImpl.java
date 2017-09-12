package com.epam.dao;

import com.epam.daoInterfaces.OfferDAO;
import com.epam.model.Offer;
import com.epam.store.ConnectionPool;
import lombok.SneakyThrows;
import lombok.val;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OfferDAOImpl implements OfferDAO {
    /**
     * SQL queries that calls stored procedures in database
     */
    private static final String SELECT_BY_EVENT_ID =
            "SELECT id, event_id, description, coefficient, result FROM select_offers_by_event_id(?)";
    private static final String SELECT_BY_ID =
            "SELECT id, event_id, description, coefficient, result FROM select_offer_by_id(?)";
    private static final String INSERT = "SELECT insert_offer(?, ?, ?, ?)";
    private static final String DELETE = "SELECT delete_offer(?)";
    private static final String UPDATE = "SELECT update_offer(?, ?, ?, ?, ?)";

    @SneakyThrows
    @Override
    public List<Offer> readOffersByEventId(long eventId) {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_EVENT_ID)) {
            preparedStatement.setLong(1, eventId);
            ResultSet resultSet = preparedStatement.executeQuery();
            val list = new ArrayList<Offer>();
            while (resultSet.next()) {
                list.add(new Offer(resultSet.getLong("id"),
                        resultSet.getLong("event_id"),
                        resultSet.getString("description"),
                        resultSet.getFloat("coefficient"),
                        resultSet.getBoolean("result")
                ));
            }
            return list;
        }
    }

    @SneakyThrows
    @Override
    public void create(Offer offer) {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            preparedStatement.setLong(1, offer.getEventId());
            preparedStatement.setString(2, offer.getDescription());
            preparedStatement.setFloat(3, offer.getCoefficient());
            preparedStatement.setBoolean(4, offer.isResult());
            preparedStatement.execute();
        }
    }

    @SneakyThrows
    @Override
    public Offer readById(Long id) {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    new Offer(resultSet.getLong("id"),
                            resultSet.getLong("event_id"),
                            resultSet.getString("description"),
                            resultSet.getFloat("coefficient"),
                            resultSet.getBoolean("result"));
                }
            }
            return null;
        }
    }

    @SneakyThrows
    @Override
    public void update(Long id, Offer offer) {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, offer.getEventId());
            preparedStatement.setString(3, offer.getDescription());
            preparedStatement.setFloat(4, offer.getCoefficient());
            preparedStatement.setBoolean(5, offer.isResult());
            preparedStatement.execute();
        }
    }

    @SneakyThrows
    @Override
    public void deleteById(Long id) {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        }
    }
}
