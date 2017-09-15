package com.epam.net;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode
@AllArgsConstructor
public class HttpResponse {
    @NonNull
    private HttpCodes code;
    private Map<String, String> headers;
    private String body;

    HttpResponse(HttpCodes code) {
        this(code, null, null);
    }

    HttpResponse(String JSON) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        this.code = HttpCodes.OK;
        this.headers = headers;
        this.body = JSON;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("HTTP/1.1 ");
        sb.append(code);
        if (headers != null) {
            headers.forEach((k, v) -> sb.append(String.format("%n%s: %s", k, v)));
        }
        if (body != null) {
            sb.append(String.format("%n%n%s", body));
        }
        sb.append(String.format("%n%n"));
        return sb.toString();
    }
}
