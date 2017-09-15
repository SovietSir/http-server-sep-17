package com.epam.dao;

import com.epam.model.Bet;
import com.epam.model.Person;

public interface PersonDAO extends DAOCrud<Person, Long, Bet> {
    @Override
    default Class<Person> getModelClass() {
        return Person.class;
    }
}
