package com.epam.dao;

import com.epam.model.Person;
import com.epam.net.HttpCodes;
import com.epam.net.HttpResponse;
import com.epam.store.ConnectionPool;
import io.vavr.Tuple2;
import lombok.val;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class PersonDAOImpl implements PersonDAO {
    /**
     * SQL queries that calls stored procedures in database
     */
    private static final String SELECT_ALL =
            "SELECT id, login, password_hash, balance FROM select_all_persons()";
    private static final String SELECT_BY_ID =
            "SELECT id, login, password_hash, balance FROM select_person_by_id(?)";
    private static final String INSERT =
            "SELECT id, login, password_hash, balance FROM insert_person(?, ?, ?)";
    private static final String UPDATE =
            "SELECT id, login, password_hash, balance FROM update_person(?, ?, ?, ?)";
    private static final String DELETE =
            "SELECT delete_person(?)";

    public static final PersonDAO PERSON_DAO = new PersonDAOImpl();

    private PersonDAOImpl() {}

    @Override
    public List<Person> readAll() throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {
            val list = new ArrayList<Person>();
            while (resultSet.next()) {
                list.add(Person.getFromResultSet(resultSet));
            }
            return list;
        }
    }

    @Override
    public Person create(Person person) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            preparedStatement.setString(1, person.getLogin());
            preparedStatement.setInt(2, person.getPasswordHash());
            preparedStatement.setLong(3, person.getBalance());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Person.getFromResultSet(resultSet);
                } else {
                    throw new NoSuchElementException();
                }
            }
        }
    }

    @Override
    public Person read(Long id) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Person.getFromResultSet(resultSet);
                } else {
                    throw new NoSuchElementException();
                }
            }
        }
    }

    @Override
    public Person update(Long id, Person person) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, person.getLogin());
            preparedStatement.setInt(3, person.getPasswordHash());
            preparedStatement.setLong(4, person.getBalance());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Person.getFromResultSet(resultSet);
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
                json = gson.toJson(BetDAOImpl.BET_DAO.readBetsByPersonId(tuple._2));
            }
        } catch (SQLException e) {
            return new HttpResponse(HttpCodes.INTERNAL_SERVER_ERROR);
        } catch (NoSuchElementException e) {
            return new HttpResponse(HttpCodes.NOT_FOUND);
        }
        return new HttpResponse(json);
    }
}
