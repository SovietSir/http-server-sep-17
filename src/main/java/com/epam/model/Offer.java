package com.epam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Offer {
    private long id;
    private long event_id;
    private String description;
    private float coefficient;
    private boolean result;
}