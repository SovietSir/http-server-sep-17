package com.epam.dao;

import com.epam.model.*;
import com.epam.store.ConnectionPool;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static java.time.LocalDateTime.parse;
import static org.testng.Assert.*;

public class BetDAOImplTest {

    private BetDAO betDAO;
    private EventDAO eventDAO;
    private LeagueDAO leagueDAO;
    private OfferDAO offerDAO;
    private PersonDAO personDAO;

    private ArrayList<Bet> betList;
    private ArrayList<Event> eventList;
    private ArrayList<League> leaguesList;
    private ArrayList<Offer> offerList;
    private ArrayList<Person> personList;

    @BeforeClass
    void setup() throws SQLException {
        ConnectionPool.pool.dropDatabase();
        ConnectionPool.pool.initDatabase();

        betDAO = BetDAOImpl.BET_DAO;
        eventDAO = EventDAOImpl.EVENT_DAO;
        leagueDAO = LeagueDAOImpl.LEAGUE_DAO;
        offerDAO = OfferDAOImpl.OFFER_DAO;
        personDAO = PersonDAOImpl.PERSON_DAO;

        betList = new ArrayList<>();
        eventList = new ArrayList<>();
        leaguesList = new ArrayList<>();
        offerList = new ArrayList<>();
        personList = new ArrayList<>();


        leaguesList.add(new League(1, "RFPL"));
        leaguesList.add(new League(2, "APL"));
        leaguesList.add(new League(3, "PDL"));
        leaguesList.add(new League(4, "USPL"));

        eventList.add(new Event(1, 1,  parse("2007-12-03T15:15:30"), "Zenith", "Nadir", "0:2"));
        eventList.add(new Event(2, 2,  parse("2007-12-03T15:15:30"), "Andji", "Tom", "1:3"));
        eventList.add(new Event(3, 3,  parse("2007-12-03T15:15:30"), "Bavaria", "MU", "0:0"));
        eventList.add(new Event(4, 4,  parse("2007-12-03T15:15:30"), "Dinamo", "Sokol", "3:2"));

        offerList.add(new Offer(1,1,"111",111,true));
        offerList.add(new Offer(2,2,"222",222,false));
        offerList.add(new Offer(3,3,"333",333,true));

        leagueDAO.create(leaguesList.get(0));
        leagueDAO.create(leaguesList.get(1));
        leagueDAO.create(leaguesList.get(2));
        leagueDAO.create(leaguesList.get(3));

        eventDAO.create(eventList.get(0));
        eventDAO.create(eventList.get(1));
        eventDAO.create(eventList.get(2));

        offerDAO.create(offerList.get(0));
        offerDAO.create(offerList.get(1));
        offerDAO.create(offerList.get(2));

        personList.add(new Person(1,"user1",111,111));
        personList.add(new Person(2,"user2",222,222));
        personList.add(new Person(3,"user3",333,333));

        personDAO.create(personList.get(0));
        personDAO.create(personList.get(1));
        personDAO.create(personList.get(2));


        betList.add(new Bet(1,1,1,111,1));
        betList.add(new Bet(2,2,2,222,2));
        betList.add(new Bet(3,2,2,333,3));
        betList.add(new Bet(4,3,3,444,4));

        betDAO.create(betList.get(0));
        betDAO.create(betList.get(1));
        betDAO.create(betList.get(2));
    }

    @Test
    public void testReadBetsByOfferId() throws Exception {
        assertEquals(betDAO.readBetsByOfferId(1L).get(0),betList.get(0));
    }

    @Test
    public void testReadBetsByPersonId() throws Exception {
        assertEquals(betDAO.readBetsByPersonId(1L).get(0),betList.get(0));
    }

    @Test
    public void testCreate() throws Exception {
        betDAO.create(betList.get(3));
        assertEquals(betDAO.read(4L),betList.get(3));
    }

    @Test
    public void testReadById() throws Exception {
        assertEquals(betDAO.read(1L),betList.get(0));
    }

    @Test
    public void testUpdate() throws Exception {
        betList.set(1,new Bet(2,1,1,111,1));
        betDAO.update(2L,betList.get(1));
        assertEquals(betDAO.read(2L),betList.get(1));
    }

    @Test (expectedExceptions = { NoSuchElementException.class})
    public void testUpdateWithNoSuchElementException() throws Exception {
        betList.set(1,new Bet(2,1,1,111,1));
        betDAO.update(200L,betList.get(1));
    }

    @Test (expectedExceptions = { NoSuchElementException.class})
    public void testdeleteWithNoSuchElementException() throws Exception {
        betDAO.delete(4L);
        betList.remove(3);
        assertNull(betDAO.read(4L));
    }
}