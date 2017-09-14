package com.epam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@AllArgsConstructor
public class Bet {
    private long id;
    private long personId;
    private long offerId;
    private long amount;
    private float gain;

    public static Bet getFromResultSet(ResultSet resultSet) throws SQLException {
        return new Bet(resultSet.getLong("id"),
                resultSet.getLong("person_id"),
                resultSet.getLong("offer_id"),
                resultSet.getLong("amount"),
                resultSet.getFloat("gain"));
    }
}
