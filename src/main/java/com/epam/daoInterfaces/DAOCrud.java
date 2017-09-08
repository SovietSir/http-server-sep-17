package com.epam.daoInterfaces;

interface DAOCrud<Value, Id> {
    void create(Value value);

    Value readById(Id id);

    void update(Value value);

    void deleteById(Id id);
}