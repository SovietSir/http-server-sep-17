package com.epam.dao;

import com.epam.model.Person;

import java.sql.SQLException;
import java.util.List;

public interface PersonDAO extends AbstractDAO<Person> {
    List<Person> readAll() throws SQLException;

    @Override
    default Class<Person> getModelClass() {
        return Person.class;
    }
}
