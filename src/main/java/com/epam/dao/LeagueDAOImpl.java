package com.epam.dao;

import com.epam.model.League;
import com.epam.store.ConnectionPool;
import lombok.SneakyThrows;
import lombok.val;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LeagueDAOImpl implements LeagueDAO {
    /**
     * SQL queries that calls stored procedures in database
     */
    private static final String SELECT_ALL = "SELECT id, name FROM select_all_leagues()";
    private static final String SELECT_BY_ID = "SELECT id, name FROM select_league_by_id(?)";
    private static final String INSERT = "SELECT insert_league(?)";
    private static final String DELETE = "SELECT delete_league(?)";
    private static final String UPDATE = "SELECT update_league(?, ?)";

    @SneakyThrows
    @Override
    public List<League> readAll() {
        try (Connection connection = ConnectionPool.pool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {
            val list = new ArrayList<League>();
            while (resultSet.next()) {
                list.add(new League(resultSet.getLong("id"),
                        resultSet.getString("name")));
            }
            return list;
        }
    }

    @SneakyThrows
    @Override
    public void create(League league) {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            preparedStatement.setString(1, league.getName());
            preparedStatement.execute();
        }
    }

    @SneakyThrows
    @Override
    public League read(Long id) {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new League(resultSet.getLong("id"), resultSet.getString("name"));
                }
            }
            return null;
        }
    }

    @SneakyThrows
    @Override
    public void update(Long id, League league) {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, league.getName());
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
