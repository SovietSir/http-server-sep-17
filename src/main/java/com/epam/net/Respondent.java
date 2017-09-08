package com.epam.net;

import lombok.val;

class Respondent {
    String getResponse(String request) {
        return String.format(
                "HTTP/1.1 200 OK\nContent-Type: application/json\n\n%s", parse(request));
    }

    private String parse(String request) {
        val contentAndTail = request.split("\r\n", 2);
        val methodAndPath = contentAndTail[0].split(" ");
        val method = methodAndPath[0];
        val path = methodAndPath[1];

        switch (HttpMethod.valueOf(method)) {
            case GET:
                return get(path);
            case POST:
                return post(path, contentAndTail[1]);
            case PUT:
                return put(path, contentAndTail[1]);
            case DELETE:
                return delete(path, contentAndTail[1]);
            default:
                return unsupported();
        }
    }

    private String get(String path) {
        return "{\"method\": \"GET\"}";
    }

    private String post(String path, String headers) {
        return null;
    }

    private String put(String path, String headers) {
        return null;
    }

    private String delete(String path, String headers) {
        return null;
    }

    private String unsupported() {
        return null;
    }
}