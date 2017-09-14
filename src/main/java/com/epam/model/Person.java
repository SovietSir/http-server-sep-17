package com.epam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@AllArgsConstructor
public class Person {
    private long id;
    private String login;
    private int passwordHash;
    private long balance;

    public static Person getFromResultSet(ResultSet resultSet) throws SQLException {
        return new Person(resultSet.getLong("id"),
                resultSet.getString("login"),
                resultSet.getInt("password_hash"),
                resultSet.getLong("balance"));
    }
}
