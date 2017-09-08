package com.epam.model;

import lombok.Value;

@Value
public class User {
    private long id;
    private String login;
    private int passwordHash;
    private int balance;
}