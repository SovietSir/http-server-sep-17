package com.epam.dao;

import com.epam.model.Event;
import com.epam.model.League;
import com.epam.model.Offer;
import com.epam.store.ConnectionPool;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static java.time.LocalDateTime.parse;
import static org.testng.Assert.*;

public class OfferDAOImplTest {

    private EventDAO eventDAO;
    private LeagueDAO leagueDAO;
    private OfferDAO offerDAO;

    private ArrayList<Event> eventList;
    private ArrayList<League> leaguesList;
    private ArrayList<Offer> offerList;

    @BeforeClass
    void setup() throws SQLException {
        ConnectionPool.pool.dropDatabase();
        ConnectionPool.pool.initDatabase();

        eventDAO = EventDAOImpl.EVENT_DAO;
        leagueDAO = LeagueDAOImpl.LEAGUE_DAO;
        offerDAO = OfferDAOImpl.OFFER_DAO;

        eventList = new ArrayList<>();
        leaguesList = new ArrayList<>();
        offerList = new ArrayList<>();

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
    }

    @Test
    public void testReadOffersByEventId() throws Exception {
        assertEquals(offerDAO.readOffersByEventId(1L).get(0),offerList.get(0));
    }

    @Test
    public void testCreate() throws Exception {
        offerList.add(new Offer(4,3,"444",444,true));
        offerDAO.create(offerList.get(3));
        assertEquals(offerDAO.read(4L),offerList.get(3));
    }

    @Test
    public void testReadById() throws Exception {
        assertEquals(offerDAO.read(1L),offerList.get(0));
    }

    @Test
    public void testUpdate() throws Exception {
        Offer newOffer = new Offer(1,3,"333",333,true);
        offerDAO.update(1L,newOffer);
        assertEquals(offerDAO.read(1L),newOffer);
    }

    @Test (expectedExceptions = { NoSuchElementException.class})
    public void testDeleteByIdWithNoSuchElementException() throws Exception {
        offerDAO.deleteById(2L);
        assertNull(offerDAO.read(2L));
    }
}