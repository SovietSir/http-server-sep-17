package com.epam.dao;

import com.epam.model.Person;

import java.util.List;

public interface PersonDAO extends DAOCrud<Person, Long> {
    List<Person> readAll();
}
