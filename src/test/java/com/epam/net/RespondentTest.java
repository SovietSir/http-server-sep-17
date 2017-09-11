package com.epam.net;

import com.epam.daoInterfaces.EventDAO;
import com.epam.daoInterfaces.LeagueDAO;
import com.epam.model.Event;
import com.epam.model.League;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vavr.Tuple2;
import org.testng.annotations.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

class RespondentTest {
    private Respondent respondent;
    private Gson gson;
    private ArrayList <League> leaguesList;
    private ArrayList <Event> eventsList;

    @BeforeClass
    void setup() {
        respondent = new Respondent();
        gson = new GsonBuilder().create();
        leaguesList = new ArrayList<>();
        eventsList = new ArrayList<>();


        leaguesList.add(new League(1,"RFPL"));
        leaguesList.add(new League(2,"APL"));
        leaguesList.add(new League(3,"PDL"));
        leaguesList.add(new League(4,"USPL"));

        eventsList.add(new Event(1, Instant.parse("2007-12-03T10:15:30.00Z"), "Zenith", "Nadir", new Tuple2((byte)2, (byte)2)));
        eventsList.add(new Event(2, Instant.parse("2007-12-03T10:15:30.00Z"), "Andji", "Tom", new Tuple2((byte)2, (byte)2)));
        eventsList.add(new Event(3, Instant.parse("2007-12-03T10:15:30.00Z"), "Bavaria", "MU", new Tuple2((byte)2, (byte)2)));
        eventsList.add(new Event(4, Instant.parse("2007-12-03T10:15:30.00Z"), "Dinamo", "Sokol", new Tuple2((byte)2, (byte)2)));

        respondent.setLeagueDAO(new LeagueDAO() {
            @Override
            public void create(League league) {}
            @Override
            public League readById(Long aLong) {return leaguesList.get(aLong.intValue());}
            @Override
            public void update(Long aLong, League league) {}
            @Override
            public void deleteById(Long aLong) {}
            @Override
            public List<League> readAll() {return leaguesList;}
        });

        respondent.setEventDAO(new EventDAO() {
            @Override
            public void create(Event event) {}
            @Override
            public Event readById(Long aLong) {return eventsList.get(aLong.intValue());}
            @Override
            public void update(Long aLong, Event event) {}
            @Override
            public void deleteById(Long aLong) {}
            @Override
            public List<Event> readAll() {return eventsList;}
            @Override
            public List<Event> readEventsByLeagueId(long leagueId) {return eventsList;}
        });


    }

    @Test
    void parseCorrect() {
        List<Tuple2<String, Long>> list = respondent.parse("/leagues/10/events");
        assertEquals("leagues", list.get(0)._1);
        assertEquals(10L, (long)list.get(0)._2);
        assertEquals("events", list.get(1)._1);
        assertNull(list.get(1)._2);
    }

    @Test
    void parseIncorrect() {
        assertNull(respondent.parse("leagues/10/events"));
        assertNull(respondent.parse("/leagues/events"));
        assertNull(respondent.parse("/leagues/102f/events"));
        assertNull(respondent.parse("/leaGues/10/events"));
    }

    @Test (dataProvider = "Request-Responce provider")
    public void testGetResponse(String request, String responce) throws Exception {
        assertEquals(respondent.getResponse(request), responce);
        //System.out.println("Request is: " + request + "Expected responce is" + responce);
    }

    @DataProvider (name = "Request-Responce provider")
    public Object[][] parseLocaleData() {
        return new Object[][]{
                // Non-implemented (or unknown) method
                {"HEAD / HTTP/1.1\r\n", "HTTP/1.1 501 Not Implemented\r\n\r\n"},
                // Bad URL
                {"GET /// HTTP/1.1\r\n", "HTTP/1.1 400 Bad Request\r\n\r\n"},
                // GET method
                {"GET / HTTP/1.1\r\n", "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n{\"content\": \"start page\"}"},

                {"GET /leagues HTTP/1.1\r\nbody", "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + gson.toJson(leaguesList)},

                {"GET /leagues/1 HTTP/1.1\r\nbody", "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + gson.toJson(leaguesList.get(1))},
        };
    }
}