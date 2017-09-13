package com.epam.net;

import com.epam.daoInterfaces.*;
import com.epam.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vavr.Tuple2;
import org.testng.annotations.*;

import java.time.Instant;
import java.util.*;

import static java.util.Date.from;
import static org.testng.Assert.*;

class RespondentTest {
    private Respondent respondent;
    private Gson gson;
    private ArrayList<League> leaguesList;
    private ArrayList<Event> eventsList;
    private ArrayList<Offer> offersList;
    private Map<String, String> defaultHeaders;

    @BeforeClass
    void setup() {
        respondent = new Respondent();
        gson = new GsonBuilder()
                .setDateFormat("dd-MM-yyyy HH:mm")
                .create();
        leaguesList = new ArrayList<>();
        eventsList = new ArrayList<>();
        offersList = new ArrayList<>();
        defaultHeaders = new HashMap<String, String>();
        defaultHeaders.put("Content-Type", "application/json");


        leaguesList.add(new League(1, "RFPL"));
        leaguesList.add(new League(2, "APL"));
        leaguesList.add(new League(3, "PDL"));
        leaguesList.add(new League(4, "USPL"));

        eventsList.add(new Event(1, 1, from(Instant.parse("2007-12-03T10:15:30.00Z")), "Zenith", "Nadir", "0:2"));
        eventsList.add(new Event(2, 1, from(Instant.parse("2008-11-04T11:20:35.00Z")), "Andji", "Tom", "1:3"));
        eventsList.add(new Event(3, 1, from(Instant.parse("2009-10-05T12:25:40.00Z")), "Bavaria", "MU", "0:0"));
        eventsList.add(new Event(4, 1, from(Instant.parse("2010-09-06T13:30:45.00Z")), "Dinamo", "Sokol", "3:2"));

        offersList.add(new Offer(1, 1, "Zenith will win Nadir", 0.95f, true));
        offersList.add(new Offer(2, 2, "Andji will lose Tom", 0.65f, true));
        offersList.add(new Offer(3, 3, "Bavaria had to destroy MU", 0.999f, true));
        offersList.add(new Offer(4, 4, "Dinamo will split with Sokol", 0.5f, true));

        respondent.setLeagueDAO(new LeagueDAO() {
            @Override
            public void create(League league) {
            }

            @Override
            public League readById(Long aLong) {
                return leaguesList.get(aLong.intValue());
            }

            @Override
            public void update(Long aLong, League league) {
            }

            @Override
            public void deleteById(Long aLong) {
            }

            @Override
            public List<League> readAll() {
                return leaguesList;
            }
        });

        respondent.setEventDAO(new EventDAO() {
            @Override
            public void create(Event event) {
            }

            @Override
            public Event readById(Long aLong) {
                return eventsList.get(aLong.intValue());
            }

            @Override
            public void update(Long aLong, Event event) {
            }

            @Override
            public void deleteById(Long aLong) {
            }

            @Override
            public List<Event> readAll() {
                return eventsList;
            }

            @Override
            public List<Event> readEventsByLeagueId(long leagueId) {
                return eventsList;
            }
        });

        respondent.setOfferDAO(new OfferDAO() {

            @Override
            public void create(Offer offer) {}

            @Override
            public Offer readById(Long aLong) {
                return offersList.get(aLong.intValue());
            }

            @Override
            public void update(Long aLong, Offer offer) {

            }

            @Override
            public void deleteById(Long aLong) {

            }

            @Override
            public List<Offer> readOffersByEventId(long eventId) {
                int intId = (int) eventId;
                return offersList.subList(intId, intId + 1);
            }
        });


    }

    @Test()
    void parseCorrect() {
        List<Tuple2<String, Long>> list = respondent.parse("/leagues/10/events");
        assertEquals("leagues", list.get(0)._1);
        assertEquals(10L, (long) list.get(0)._2);
        assertEquals("events", list.get(1)._1);
        assertNull(list.get(1)._2);
    }

    @Test()
    void parseIncorrect() {
        assertNull(respondent.parse("leagues/10/events"));
        assertNull(respondent.parse("/leagues/events"));
        assertNull(respondent.parse("/leagues/102f/events"));
        assertNull(respondent.parse("/leaGues/10/events"));
    }

    @Test(dataProvider = "Request-Responce provider")
    public void testGetResponse(String request, HttpResponse responce) throws Exception {
        assertEquals(respondent.getResponse(request), responce);
        //System.out.println("Request is: " + request + "Expected responce is" + responce);
    }

    @DataProvider(name = "Request-Responce provider")
    public Object[][] parseLocaleData() {
        return new Object[][]{
                // Non-implemented (or unknown) method
                {"HEAD / HTTP/1.1\r\n", new HttpResponse(HttpCodes.NOT_IMPLEMENTED, null, null)},
                // Bad URL
//                {"GET /// HTTP/1.1\r\n", "HTTP/1.1 400 Bad Request\r\n\r\n"},
                // Another bad URL
                {"GET * HTTP/1.1\r\n", new HttpResponse(HttpCodes.BAD_REQUEST, null, null)},

                // GET method
                // startPage
                {"GET / HTTP/1.1\r\n", new HttpResponse(HttpCodes.OK, defaultHeaders, "{\"content\": \"start page\"}")},
                // leagues
                {"GET /leagues HTTP/1.1\r\nbody", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(leaguesList))},

                {"GET /leagues/1 HTTP/1.1\r\nbody", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(leaguesList.get(1)))},

                {"GET /leagues/1/events HTTP/1.1\r\nbody", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(eventsList))},

                // events
                {"GET /events HTTP/1.1\r\nbody", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(eventsList))},

                {"GET /events/3 HTTP/1.1\r\nbody", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(eventsList.get(3)))},

                {"GET /events/3/offers HTTP/1.1\r\nbody", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(offersList.subList(3, 4)))},
        };
    }
}