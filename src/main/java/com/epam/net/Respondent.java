package com.epam.net;

import com.epam.dao.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import io.vavr.Tuple2;
import lombok.Setter;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Callable;

@SuppressWarnings("ConstantConditions")
class Respondent {
    private static final Set<String> pathSet;

    private static final Map<String, DAOCrud> mapWithDAO = new HashMap<>();

    private static final Map<String, String> nextDirs = new HashMap<>();

    static {
        mapWithDAO.put("leagues", LeagueDAOImpl.LEAGUE_DAO);
        mapWithDAO.put("events", EventDAOImpl.EVENT_DAO);
        mapWithDAO.put("offers", OfferDAOImpl.OFFER_DAO);
        mapWithDAO.put("persons", PersonDAOImpl.PERSON_DAO);
        mapWithDAO.put("bets", BetDAOImpl.BET_DAO);

        pathSet = mapWithDAO.keySet();

        nextDirs.put("leagues", "events");
        nextDirs.put("events", "offers");
        nextDirs.put("offers", "bets");
        nextDirs.put("persons", "bets");
    }

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                return LocalDateTime.parse(json.getAsString(), formatter);
            })
            .create();

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
            if (method == HttpMethod.GET) {
                return new HttpResponse("{\"content\": \"start page\"}");
            } else {
                return new HttpResponse(HttpCodes.BAD_REQUEST);
            }
        }
        List<Tuple2<String, Long>> tuples = parse(path);
        if (tuples == null) {
            //syntax error
            return new HttpResponse(HttpCodes.BAD_REQUEST);
        }

        DAOCrud dao = mapWithDAO.get(tuples.get(0)._1);
        if (dao == null) {
            return new HttpResponse(HttpCodes.BAD_REQUEST);
        }

        contentAndTail = contentAndTail[1].split("\r\n\r\n");
        String body = contentAndTail.length == 1 ? null : contentAndTail[1];

        switch (method) {
            case GET:
                return getResponse(tuples.size() > 0,
                        () -> {
                            Tuple2<String, Long> tuple = tuples.get(0);
                            if (tuples.size() == 1) {
                                if (tuple._2 == null) {
                                    return new HttpResponse(gson.toJson(dao.readAll()));
                                } else {
                                    return new HttpResponse(gson.toJson(dao.read(tuple._2)));
                                }
                            } else {
                                return new HttpResponse(gson.toJson(dao.readSubLevel(tuple._2)));
                            }
                        });
            case POST:
                return getResponse(
                        tuples.size() == 1 && tuples.get(0)._2 != null,
                        () -> new HttpResponse(gson.toJson(
                                dao.update(tuples.get(0)._2, gson.fromJson(body, dao.getModelClass())))));
            case PUT:
                return getResponse(
                        tuples.size() == 1 && tuples.get(0)._2 == null,
                        () -> new HttpResponse(gson.toJson(
                                dao.create(gson.fromJson(body, dao.getModelClass())))));
            case DELETE:
                return getResponse(
                        tuples.size() == 1 && tuples.get(0)._2 != null,
                        () -> {
                            dao.delete(tuples.get(0)._2);
                            return new HttpResponse(HttpCodes.OK);
                        });
            default:
                return new HttpResponse(HttpCodes.NOT_IMPLEMENTED);
        }
    }

    private HttpResponse getResponse(boolean predicate, Callable<HttpResponse> callable) {
        if (!predicate) {
            return new HttpResponse(HttpCodes.BAD_REQUEST);
        }
        try {
            return callable.call();
        } catch (SQLException e) {
            return new HttpResponse(HttpCodes.INTERNAL_SERVER_ERROR);
        } catch (NoSuchElementException e) {
            return new HttpResponse(HttpCodes.NOT_FOUND);
        } catch (Exception e) {
            return new HttpResponse(HttpCodes.BAD_REQUEST);
        }
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
        List<Tuple2<String, Long>> list = new ArrayList<>(2);
        String nextDir = null;
        while (idx < tokens.length) {
            String entity = tokens[idx++];
            if (!pathSet.contains(entity)) return null;
            if (nextDir != null && !entity.equals(nextDir)) return null;
            nextDir = nextDirs.get(entity);
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
