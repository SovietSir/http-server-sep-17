package com.epam.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Bet {
    private long id;
    private long offer_id;
    private long user_id;
    private int amount;
    private int gain;
}