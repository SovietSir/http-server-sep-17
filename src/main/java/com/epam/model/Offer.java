package com.epam.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Offer {
    private long id;
    private long eventId;
    private String description;
    private float coefficient;
    private boolean result;
}
