package com.epam.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Person {
    private long id;
    private String login;
    private int passwordHash;
    private long balance;

    public static Person getFromResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return extract(resultSet);
        } else {
            throw new NoSuchElementException();
        }
    }

    public static List<Person> getAllFromResultSet(ResultSet resultSet) throws SQLException {
        List<Person> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(extract(resultSet));
        }
        return list;
    }

    private static Person extract(ResultSet resultSet) throws SQLException {
        return new Person(resultSet.getLong("id"),
                resultSet.getString("login"),
                resultSet.getInt("password_hash"),
                resultSet.getLong("balance"));
    }
}
