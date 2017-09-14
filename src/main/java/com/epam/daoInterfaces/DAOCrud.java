package com.epam.daoInterfaces;

interface DAOCrud<Value, Id> {
    void create(Value value);

    Value read(Id id);

    void update(Id id, Value value);

    void deleteById(Id id);
}
