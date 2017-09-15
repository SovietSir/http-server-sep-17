package com.epam.net;

import com.epam.dao.*;
import com.google.gson.*;
import io.vavr.Tuple2;
import lombok.Setter;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

@SuppressWarnings("ConstantConditions")
@Setter
class Respondent {
    private static final String[] PATHS =
            {"leagues", "events", "offers", "persons", "bets"};

    static {
        Arrays.sort(PATHS);
    }

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                return LocalDateTime.parse(json.getAsString(), formatter);
            })
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (date, type, jsonSerializationContext) -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                return date == null ? null : new JsonPrimitive(date.format(formatter));
            })
            .create();

    HttpResponse getResponse(String request) {
        String[] contentAndTail = request.split("\r\n\r\n", 2);
        if (contentAndTail.length == 0) {
            new HttpResponse(HttpCodes.BAD_REQUEST);
        }
        String body = contentAndTail.length == 1 ? null : contentAndTail[1];
        contentAndTail = contentAndTail[0].split("\r\n", 2);
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

        DAOCrud dao;
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

    //I could do it with Predicate<List<Tuple2...>>...
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
