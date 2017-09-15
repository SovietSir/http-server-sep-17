package com.epam.dao;

import com.epam.model.Bet;
import com.epam.model.Person;
import com.epam.store.ConnectionPool;

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

    public static PersonDAO PERSON_DAO = new PersonDAOImpl();

    private PersonDAOImpl() {}

    @Override
    public List<Person> readAll() throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {
            ArrayList<Person> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(Person.getFromResultSet(resultSet));
            }
            return list;
        }
    }

    @Override
    public List<Bet> readSubLevel(Long id) throws SQLException {
        return BetDAOImpl.BET_DAO.readBetsByPersonId(id);
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
    public void delete(Long id) throws SQLException {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        }
    }
}
