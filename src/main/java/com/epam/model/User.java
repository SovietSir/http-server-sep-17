package com.epam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
    private long id;
    private String login;
    private int passwordHash;
    private int balance;
}