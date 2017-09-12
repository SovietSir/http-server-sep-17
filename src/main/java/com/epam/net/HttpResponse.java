package com.epam.net;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.val;

import java.util.Map;

@EqualsAndHashCode
@AllArgsConstructor
class HttpResponse {
    @NonNull private HttpCodes code;
    private Map<String, String> headers;
    private String body;

    HttpResponse(HttpCodes code) {
        this(code, null, null);
    }

    @Override
    public String toString() {
        val sb = new StringBuilder("HTTP/1.1 ");
        sb.append(code);
        if (headers != null) {
            headers.forEach((k, v) -> sb.append(String.format("%n%s: %s", k, v)));
        }
        if (body != null) {
            sb.append(String.format("%n%n%s", body));
        }
        return sb.toString();
    }
}
