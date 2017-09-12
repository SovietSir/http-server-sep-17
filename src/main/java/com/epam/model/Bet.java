package com.epam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Bet {
    private long id;
    private long offerId;
    private long personId;
    private int amount;
    private int gain;
}
