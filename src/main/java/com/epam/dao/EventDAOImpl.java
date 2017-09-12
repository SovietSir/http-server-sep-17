package com.epam.dao;

import com.epam.daoInterfaces.EventDAO;
import com.epam.model.Event;
import com.epam.store.ConnectionPool;
import lombok.SneakyThrows;
import lombok.val;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAOImpl implements EventDAO {
    /**
     * SQL queries that calls stored procedures in database
     */
    private static final String SELECT_ALL =
            "SELECT id, league_id, date, home_team, guest_team, score FROM select_all_events()";
    private static final String SELECT_BY_LEAGUE_ID =
            "SELECT id, league_id, date, home_team, guest_team, score FROM select_events_by_league_id(?)";
    private static final String SELECT_BY_ID =
            "SELECT id, league_id, date, home_team, guest_team, score FROM select_event_by_id(?)";
    private static final String INSERT = "SELECT insert_event(?, ?, ?, ?, ?)";
    private static final String DELETE = "SELECT delete_event(?)";
    private static final String UPDATE = "SELECT update_event(?, ?, ?, ?, ?, ?)";

    @SneakyThrows
    @Override
    public List<Event> readAll() {
        try (Connection connection = ConnectionPool.pool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {
            val list = new ArrayList<Event>();
            while (resultSet.next()) {
                list.add(new Event(resultSet.getLong("id"),
                        resultSet.getLong("league_id"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("home_team"),
                        resultSet.getString("guest_team"),
                        resultSet.getString("score")
                ));
            }
            return list;
        }
    }

    @SneakyThrows
    @Override
    public List<Event> readEventsByLeagueId(long leagueId) {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_LEAGUE_ID)) {
            preparedStatement.setLong(1, leagueId);
            ResultSet resultSet = preparedStatement.executeQuery();
            val list = new ArrayList<Event>();
            while (resultSet.next()) {
                list.add(new Event(resultSet.getLong("id"),
                        resultSet.getLong("league_id"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("home_team"),
                        resultSet.getString("guest_team"),
                        resultSet.getString("score")
                ));
            }
            return list;
        }
    }

    @SneakyThrows
    @Override
    public void create(Event event) {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            preparedStatement.setLong(1, event.getLeagueId());
            preparedStatement.setTimestamp(2, new Timestamp(event.getDate().getTime()));
            preparedStatement.setString(3, event.getHomeTeam());
            preparedStatement.setString(4, event.getGuestTeam());
            preparedStatement.setString(5, event.getScore());
            preparedStatement.execute();
        }
    }

    @SneakyThrows
    @Override
    public Event readById(Long id) {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    new Event(resultSet.getLong("id"),
                            resultSet.getLong("league_id"),
                            resultSet.getTimestamp("date"),
                            resultSet.getString("home_team"),
                            resultSet.getString("guest_team"),
                            resultSet.getString("score"));
                }
            }
            return null;
        }
    }

    @SneakyThrows
    @Override
    public void update(Long id, Event event) {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, event.getLeagueId());
            preparedStatement.setTimestamp(3, new Timestamp(event.getDate().getTime()));
            preparedStatement.setString(4, event.getHomeTeam());
            preparedStatement.setString(5, event.getGuestTeam());
            preparedStatement.setString(6, event.getScore());
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
