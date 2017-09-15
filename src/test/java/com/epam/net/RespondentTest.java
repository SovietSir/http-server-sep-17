package com.epam.net;

import com.epam.dao.*;
import com.epam.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vavr.Tuple2;
import org.testng.annotations.*;

import java.sql.SQLException;
import java.util.*;

import static com.epam.dao.BetDAOImpl.*;
import static com.epam.dao.EventDAOImpl.*;
import static com.epam.dao.LeagueDAOImpl.*;
import static com.epam.dao.OfferDAOImpl.*;
import static com.epam.dao.PersonDAOImpl.*;
import static java.time.LocalDateTime.parse;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import static org.testng.Assert.*;

public class RespondentTest {
    private Respondent respondent;
    private Gson gson;
    private ArrayList<League> leaguesList;
    private ArrayList<Event> eventsList;
    private ArrayList<Offer> offersList;
    private ArrayList<Bet> betsList;
    private ArrayList<Person> personsList;
    private LeagueDAOImpl mockedLeagueDAO;
    private EventDAOImpl mockedEventDAO;
    private BetDAOImpl mockedBetDAO;
    private OfferDAOImpl mockedOfferDAO;
    private PersonDAOImpl mockedPersonDAO;
    private Map<String, String> defaultHeaders;
    private Map<String, String> defaultBodies;
    private Map<String, String> badBodies;

