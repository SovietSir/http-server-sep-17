package com.epam.dao;

import com.epam.model.Event;
import com.epam.net.HttpCodes;
import com.epam.net.HttpResponse;
import com.epam.store.ConnectionPool;
import io.vavr.Tuple2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

    private EventDAOImpl() {}

    @Override
    public List<Event> readAll() throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {
            ArrayList<Event> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(Event.getFromResultSet(resultSet));
            }
            return list;
        }
    }

    @Override
    public List<Event> readEventsByLeagueId(long leagueId) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_LEAGUE_ID)) {
            preparedStatement.setLong(1, leagueId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Event> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(Event.getFromResultSet(resultSet));
            }
            return list;
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
                if (resultSet.next()) {
                    return Event.getFromResultSet(resultSet);
                } else {
                    throw new NoSuchElementException();
                }
            }
        }
    }

    @Override
    public Event read(Long id) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Event.getFromResultSet(resultSet);
                } else {
                    throw new NoSuchElementException();
                }
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
                if (resultSet.next()) {
                    return Event.getFromResultSet(resultSet);
                } else {
                    throw new NoSuchElementException();
                }
            }
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        }
    }

    @Override
    public HttpResponse respondOnGET(List<Tuple2<String, Long>> tuples) {
        String json;
        try {
            Tuple2<String, Long> tuple = tuples.get(0);
            if (tuples.size() == 1) {
                if (tuple._2 == null) {
                    json = gson.toJson(readAll());
                } else {
                    json = gson.toJson(read(tuple._2));
                }
            } else {
                json = gson.toJson(OfferDAOImpl.OFFER_DAO.readOffersByEventId(tuple._2));
            }
        } catch (SQLException e) {
            return new HttpResponse(HttpCodes.INTERNAL_SERVER_ERROR);
        } catch (NoSuchElementException e) {
            return new HttpResponse(HttpCodes.NOT_FOUND);
        }
        return new HttpResponse(json);
    }
}
