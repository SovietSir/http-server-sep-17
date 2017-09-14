package com.epam.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class League {
    private long id;
    private String name;

    public static League getFromResultSet(ResultSet resultSet) throws SQLException {
        return new League(resultSet.getLong("id"),
                resultSet.getString("name"));
    }
}
