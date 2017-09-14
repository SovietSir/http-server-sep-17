package com.epam.dao;

import com.epam.daoInterfaces.PersonDAO;
import com.epam.model.Person;
import com.epam.store.ConnectionPool;
import lombok.SneakyThrows;
import lombok.val;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PersonDAOImpl implements PersonDAO {
    /**
     * SQL queries that calls stored procedures in database
     */
    private static final String SELECT_ALL = "SELECT id, login, password_hash, balance FROM select_all_persons()";
    private static final String SELECT_BY_ID = "SELECT id, login, password_hash, balance FROM select_person_by_id(?)";
    private static final String INSERT = "SELECT insert_person(?, ?, ?)";
    private static final String DELETE = "SELECT delete_person(?)";
    private static final String UPDATE = "SELECT update_person(?, ?, ?, ?)";

    @SneakyThrows
    @Override
    public List<Person> readAll() {
        try (Connection connection = ConnectionPool.pool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {
            val list = new ArrayList<Person>();
            while (resultSet.next()) {
                list.add(new Person(resultSet.getLong("id"),
                        resultSet.getString("login"),
                        resultSet.getInt("password_hash"),
                        resultSet.getLong("balance")));
            }
            return list;
        }
    }

    @SneakyThrows
    @Override
    public void create(Person person) {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            preparedStatement.setString(1, person.getLogin());
            preparedStatement.setInt(2, person.getPasswordHash());
            preparedStatement.setLong(3, person.getBalance());
            preparedStatement.execute();
        }
    }

    @SneakyThrows
    @Override
    public Person read(Long id) {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Person(resultSet.getLong("id"),
                            resultSet.getString("login"),
                            resultSet.getInt("password_hash"),
                            resultSet.getLong("balance"));
                }
            }
            return null;
        }
    }

    @SneakyThrows
    @Override
    public void update(Long id, Person person) {
        try (Connection connection = ConnectionPool.pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, person.getLogin());
            preparedStatement.setInt(3, person.getPasswordHash());
            preparedStatement.setLong(4, person.getBalance());
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
