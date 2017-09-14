package com.epam.dao;

import com.epam.daoInterfaces.BetDAO;
import com.epam.model.Bet;
import com.epam.store.ConnectionPool;
import lombok.SneakyThrows;
import lombok.val;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BetDAOImpl implements BetDAO {
    /**
     * SQL queries that calls stored procedures in database
     */
    private static final String SELECT_BY_ID =
            "SELECT id, person_id, offer_id, amount, gain FROM select_bet_by_id(?)";
    private static final String SELECT_BY_PERSON_ID =
            "SELECT id, person_id, offer_id, amount, gain FROM select_bets_by_person_id(?)";
    private static final String SELECT_BY_OFFER_ID =
            "SELECT id, person_id, offer_id, amount, gain FROM select_bets_by_offer_id(?)";
    private static final String INSERT = "SELECT insert_bet(?, ?, ?, ?)";
    private static final String DELETE = "SELECT delete_bet(?)";
    private static final String UPDATE = "SELECT update_bet(?, ?, ?, ?, ?)";

    @SneakyThrows
    @Override
    public List<Bet> readBetsByOfferId(long offerId) {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_OFFER_ID)) {
            preparedStatement.setLong(1, offerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            val list = new ArrayList<Bet>();
            while (resultSet.next()) {
                list.add(new Bet(resultSet.getLong("id"),
                        resultSet.getLong("person_id"),
                        resultSet.getLong("offer_id"),
                        resultSet.getLong("amount"),
                        resultSet.getFloat("gain")
                ));
            }
            return list;
        }
    }

    @SneakyThrows
    @Override
    public List<Bet> readBetsByPersonId(long personId) {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_PERSON_ID)) {
            preparedStatement.setLong(1, personId);
            ResultSet resultSet = preparedStatement.executeQuery();
            val list = new ArrayList<Bet>();
            while (resultSet.next()) {
                list.add(new Bet(resultSet.getLong("id"),
                        resultSet.getLong("person_id"),
                        resultSet.getLong("offer_id"),
                        resultSet.getLong("amount"),
                        resultSet.getFloat("gain")
                ));
            }
            return list;
        }
    }

    @SneakyThrows
    @Override
    public void create(Bet bet) {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            preparedStatement.setLong(1, bet.getPersonId());
            preparedStatement.setLong(2, bet.getOfferId());
            preparedStatement.setLong(3, bet.getAmount());
            preparedStatement.setFloat(4, bet.getGain());
            preparedStatement.execute();
        }
    }

    @SneakyThrows
    @Override
    public Bet readById(Long id) {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    new Bet(resultSet.getLong("id"),
                            resultSet.getLong("person_id"),
                            resultSet.getLong("offer_id"),
                            resultSet.getLong("amount"),
                            resultSet.getFloat("gain"));
                }
            }
            return null;
        }
    }

    @SneakyThrows
    @Override
    public void update(Long id, Bet bet) {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, bet.getPersonId());
            preparedStatement.setLong(3, bet.getOfferId());
            preparedStatement.setLong(4, bet.getAmount());
            preparedStatement.setFloat(5, bet.getGain());
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
