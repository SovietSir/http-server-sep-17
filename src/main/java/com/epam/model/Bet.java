package com.epam.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Bet {
    private long id;
    private long personId;
    private long offerId;
    private long amount;
    private float gain;
}
