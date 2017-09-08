package com.epam.daoInterfaces;

interface DAOCrud<Value, Id> {
    abstract void create(Value value);

    abstract Value readById(Id id);

    abstract void update(Value value);

    abstract void deleteById(Id id);
}