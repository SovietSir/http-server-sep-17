package com.epam.net;

public enum HttpCodes {
    OK(200),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500),
    NOT_IMPLEMENTED(501);

    private int code;

    HttpCodes(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code + " " + super.toString();
    }
}
