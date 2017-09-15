package com.epam.dao;

import java.sql.SQLException;
import java.util.List;

public interface DAOCrud<Value, Id, SubLevel> {
    Value create(Value value) throws SQLException;

    Value read(Id id) throws SQLException;

    List<Value> readAll() throws SQLException;

    List<SubLevel> readSubLevel(Id id) throws SQLException;

    Value update(Id id, Value value) throws SQLException;

    void delete(Id id) throws SQLException;

    Class<Value> getModelClass();
}