    @BeforeClass
    void setup() throws SQLException {
        respondent = new Respondent();
        gson = new GsonBuilder()
                .setDateFormat("dd-MM-yyyy HH:mm")
                .create();
        leaguesList = new ArrayList<>();
        eventsList = new ArrayList<>();
        offersList = new ArrayList<>();
        betsList = new ArrayList<>();
        personsList = new ArrayList<>();
        defaultHeaders = new HashMap<String, String>();
        defaultHeaders.put("Content-Type", "application/json");

        defaultBodies = new HashMap<String, String>();
        defaultBodies.put("League", "{\"id\":1, \"name\":\"RFPL\"}");
        defaultBodies.put("Event", "{\"id\":1, \"eventId\":1, \"date\":\"2007-12-03T15:15:30\", \"homeTeam\":\"Zenith\", \"guestTeam\":\"Zenith\", \"score\":\"0:1\"}");
        defaultBodies.put("Offer", "{\"id\":1, \"eventId\":1, \"description\":\"Some descr.\", \"coefficient\":0.75f, \"result\":\"true\"}");
        defaultBodies.put("Bet", "{\"id\":1, \"personId\":1, \"eventId\":1, \"amount\":150L, \"gain\":88.14f}");
        defaultBodies.put("Person", "{\"id\":1, \"login\":\"admin\", \"passwordHash\":39210433, \"balance\":11150L");

        badBodies = new HashMap<String, String>();
        badBodies.put("League", "{\"id\":1, \"name\":\"RFPL\"}");
        badBodies.put("Event", "{\"id\":1, \"eventId\":1, \"date\":\"2007-12-03T15:15:30\", \"homeTeam\":\"Zenith\", \"guestTeam\":\"Zenith\", \"score\":\"0:1\"}");
        badBodies.put("Offer", "{\"id\":1, \"eventId\":1, \"description\":\"Some descr.\", \"coefficient\":0.75f, \"result\":\"true\"}");
        badBodies.put("Bet", "{\"id\":1, \"personId\":1, \"eventId\":1, \"amount\":150L, \"gain\":88.14f}");
        badBodies.put("Person", "{\"id\":1, \"login\":\"admin\", \"passwordHash\":39210433, \"balance\":11150L");

        leaguesList.add(new League(1, "RFPL"));
        leaguesList.add(new League(2, "APL"));
        leaguesList.add(new League(3, "PDL"));
        leaguesList.add(new League(4, "USPL"));

        eventsList.add(new Event(1, 1, parse("2007-12-03T15:15:30"), "Zenith", "Nadir", "0:2"));
        eventsList.add(new Event(2, 1, parse("2008-11-04T11:20:35"), "Andji", "Tom", "1:3"));
        eventsList.add(new Event(3, 1, parse("2009-10-05T12:25:40"), "Bavaria", "MU", "0:0"));
        eventsList.add(new Event(4, 1, parse("2010-09-06T13:30:45"), "Dinamo", "Sokol", "3:2"));

        offersList.add(new Offer(1, 1, "Zenith will win Nadir", 0.95f, true));
        offersList.add(new Offer(2, 2, "Andji will lose Tom", 0.65f, true));
        offersList.add(new Offer(3, 3, "Bavaria had to destroy MU", 0.999f, true));
        offersList.add(new Offer(4, 4, "Dinamo will split with Sokol", 0.5f, true));

        betsList.add(new Bet(1, 5,1, 900L, 455.0f));
        betsList.add(new Bet(2, 6,2, 2100L, 55.15f));
        betsList.add(new Bet(3, 7,3, 130L, 135.8f));
        betsList.add(new Bet(4, 8,4, 19000L, 2214.2f));

        personsList.add(new Person(1, "user1",34212254, 4500));
        personsList.add(new Person(2, "user2",13782921, 4700));
        personsList.add(new Person(3, "user3",56793294, 8900));
        personsList.add(new Person(4, "user4",39210433, 135000));

        LEAGUE_DAO = mock(LeagueDAOImpl.class);
        EVENT_DAO = mock(EventDAOImpl.class);
        OFFER_DAO = mock(OfferDAOImpl.class);
        BET_DAO = mock(BetDAOImpl.class);
        PERSON_DAO = mock(PersonDAOImpl.class);

        //mock LeagueDAO
        when(LEAGUE_DAO.readAll()).thenReturn(leaguesList);
        when(LEAGUE_DAO.read(0L)).thenReturn(leaguesList.get(0));
        when(LEAGUE_DAO.read(1L)).thenReturn(leaguesList.get(1));
        when(LEAGUE_DAO.read(2L)).thenReturn(leaguesList.get(2));
        when(LEAGUE_DAO.read(3L)).thenReturn(leaguesList.get(3));
        doNothing().when(LEAGUE_DAO).create(any(League.class));
        doNothing().when(LEAGUE_DAO).update(anyLong(), any(League.class));
        doNothing().when(LEAGUE_DAO).delete(anyLong());

        //mock EventDAO
        when(EVENT_DAO.readAll()).thenReturn(eventsList);
        when(EVENT_DAO.readEventsByLeagueId(anyLong())).thenReturn(eventsList);
        when(EVENT_DAO.read(0L)).thenReturn(eventsList.get(0));
        when(EVENT_DAO.read(1L)).thenReturn(eventsList.get(1));
        when(EVENT_DAO.read(2L)).thenReturn(eventsList.get(2));
        when(EVENT_DAO.read(3L)).thenReturn(eventsList.get(3));
        doNothing().when(EVENT_DAO).create(any(Event.class));
        doNothing().when(EVENT_DAO).update(anyLong(), any(Event.class));
        doNothing().when(EVENT_DAO).delete(anyLong());

        //mock OfferDAO
        when(OFFER_DAO.readOffersByEventId(anyLong())).thenReturn(offersList);
        when(OFFER_DAO.read(0L)).thenReturn(offersList.get(0));
        when(OFFER_DAO.read(1L)).thenReturn(offersList.get(1));
        when(OFFER_DAO.read(2L)).thenReturn(offersList.get(2));
        when(OFFER_DAO.read(3L)).thenReturn(offersList.get(3));
        doNothing().when(OFFER_DAO).create(any(Offer.class));
        doNothing().when(OFFER_DAO).update(anyLong(), any(Offer.class));
        doNothing().when(OFFER_DAO).delete(anyLong());

        //mock BetDAO
        when(BET_DAO.readBetsByOfferId(anyLong())).thenReturn(betsList);
        when(BET_DAO.readBetsByPersonId(anyLong())).thenReturn(betsList);
        when(BET_DAO.read(0L)).thenReturn(betsList.get(0));
        when(BET_DAO.read(1L)).thenReturn(betsList.get(1));
        when(BET_DAO.read(2L)).thenReturn(betsList.get(2));
        when(BET_DAO.read(3L)).thenReturn(betsList.get(3));
        doNothing().when(BET_DAO).create(any(Bet.class));
        doNothing().when(BET_DAO).update(anyLong(), any(Bet.class));
        doNothing().when(BET_DAO).delete(anyLong());

        //mock PersonDAO
        when(PERSON_DAO.readAll()).thenReturn(personsList);
        when(PERSON_DAO.read(0L)).thenReturn(personsList.get(0));
        when(PERSON_DAO.read(1L)).thenReturn(personsList.get(1));
        when(PERSON_DAO.read(2L)).thenReturn(personsList.get(2));
        when(PERSON_DAO.read(3L)).thenReturn(personsList.get(3));
        doNothing().when(PERSON_DAO).create(any(Person.class));
        doNothing().when(PERSON_DAO).update(anyLong(), any(Person.class));
        doNothing().when(PERSON_DAO).delete(anyLong());

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

    @Test(dataProvider = "Good Get Requests", enabled = true)
    public void testGoodGetRequests(String request, HttpResponse responce) throws Exception {
        assertEquals(respondent.getResponse(request), responce);
    }

    @Test(dataProvider = "Bad Get Requests", enabled = true)
    public void testBadGetRequests(String request, HttpResponse responce) throws Exception {
        assertEquals(respondent.getResponse(request), responce);
    }

    @Test(dataProvider = "Good Put Requests", enabled = true)
    public void testGoodPutRequests(String request, HttpResponse responce) throws Exception {
        assertEquals(respondent.getResponse(request), responce);
    }

    @Test(dataProvider = "Bad Put Requests", enabled = true)
    public void testBadPutRequests(String request, HttpResponse responce) throws Exception {
        assertEquals(respondent.getResponse(request), responce);
    }

    @Test(dataProvider = "Good Post Requests", enabled = true)
    public void testGoodPostRequests(String request, HttpResponse responce) throws Exception {
        assertEquals(respondent.getResponse(request), responce);
    }

    @Test(dataProvider = "Bad Post Requests", enabled = true)
    public void testBadPostRequests(String request, HttpResponse responce) throws Exception {
        assertEquals(respondent.getResponse(request), responce);
    }

    @Test(dataProvider = "Good Delete Requests", enabled = true)
    public void testGoodDeleteRequests(String request, HttpResponse responce) throws Exception {
        assertEquals(respondent.getResponse(request), responce);
    }

    @Test(dataProvider = "Bad Delete Requests", enabled = true)
    public void testBadDeleteRequests(String request, HttpResponse responce) throws Exception {
        assertEquals(respondent.getResponse(request), responce);
    }

    @DataProvider(name = "Good Get Requests")
    public Object[][] goodGetRequests() {
        return new Object[][]{
                // startPage
                {"GET / HTTP/1.1\r\n", new HttpResponse(HttpCodes.OK, defaultHeaders, "{\"content\": \"start page\"}")},
                // leagues
                {"GET /leagues HTTP/1.1", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(leaguesList))},
                {"GET /leagues/1 HTTP/1.1", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(leaguesList.get(1)))},
                {"GET /leagues/1/events HTTP/1.1", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(eventsList))},
                // events
                {"GET /events HTTP/1.1", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(eventsList))},
                {"GET /events/3 HTTP/1.1", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(eventsList.get(3)))},
                {"GET /events/3/offers HTTP/1.1", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(offersList))},
                // offers
                {"GET /offers/3 HTTP/1.1", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(offersList.get(3)))},
                {"GET /offers/3/bets HTTP/1.1", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(betsList))},
                // bets
                {"GET /bets/3 HTTP/1.1", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(betsList.get(3)))},
                //persons
                {"GET /persons HTTP/1.1", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(personsList))},
                {"GET /persons/3 HTTP/1.1", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(personsList.get(3)))},
                {"GET /persons/3/bets HTTP/1.1", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(betsList))},
        };
    }

    @DataProvider(name = "Bad Get Requests")
    public Object[][] badGetRequests() {
        return new Object[][]{
                // Bad request
                {"someRubbish", new HttpResponse(HttpCodes.BAD_REQUEST)},
                // Bad URL
//                {"GET /// HTTP/1.1", "HTTP/1.1 400 Bad Request\r\n\r\n"},
                {"GET * HTTP/1.1", new HttpResponse(HttpCodes.BAD_REQUEST)},
                // Non-implemented (or unknown) method
                {"HEAD / HTTP/1.1", new HttpResponse(HttpCodes.NOT_IMPLEMENTED)},
                // denied routes
                {"GET /offers HTTP/1.1", new HttpResponse(HttpCodes.BAD_REQUEST)},
                {"GET /bets HTTP/1.1", new HttpResponse(HttpCodes.BAD_REQUEST)},
                {"GET /bets/3/any HTTP/1.1", new HttpResponse(HttpCodes.BAD_REQUEST)},
                // non-existent instances
//                {"GET /leagues/6 HTTP/1.1", new HttpResponse(HttpCodes.BAD_REQUEST, null, null)},
        };

    }

    @DataProvider(name = "Good Put Requests")
    public Object[][] goodPutRequests() {
        return new Object[][]{
                {String.format("PUT /leagues HTTP/1.1\r\n\r\n%s", defaultBodies.get("League")), new HttpResponse(HttpCodes.OK)},
                {String.format("PUT /events HTTP/1.1\r\n\r\n%s", defaultBodies.get("Event")), new HttpResponse(HttpCodes.OK)},
                {String.format("PUT /offers HTTP/1.1\r\n\r\n%s", defaultBodies.get("Offer")), new HttpResponse(HttpCodes.OK)},
                {String.format("PUT /bets HTTP/1.1\r\n\r\n%s", defaultBodies.get("Bet")), new HttpResponse(HttpCodes.OK)},
                {String.format("PUT /persons HTTP/1.1\r\n\r\n%s", defaultBodies.get("Person")), new HttpResponse(HttpCodes.OK)},
        };
    }

    @DataProvider(name = "Bad Put Requests")
    public Object[][] badPutRequests() {
        return new Object[][]{
                //non-existent route
                //{String.format("PUT / HTTP/1.1\r\n\r\n%s", defaultBodies.get("League")), new HttpResponse(HttpCodes.BAD_REQUEST)},
                {String.format("PUT /bad HTTP/1.1\r\n\r\n%s", defaultBodies.get("League")), new HttpResponse(HttpCodes.BAD_REQUEST)},
                {String.format("PUT /leagues/events HTTP/1.1\r\n\r\n%s", defaultBodies.get("League")), new HttpResponse(HttpCodes.BAD_REQUEST)},
                //existent, but denied routes
                {String.format("PUT /leagues/3 HTTP/1.1\r\n\r\n%s", defaultBodies.get("League")), new HttpResponse(HttpCodes.BAD_REQUEST)},
                {String.format("PUT /leagues/3/events HTTP/1.1\r\n\r\n%s", defaultBodies.get("League")), new HttpResponse(HttpCodes.BAD_REQUEST)},
                //bad JSONs
                {"PUT /leagues/3 HTTP/1.1\r\n\r\nsomeRubbish", new HttpResponse(HttpCodes.BAD_REQUEST)},
                {String.format("PUT /leagues/3/events HTTP/1.1\r\n\r\n%s", defaultBodies.get("League")), new HttpResponse(HttpCodes.BAD_REQUEST)},
        };
    }

    @DataProvider(name = "Good Post Requests")
    public Object[][] goodPostRequests() {
        return new Object[][]{
                {String.format("POST /leagues/1 HTTP/1.1\r\n\r\n%s", defaultBodies.get("League")), new HttpResponse(HttpCodes.OK)},
                {String.format("POST /events/2 HTTP/1.1\r\n\r\n%s", defaultBodies.get("Event")), new HttpResponse(HttpCodes.OK)},
                {String.format("POST /offers/2 HTTP/1.1\r\n\r\n%s", defaultBodies.get("Offer")), new HttpResponse(HttpCodes.OK)},
                {String.format("POST /bets/4 HTTP/1.1\r\n\r\n%s", defaultBodies.get("Bet")), new HttpResponse(HttpCodes.OK)},
                {String.format("POST /persons/6 HTTP/1.1\r\n\r\n%s", defaultBodies.get("Person")), new HttpResponse(HttpCodes.OK)},
        };
    }

    @DataProvider(name = "Bad Post Requests")
    public Object[][] badPostRequests() {
        return new Object[][]{
                //non-existent route
                //{String.format("PUT / HTTP/1.1\r\n\r\n%s", defaultBodies.get("League")), new HttpResponse(HttpCodes.BAD_REQUEST)},
                {String.format("POST /bad HTTP/1.1\r\n\r\n%s", defaultBodies.get("League")), new HttpResponse(HttpCodes.BAD_REQUEST)},
                {String.format("POST /leagues/events HTTP/1.1\r\n\r\n%s", defaultBodies.get("League")), new HttpResponse(HttpCodes.BAD_REQUEST)},
                //existent, but denied routes
                {String.format("POST /leagues HTTP/1.1\r\n\r\n%s", defaultBodies.get("League")), new HttpResponse(HttpCodes.BAD_REQUEST)},
                {String.format("POST /leagues/3/events HTTP/1.1\r\n\r\n%s", defaultBodies.get("League")), new HttpResponse(HttpCodes.BAD_REQUEST)},

        };
    }

    @DataProvider(name = "Good Delete Requests")
    public Object[][] goodDeleteRequests() {
        return new Object[][]{

        };
    }

    @DataProvider(name = "Bad Delete Requests")
    public Object[][] badDeleteRequests() {
        return new Object[][]{

        };
    }
}