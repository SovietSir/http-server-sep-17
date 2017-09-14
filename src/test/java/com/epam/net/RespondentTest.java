package com.epam.net;

import com.epam.dao.*;
import com.epam.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vavr.Tuple2;
import org.testng.annotations.*;

import java.util.*;

import static java.time.LocalDateTime.parse;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

class RespondentTest {
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

    @BeforeClass
    void setup() {
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

        mockedLeagueDAO = mock(LeagueDAOImpl.class);
        mockedEventDAO = mock(EventDAOImpl.class);
        mockedOfferDAO = mock(OfferDAOImpl.class);
        mockedBetDAO = mock(BetDAOImpl.class);
        mockedPersonDAO = mock(PersonDAOImpl.class);

        //mock LeagueDAO
        when(mockedLeagueDAO.readAll()).thenReturn(leaguesList);
        when(mockedLeagueDAO.readById(0L)).thenReturn(leaguesList.get(0));
        when(mockedLeagueDAO.readById(1L)).thenReturn(leaguesList.get(1));
        when(mockedLeagueDAO.readById(2L)).thenReturn(leaguesList.get(2));
        when(mockedLeagueDAO.readById(3L)).thenReturn(leaguesList.get(3));
        doNothing().when(mockedLeagueDAO).create(any(League.class));
        doNothing().when(mockedLeagueDAO).update(anyLong(), any(League.class));
        doNothing().when(mockedLeagueDAO).deleteById(anyLong());

        //mock EventDAO
        when(mockedEventDAO.readAll()).thenReturn(eventsList);
        when(mockedEventDAO.readEventsByLeagueId(anyLong())).thenReturn(eventsList);
        when(mockedEventDAO.readById(0L)).thenReturn(eventsList.get(0));
        when(mockedEventDAO.readById(1L)).thenReturn(eventsList.get(1));
        when(mockedEventDAO.readById(2L)).thenReturn(eventsList.get(2));
        when(mockedEventDAO.readById(3L)).thenReturn(eventsList.get(3));
        doNothing().when(mockedEventDAO).create(any(Event.class));
        doNothing().when(mockedEventDAO).update(anyLong(), any(Event.class));
        doNothing().when(mockedEventDAO).deleteById(anyLong());

        //mock OfferDAO
        when(mockedOfferDAO.readOffersByEventId(anyLong())).thenReturn(offersList);
        when(mockedOfferDAO.readById(0L)).thenReturn(offersList.get(0));
        when(mockedOfferDAO.readById(1L)).thenReturn(offersList.get(1));
        when(mockedOfferDAO.readById(2L)).thenReturn(offersList.get(2));
        when(mockedOfferDAO.readById(3L)).thenReturn(offersList.get(3));
        doNothing().when(mockedOfferDAO).create(any(Offer.class));
        doNothing().when(mockedOfferDAO).update(anyLong(), any(Offer.class));
        doNothing().when(mockedOfferDAO).deleteById(anyLong());

        //mock BetDAO
        when(mockedBetDAO.readBetsByOfferId(anyLong())).thenReturn(betsList);
        when(mockedBetDAO.readBetsByPersonId(anyLong())).thenReturn(betsList);
        when(mockedBetDAO.readById(0L)).thenReturn(betsList.get(0));
        when(mockedBetDAO.readById(1L)).thenReturn(betsList.get(1));
        when(mockedBetDAO.readById(2L)).thenReturn(betsList.get(2));
        when(mockedBetDAO.readById(3L)).thenReturn(betsList.get(3));
        doNothing().when(mockedBetDAO).create(any(Bet.class));
        doNothing().when(mockedBetDAO).update(anyLong(), any(Bet.class));
        doNothing().when(mockedBetDAO).deleteById(anyLong());

        //mock PersonDAO
        when(mockedPersonDAO.readAll()).thenReturn(personsList);
        when(mockedPersonDAO.readById(0L)).thenReturn(personsList.get(0));
        when(mockedPersonDAO.readById(1L)).thenReturn(personsList.get(1));
        when(mockedPersonDAO.readById(2L)).thenReturn(personsList.get(2));
        when(mockedPersonDAO.readById(3L)).thenReturn(personsList.get(3));
        doNothing().when(mockedPersonDAO).create(any(Person.class));
        doNothing().when(mockedPersonDAO).update(anyLong(), any(Person.class));
        doNothing().when(mockedPersonDAO).deleteById(anyLong());

        respondent.setLeagueDAO(mockedLeagueDAO);
        respondent.setEventDAO(mockedEventDAO);
        respondent.setOfferDAO(mockedOfferDAO);
        respondent.setBetDAO(mockedBetDAO);
        respondent.setPersonDAO(mockedPersonDAO);

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

//                {"GET /leagues/6 HTTP/1.1\r\nbody", new HttpResponse(HttpCodes.BAD_REQUEST, null, null)},

                {"GET /leagues/1/events HTTP/1.1\r\nbody", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(eventsList))},

                // events
                {"GET /events HTTP/1.1\r\nbody", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(eventsList))},

                {"GET /events/3 HTTP/1.1\r\nbody", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(eventsList.get(3)))},

                {"GET /events/3/offers HTTP/1.1\r\nbody", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(offersList))},

                // offers
                // denied route
                {"GET /offers HTTP/1.1\r\nbody", new HttpResponse(HttpCodes.BAD_REQUEST, null, null)},

                {"GET /offers/3 HTTP/1.1\r\nbody", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(offersList.get(3)))},

                {"GET /offers/3/bets HTTP/1.1\r\nbody", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(betsList))},

                // bets
                // denied route
                {"GET /bets HTTP/1.1\r\nbody", new HttpResponse(HttpCodes.BAD_REQUEST, null, null)},

                {"GET /bets/3 HTTP/1.1\r\nbody", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(betsList.get(3)))},

                // denied route
                {"GET /bets/3/any HTTP/1.1\r\nbody", new HttpResponse(HttpCodes.BAD_REQUEST, null, null)},

                //persons
                {"GET /persons HTTP/1.1\r\nbody", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(personsList))},

                {"GET /persons/3 HTTP/1.1\r\nbody", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(personsList.get(3)))},

                {"GET /persons/3/bets HTTP/1.1\r\nbody", new HttpResponse(HttpCodes.OK, defaultHeaders, gson.toJson(betsList))},
        };
    }
}