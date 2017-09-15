package com.epam.dao;

import com.epam.model.Bet;
import com.epam.store.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
    private static final String INSERT =
            "SELECT id, person_id, offer_id, amount, gain FROM insert_bet(?, ?, ?, ?)";
    private static final String UPDATE =
            "SELECT id, person_id, offer_id, amount, gain FROM update_bet(?, ?, ?, ?, ?)";
    private static final String DELETE =
            "SELECT delete_bet(?)";

    public static BetDAO BET_DAO = new BetDAOImpl();

    private BetDAOImpl() {}

    @Override
    public List<Bet> readBetsByOfferId(long offerId) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_OFFER_ID)) {
            preparedStatement.setLong(1, offerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Bet> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(Bet.getFromResultSet(resultSet));
            }
            return list;
        }
    }

    @Override
    public List<Bet> readBetsByPersonId(long personId) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_PERSON_ID)) {
            preparedStatement.setLong(1, personId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ArrayList<Bet> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(Bet.getFromResultSet(resultSet));
                }
                return list;
            }
        }
    }

    @Override
    public Bet create(Bet bet) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            preparedStatement.setLong(1, bet.getPersonId());
            preparedStatement.setLong(2, bet.getOfferId());
            preparedStatement.setLong(3, bet.getAmount());
            preparedStatement.setFloat(4, bet.getGain());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Bet.getFromResultSet(resultSet);
                } else {
                    throw new NoSuchElementException();
                }
            }
        }
    }

    @Override
    public Bet read(Long id) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Bet.getFromResultSet(resultSet);
                } else {
                    throw new NoSuchElementException();
                }
            }
        }
    }

    @Override
    public List<Bet> readAll() throws SQLException {
        throw new BadRequestException();
    }

    @Override
    public List<Void> readSubLevel(Long aLong) throws SQLException {
        throw new BadRequestException();
    }

    @Override
    public Bet update(Long id, Bet bet) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, bet.getPersonId());
            preparedStatement.setLong(3, bet.getOfferId());
            preparedStatement.setLong(4, bet.getAmount());
            preparedStatement.setFloat(5, bet.getGain());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Bet.getFromResultSet(resultSet);
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
