package com.epam.net;

import io.vavr.Tuple2;
import lombok.val;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Respondent {
    private static final String[] PATHS =
            {"leagues", "events", "offers", "users", "bets"};
    static {
        Arrays.sort(PATHS);
    }

    String getResponse(String request) {
        String[] contentAndTail = request.split("\r\n", 2);
        String[] methodAndPath = contentAndTail[0].split(" ");

        HttpMethod method;
        try {
            method = HttpMethod.valueOf(methodAndPath[0]);
        } catch (IllegalArgumentException e) {
            return createResponse(HttpCodes.NOT_IMPLEMENTED);
        }

        String path = methodAndPath[1];
        if (path.equals("/")) {
            return createResponse(HttpCodes.OK, "{\"content\": \"start page\"}");
        }
        List<Tuple2<String, Long>> tuples = parse(path);
        if (tuples == null) {
            //syntax error
            return createResponse(HttpCodes.BAD_REQUEST);
        }

        contentAndTail = contentAndTail[1].split("\r\n\r\n");
        String body = contentAndTail.length == 1 ? null : contentAndTail[1];

        return getResponseFromCorrectInput(method, tuples, body);
    }

    private String getResponseFromCorrectInput
            (HttpMethod method, List<Tuple2<String, Long>> tuples, String body) {
        switch (method) {
            case GET:
                return respondOnGET(tuples);
            case POST:
                return respondOnPOST(tuples, body);
            case PUT:
                return respondOnPUT(tuples, body);
            case DELETE:
                return respondOnDELETE(tuples);
            default:
                return createResponse(HttpCodes.NOT_IMPLEMENTED);
        }
    }

    /**
     *
     * @param path  path to parse
     * @return      null if syntax error, otherwise tuples of String and Long -
     *              the entity and id
     */
    List<Tuple2<String, Long>> parse(String path) {
        if (!path.startsWith("/")) return null;
        String[] tokens = path.substring(1).split("/");
        int idx = 0;
        val list = new ArrayList<Tuple2<String, Long>>(2);
        while (idx < tokens.length) {
            String entity = tokens[idx++];
            if (Arrays.binarySearch(PATHS, entity) < 0) return null;
            Long id = null;
            if (!(idx == tokens.length)) {
                id = extractLong(tokens[idx++]);
                //if NumberFormatException while extracting id
                if (id == null) return null;
            }
            list.add(new Tuple2<>(entity, id));
        }
        return list;
    }

    private Long extractLong(String line) {
        try {
            return Long.parseLong(line);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String respondOnGET(List<Tuple2<String, Long>> tuples) {
        return "{\"method\": \"GET\"}";
    }

    private String respondOnPOST(List<Tuple2<String, Long>> tuples, String body) {
        return null;
    }

    private String respondOnPUT(List<Tuple2<String, Long>> tuples, String body) {
        return null;
    }

    private String respondOnDELETE(List<Tuple2<String, Long>> tuples) {
        return null;
    }

    private String createResponse(String code) {
        return String.format("HTTP/1.1 %s\r\n\r\n", code);
    }

    private String createResponse(String code, String JSON) {
        return String.format(
                "HTTP/1.1 %s\r\nContent-Type: application/json\r\n\r\n%s",
                code, JSON);
    }
}