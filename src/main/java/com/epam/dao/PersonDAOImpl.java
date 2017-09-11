package com.epam.dao;

import com.epam.daoInterfaces.PersonDAO;
import com.epam.model.Person;

import java.util.List;

public class PersonDAOImpl implements PersonDAO {
    @Override
    public List<Person> readAll() {
        return null;
    }

    @Override
    public void create(Person person) {

    }

    @Override
    public Person readById(Long aLong) {
        return null;
    }

    @Override
    public void update(Long aLong, Person person) {

    }

    @Override
    public void deleteById(Long aLong) {

    }
}
