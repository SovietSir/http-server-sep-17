package com.epam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@AllArgsConstructor
public class Offer {
    private long id;
    private long eventId;
    private String description;
    private float coefficient;
    private boolean result;

    public static Offer getFromResultSet(ResultSet resultSet) throws SQLException {
        return new Offer(resultSet.getLong("id"),
                resultSet.getLong("event_id"),
                resultSet.getString("description"),
                resultSet.getFloat("coefficient"),
                resultSet.getBoolean("result"));
    }
}
