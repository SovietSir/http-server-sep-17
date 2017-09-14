package com.epam.dao;

import com.epam.model.Event;
import com.epam.store.ConnectionPool;
import org.testng.annotations.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static java.time.LocalDateTime.from;
import static org.testng.Assert.*;

public class EventDAOImplTest {

    private EventDAOImpl eventDAO;
    private ArrayList<Event> eventList;

    void setup(){
        eventDAO = new EventDAOImpl();
        eventList = new ArrayList<>();

        ConnectionPool.pool.dropDatabase();
        ConnectionPool.pool.initDatabase();

        eventList.add(new Event(1, 1, from(Instant.parse("2007-12-03T10:15:30.00Z")), "Zenith", "Nadir", "0:2"));
        eventList.add(new Event(2, 1, from(Instant.parse("2008-11-04T11:20:35.00Z")), "Andji", "Tom", "1:3"));
        eventList.add(new Event(3, 1, from(Instant.parse("2009-10-05T12:25:40.00Z")), "Bavaria", "MU", "0:0"));
        eventList.add(new Event(4, 1, from(Instant.parse("2010-09-06T13:30:45.00Z")), "Dinamo", "Sokol", "3:2"));


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
    }

    @Test
    public void testCreate() throws Exception {
    }

    @Test
    public void testReadById() throws Exception {
    }

    @Test
    public void testUpdate() throws Exception {
    }

    @Test
    public void testDeleteById() throws Exception {
    }

}