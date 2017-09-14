package com.epam.dao;

import com.epam.model.Event;
import com.epam.model.League;
import com.epam.model.Offer;
import com.epam.store.ConnectionPool;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static java.time.LocalDateTime.parse;
import static org.testng.Assert.*;

public class OfferDAOImplTest {

    private EventDAOImpl eventDAO;
    private OfferDAOImpl offerDAO;
    private ArrayList<Offer> offerList;
    private ArrayList<Event> eventList;
    private LeagueDAOImpl leagueDAO;
    private ArrayList<League> leaguesList;

    @BeforeClass
    void setup(){
        offerDAO = new OfferDAOImpl();
        offerList = new ArrayList<>();
        eventList = new ArrayList<>();
        eventDAO = new EventDAOImpl();
        leagueDAO = new LeagueDAOImpl();
        leaguesList = new ArrayList<>();

        ConnectionPool.pool.dropDatabase();
        ConnectionPool.pool.initDatabase();

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
        assertEquals(offerDAO.readById(4L),offerList.get(3));
    }

    @Test
    public void testReadById() throws Exception {
        assertEquals(offerDAO.readById(1L),offerList.get(0));
    }

    @Test
    public void testUpdate() throws Exception {
        Offer newOffer = new Offer(1,3,"333",333,true);
        offerDAO.update(1L,newOffer);
        assertEquals(offerDAO.readById(1L),newOffer);
    }

    @Test
    public void testDeleteById() throws Exception {
        offerDAO.deleteById(2L);
        assertNull(offerDAO.readById(2L));
    }

}