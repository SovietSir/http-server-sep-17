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
public class League {
    private long id;
    private String name;

    public static League getFromResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return extract(resultSet);
        } else {
            throw new NoSuchElementException();
        }
    }

    public static List<League> getAllFromResultSet(ResultSet resultSet) throws SQLException {
        List<League> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(extract(resultSet));
        }
        return list;
    }

    private static League extract(ResultSet resultSet) throws SQLException {
        return new League(resultSet.getLong("id"),
                resultSet.getString("name"));
    }
}
