package com.epam.dao;

import com.epam.model.Event;
import com.epam.model.Offer;
import com.epam.store.ConnectionPool;

import java.sql.*;
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
    private static final String INSERT =
            "SELECT id, league_id, date, home_team, guest_team, score FROM insert_event(?, ?, ?, ?, ?)";
    private static final String UPDATE =
            "SELECT id, league_id, date, home_team, guest_team, score FROM update_event(?, ?, ?, ?, ?, ?)";
    private static final String DELETE =
            "SELECT delete_event(?)";

    public static EventDAO EVENT_DAO = new EventDAOImpl();

    private EventDAOImpl() {
    }

    @Override
    public List<Event> readAll() throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {
            return Event.getAllFromResultSet(resultSet);
        }
    }

    @Override
    public List<Offer> readSubLevel(Long id) throws SQLException {
        return OfferDAOImpl.OFFER_DAO.readOffersByEventId(id);
    }

    @Override
    public List<Event> readEventsByLeagueId(long leagueId) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_LEAGUE_ID)) {
            preparedStatement.setLong(1, leagueId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return Event.getAllFromResultSet(resultSet);
        }
    }

    @Override
    public Event create(Event event) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            preparedStatement.setLong(1, event.getLeagueId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(event.getDate()));
            preparedStatement.setString(3, event.getHomeTeam());
            preparedStatement.setString(4, event.getGuestTeam());
            preparedStatement.setString(5, event.getScore());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return Event.getFromResultSet(resultSet);
            }
        }
    }

    @Override
    public Event read(Long id) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return Event.getFromResultSet(resultSet);
            }
        }
    }

    @Override
    public Event update(Long id, Event event) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, event.getLeagueId());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(event.getDate()));
            preparedStatement.setString(4, event.getHomeTeam());
            preparedStatement.setString(5, event.getGuestTeam());
            preparedStatement.setString(6, event.getScore());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return Event.getFromResultSet(resultSet);
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
