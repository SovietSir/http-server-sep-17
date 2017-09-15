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
public class Offer {
    private long id;
    private long eventId;
    private String description;
    private float coefficient;
    private boolean result;

    public static Offer getFromResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return extract(resultSet);
        } else {
            throw new NoSuchElementException();
        }
    }

    public static List<Offer> getAllFromResultSet(ResultSet resultSet) throws SQLException {
        List<Offer> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(extract(resultSet));
        }
        return list;
    }

    private static Offer extract(ResultSet resultSet) throws SQLException {
        return new Offer(resultSet.getLong("id"),
                resultSet.getLong("event_id"),
                resultSet.getString("description"),
                resultSet.getFloat("coefficient"),
                resultSet.getBoolean("result"));
    }
}
