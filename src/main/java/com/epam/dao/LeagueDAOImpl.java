package com.epam.dao;

import com.epam.model.Event;
import com.epam.model.League;
import com.epam.store.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class LeagueDAOImpl implements LeagueDAO {
    /**
     * SQL queries that calls stored procedures in database
     */
    private static final String SELECT_ALL =
            "SELECT id, name FROM select_all_leagues()";
    private static final String SELECT_BY_ID =
            "SELECT id, name FROM select_league_by_id(?)";
    private static final String INSERT =
            "SELECT id, name FROM insert_league(?)";
    private static final String UPDATE =
            "SELECT id, name FROM update_league(?, ?)";
    private static final String DELETE =
            "SELECT delete_league(?)";

    public static LeagueDAO LEAGUE_DAO = new LeagueDAOImpl();

    private LeagueDAOImpl() {}

    @Override
    public List<League> readAll() throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {
            ArrayList<League> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(League.getFromResultSet(resultSet));
            }
            return list;
        }
    }

    @Override
    public List<Event> readSubLevel(Long id) throws SQLException {
        return EventDAOImpl.EVENT_DAO.readEventsByLeagueId(id);
    }

    @Override
    public League create(League league) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            preparedStatement.setString(1, league.getName());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return League.getFromResultSet(resultSet);
                } else {
                    throw new NoSuchElementException();
                }
            }
        }
    }

    @Override
    public League read(Long id) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return League.getFromResultSet(resultSet);
                } else {
                    throw new NoSuchElementException();
                }
            }
        }
    }

    @Override
    public League update(Long id, League league) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, league.getName());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return League.getFromResultSet(resultSet);
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
