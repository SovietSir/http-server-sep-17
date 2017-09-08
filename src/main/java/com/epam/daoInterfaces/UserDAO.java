package com.epam.daoInterfaces;

import com.epam.model.User;

import java.util.List;

public interface UserDAO extends DAOCrud<User, Long> {
    List<User> getAll();
}