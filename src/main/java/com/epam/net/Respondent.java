package com.epam.net;

import com.epam.dao.*;
import io.vavr.Tuple2;
import lombok.Setter;
import lombok.val;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ConstantConditions")
@Setter
class Respondent {
    private static final String[] PATHS =
            {"leagues", "events", "offers", "persons", "bets"};

    static {
        Arrays.sort(PATHS);
    }

    HttpResponse getResponse(String request) {
        String[] contentAndTail = request.split("\r\n", 2);
        String[] methodAndPath = contentAndTail[0].split(" ");

        HttpMethod method;
        try {
            method = HttpMethod.valueOf(methodAndPath[0]);
        } catch (IllegalArgumentException e) {
            return new HttpResponse(HttpCodes.NOT_IMPLEMENTED);
        }

        String path = methodAndPath[1];
        if (path.equals("/")) {
            return new HttpResponse("{\"content\": \"start page\"}");
        }
        List<Tuple2<String, Long>> tuples = parse(path);
        if (tuples == null) {
            //syntax error
            return new HttpResponse(HttpCodes.BAD_REQUEST);
        }

        AbstractDAO dao;
        switch (tuples.get(0)._1) {
            case "leagues":
                dao = LeagueDAOImpl.LEAGUE_DAO;
                break;
            case "events":
                dao = EventDAOImpl.EVENT_DAO;
                break;
            case "offers":
                dao = OfferDAOImpl.OFFER_DAO;
                break;
            case "persons":
                dao = PersonDAOImpl.PERSON_DAO;
                break;
            case "bets":
                dao = BetDAOImpl.BET_DAO;
                break;
            default:
                return new HttpResponse(HttpCodes.BAD_REQUEST);
        }

        contentAndTail = contentAndTail[1].split("\r\n\r\n");
        String body = contentAndTail.length == 1 ? null : contentAndTail[1];

        switch (method) {
            case GET:
                return dao.respondOnGET(tuples);
            case POST:
                if (tuples.size() == 1 && tuples.get(0)._2 != null) {
                    return dao.respondOnPOST(tuples.get(0)._2, body);
                }
                break;
            case PUT:
                if (tuples.size() == 1 && tuples.get(0)._2 == null) {
                    return dao.respondOnPUT(body);
                }
                break;
            case DELETE:
                if (tuples.size() == 1 && tuples.get(0)._2 != null) {
                    return dao.respondOnDELETE(tuples.get(0)._2);
                }
                break;
            default:
                return new HttpResponse(HttpCodes.NOT_IMPLEMENTED);
        }
        return new HttpResponse(HttpCodes.BAD_REQUEST);
    }

    /**
     * @param path path to parse
     * @return null if syntax error, otherwise tuples of String and Long -
     * the entity and id
     */
    List<Tuple2<String, Long>> parse(String path) {
        if (!path.startsWith("/")) return null;
        String[] tokens = path.substring(1).split("/");
        if (tokens.length == 0) return null;
        int idx = 0;
        val list = new ArrayList<Tuple2<String, Long>>(2);
        String nextDir = null;
        while (idx < tokens.length) {
            String entity = tokens[idx++];
            if (Arrays.binarySearch(PATHS, entity) < 0) return null;
            if (nextDir != null && !entity.equals(nextDir)) return null;
            switch (entity) {
                case "leagues":
                    nextDir = "events";
                    break;
                case "events":
                    nextDir = "offers";
                    break;
                case "offers":
                case "persons":
                    nextDir = "bets";
                    break;
                default:
                    nextDir = null;
            }
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
}
