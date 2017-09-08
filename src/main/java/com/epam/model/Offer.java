package com.epam.model;

import lombok.Value;

@Value
public class Offer {
    private long id;
    private long event_id;
    private String description;
    private float coefficient;
    private boolean result;
}