package com.epam.net;

import com.epam.dao.*;
import com.epam.daoInterfaces.*;
import com.epam.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import io.vavr.Tuple2;
import lombok.Setter;
import lombok.val;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("ConstantConditions")
@Setter
class Respondent {
    private static final String[] PATHS =
            {"leagues", "events", "offers", "persons", "bets"};

    static {
        Arrays.sort(PATHS);
    }

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                return LocalDateTime.parse(json.getAsString(), formatter);
            })
            .create();
    private LeagueDAO leagueDAO = new LeagueDAOImpl();
    private EventDAO eventDAO = new EventDAOImpl();
    private OfferDAO offerDAO = new OfferDAOImpl();
    private PersonDAO personDAO = new PersonDAOImpl();
    private BetDAO betDAO = new BetDAOImpl();

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
            return createJSONResponseOK("{\"content\": \"start page\"}");
        }
        List<Tuple2<String, Long>> tuples = parse(path);
        if (tuples == null) {
            //syntax error
            return new HttpResponse(HttpCodes.BAD_REQUEST);
        }

        contentAndTail = contentAndTail[1].split("\r\n\r\n");
        String body = contentAndTail.length == 1 ? null : contentAndTail[1];

        return getResponseFromCorrectInput(method, tuples, body);
    }

    private HttpResponse getResponseFromCorrectInput
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
                return new HttpResponse(HttpCodes.NOT_IMPLEMENTED);
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
        if (tokens.length == 0) return null;
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

    private HttpResponse respondOnGET(List<Tuple2<String, Long>> tuples) {
        try {
            switch (tuples.get(0)._1) {
                case "leagues":
                    return createJSONResponseOK(
                            touchDAO(tuples,
                                    leagueDAO::readAll,
                                    leagueDAO::readById,
                                    eventDAO::readEventsByLeagueId));
                case "events":
                    return createJSONResponseOK(
                            touchDAO(tuples,
                                    eventDAO::readAll,
                                    eventDAO::readById,
                                    offerDAO::readOffersByEventId));
                case "offers":
                    return createJSONResponseOK(
                            touchDAO(tuples,
                                    null,
                                    offerDAO::readById,
                                    betDAO::readBetsByOfferId));
                case "persons":
                    return createJSONResponseOK(
                            touchDAO(tuples,
                                    personDAO::readAll,
                                    personDAO::readById,
                                    betDAO::readBetsByPersonId));
                case "bets":
                    return createJSONResponseOK(
                            touchDAO(tuples,
                                    null,
                                    betDAO::readById,
                                    null));
            }
        } catch (NoSuchElementException e) {
            return new HttpResponse(HttpCodes.NOT_FOUND);
        } catch (NullPointerException ignored) {
        }
        return new HttpResponse(HttpCodes.BAD_REQUEST);
    }

    private HttpResponse respondOnPOST(List<Tuple2<String, Long>> tuples, String body) {
        if (!(tuples.size() == 1 && tuples.get(0)._2 != null)) {
            return new HttpResponse(HttpCodes.BAD_REQUEST);
        }
        Tuple2<String, Long> tuple = tuples.get(0);
        switch (tuple._1) {
            case "leagues":
                leagueDAO.update(tuple._2, gson.fromJson(body, League.class));
                break;
            case "events":
                eventDAO.update(tuple._2, gson.fromJson(body, Event.class));
                break;
            case "offers":
                offerDAO.update(tuple._2, gson.fromJson(body, Offer.class));
                break;
            case "persons":
                personDAO.update(tuple._2, gson.fromJson(body, Person.class));
                break;
            case "bets":
                betDAO.update(tuple._2, gson.fromJson(body, Bet.class));
                break;
            default:
                return new HttpResponse(HttpCodes.BAD_REQUEST);
        }
        return new HttpResponse(HttpCodes.OK);
    }

    private HttpResponse respondOnPUT(List<Tuple2<String, Long>> tuples, String body) {
        if (!(tuples.size() == 1 && tuples.get(0)._2 == null)) {
            return new HttpResponse(HttpCodes.BAD_REQUEST);
        }
        switch (tuples.get(0)._1) {
            case "leagues":
                leagueDAO.create(gson.fromJson(body, League.class));
                break;
            case "events":
                eventDAO.create(gson.fromJson(body, Event.class));
                break;
            case "offers":
                offerDAO.create(gson.fromJson(body, Offer.class));
                break;
            case "persons":
                personDAO.create(gson.fromJson(body, Person.class));
                break;
            case "bets":
                betDAO.create(gson.fromJson(body, Bet.class));
                break;
            default:
                return new HttpResponse(HttpCodes.BAD_REQUEST);
        }
        return new HttpResponse(HttpCodes.OK);
    }

    private HttpResponse respondOnDELETE(List<Tuple2<String, Long>> tuples) {
        if (!(tuples.size() == 1 && tuples.get(0)._2 != null)) {
            return new HttpResponse(HttpCodes.BAD_REQUEST);
        }
        Tuple2<String, Long> tuple = tuples.get(0);
        switch (tuple._1) {
            case "leagues":
                leagueDAO.deleteById(tuple._2);
                break;
            case "events":
                eventDAO.deleteById(tuple._2);
                break;
            case "offers":
                offerDAO.deleteById(tuple._2);
                break;
            case "persons":
                personDAO.deleteById(tuple._2);
                break;
            case "bets":
                betDAO.deleteById(tuple._2);
                break;
            default:
                return new HttpResponse(HttpCodes.BAD_REQUEST);
        }
        return new HttpResponse(HttpCodes.OK);
    }

    /**
     * Do the routine work with interaction with DAO
     *
     * @param tuples     list of tuples, containing parsed path
     * @param levelOne   function that should be called if path
     *                   is first level (e.g. "/leagues")
     * @param levelTwo   function that should be called if path
     *                   is second level (e.g. "/leagues/100")
     * @param levelThree function that should be called if path
     *                   is third level (e.g. "/leagues/100/events")
     * @return generated JSON
     */
    private String touchDAO(List<Tuple2<String, Long>> tuples,
                    Supplier<Object> levelOne,
                    Function<Long, Object> levelTwo,
                    Function<Long, Object> levelThree) {
        String json;
        Tuple2<String, Long> tuple = tuples.get(0);
        if (tuples.size() == 1) {
            if (tuple._2 == null) {
                json = gson.toJson(levelOne.get());
            } else {
                json = gson.toJson(levelTwo.apply(tuple._2));
            }
        } else {
            json = gson.toJson(levelThree.apply(tuple._2));
        }
        if (json == null) throw new NoSuchElementException();
        return json;
    }

    private HttpResponse createJSONResponseOK(String JSON) {
        val headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        return new HttpResponse(HttpCodes.OK, headers, JSON);
    }
}
