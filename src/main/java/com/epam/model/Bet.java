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
public class Bet {
    private long id;
    private long personId;
    private long offerId;
    private long amount;
    private float gain;

    public static Bet getFromResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return extract(resultSet);
        } else {
            throw new NoSuchElementException();
        }
    }

    public static List<Bet> getAllFromResultSet(ResultSet resultSet) throws SQLException {
        List<Bet> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(extract(resultSet));
        }
        return list;
    }

    private static Bet extract(ResultSet resultSet) throws SQLException {
        return new Bet(resultSet.getLong("id"),
                resultSet.getLong("person_id"),
                resultSet.getLong("offer_id"),
                resultSet.getLong("amount"),
                resultSet.getFloat("gain"));
    }
}
