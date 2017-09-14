package com.epam.dao;

interface DAOCrud<Value, Id> {
    void create(Value value);

    Value read(Id id);

    void update(Id id, Value value);

    void deleteById(Id id);
}
