package com.epam.model;

import lombok.Value;

@Value
public class Bet {
    private long offer_id;
    private long user_id;
    private int amount;
    private int gain;
}