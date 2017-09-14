package com.epam.dao;

import com.epam.model.Event;
import com.epam.model.League;
import com.epam.store.ConnectionPool;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static java.time.LocalDateTime.parse;
import static org.testng.Assert.assertEquals;

public class EventDAOImplTest {

    private EventDAOImpl eventDAO;
    private ArrayList<Event> eventList;
    private LeagueDAOImpl leagueDAO;
    private ArrayList<League> leaguesList;

    @BeforeClass
    void setup(){
        eventDAO = new EventDAOImpl();
        eventList = new ArrayList<>();

        leagueDAO = new LeagueDAOImpl();
        leaguesList = new ArrayList<>();

        ConnectionPool.pool.dropDatabase();
        ConnectionPool.pool.initDatabase();

        leaguesList.add(new League(1, "RFPL"));
        leaguesList.add(new League(2, "APL"));
        leaguesList.add(new League(3, "PDL"));
        leaguesList.add(new League(4, "USPL"));
        leaguesList.add(new League(5,"TEST LEAGUE"));

        leagueDAO.create(leaguesList.get(0));
        leagueDAO.create(leaguesList.get(1));
        leagueDAO.create(leaguesList.get(2));
        leagueDAO.create(leaguesList.get(3));
        leagueDAO.create(leaguesList.get(4));


        eventList.add(new Event(1, 1,  parse("2007-12-03T15:15:30"), "Zenith", "Nadir", "0:2"));
        eventList.add(new Event(2, 2,  parse("2007-12-03T15:15:30"), "Andji", "Tom", "1:3"));
        eventList.add(new Event(3, 3,  parse("2007-12-03T15:15:30"), "Bavaria", "MU", "0:0"));
        eventList.add(new Event(4, 4,  parse("2007-12-03T15:15:30"), "Dinamo", "Sokol", "3:2"));

        eventDAO.create(eventList.get(0));
        eventDAO.create(eventList.get(1));
        eventDAO.create(eventList.get(2));
        eventDAO.create(eventList.get(3));
    }

    @Test
    public void testReadAll() throws Exception {
        assertEquals(eventDAO.readAll(),eventList);
    }

    @Test
    public void testReadEventsByLeagueId() throws Exception {
        assertEquals(eventDAO.readEventsByLeagueId(1L).get(0),eventList.get(0));
    }

    @Test
    public void testCreate() throws Exception {
        eventList.add(new Event(5, 2,  parse("2007-12-03T15:15:30"), "Dinamo", "Sokol", "3:2"));
        eventDAO.create(new Event(5, 2,  parse("2007-12-03T15:15:30"), "Dinamo", "Sokol", "3:2"));
        assertEquals(eventDAO.readById(5L), eventList.get(4));
    }


    @Test
    public void testUpdate() throws Exception {
        eventList.set(0,new Event(1, 1, parse("2007-12-03T15:15:30"), "Dinamo111", "Sokol111", "3:2"));
        eventDAO.update(1L,eventList.get(0));
        assertEquals(eventDAO.readById(1L),eventList.get(0));
    }

    @Test
    public void testDeleteById() throws Exception {
        eventDAO.deleteById(4L);
        eventList.remove(3);
        assertEquals(eventDAO.readAll(),eventList);
    }

}