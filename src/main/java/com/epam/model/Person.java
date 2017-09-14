package com.epam.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Person {
    private long id;
    private String login;
    private int passwordHash;
    private long balance;
}
