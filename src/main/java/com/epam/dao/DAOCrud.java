package com.epam.dao;

import java.sql.SQLException;

interface DAOCrud<Value, Id> {
    Value create(Value value) throws SQLException;

    Value read(Id id) throws SQLException;

    Value update(Id id, Value value) throws SQLException;

    void deleteById(Id id) throws SQLException;
}
