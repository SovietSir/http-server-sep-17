package com.epam.dao;

import com.epam.model.Event;
import com.epam.model.League;
import com.epam.model.Offer;
import com.epam.model.Person;
import com.epam.store.ConnectionPool;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static java.time.LocalDateTime.parse;

public class CriticalDBErrorTest {
    @BeforeClass
    void setup() {
        ConnectionPool.pool.dropDatabase();
    }

    @Test(expectedExceptions = {SQLException.class})
    public void testLeagueDAOCreateWithSQLException() throws Exception {
        LeagueDAOImpl.LEAGUE_DAO.create(new League(5, "\'TEST LEAGUE"));
    }

    @Test(expectedExceptions = {SQLException.class})
    public void testLeagueDAOUpdateWithSQLException() throws Exception {
        LeagueDAOImpl.LEAGUE_DAO.update(1L, new League(-5, "\'TEST LEAGUE"));
    }

    @Test(expectedExceptions = {SQLException.class})
    public void testLeagueDAODeleteWithSQLException() throws Exception {
        LeagueDAOImpl.LEAGUE_DAO.delete(1L);
    }

    @Test(expectedExceptions = {SQLException.class})
    public void testLeagueDAOReadAllWithSQLException() throws Exception {
        LeagueDAOImpl.LEAGUE_DAO.readAll();
    }

    @Test(expectedExceptions = {SQLException.class})
    public void testEventDAODeleteWithSQLException() throws Exception {
        EventDAOImpl.EVENT_DAO.delete(1L);
    }

    @Test(expectedExceptions = {SQLException.class})
    public void testEventDAOCreateWithSQLException() throws Exception {
        EventDAOImpl.EVENT_DAO.create(
                new Event(5,
                        2,
                        parse("2007-12-03T15:15:30"),
                        "Dinamo", "Sokol", "3:2"));
    }

    @Test(expectedExceptions = {SQLException.class})
    public void testEventDAOReadWithSQLException() throws Exception {
        EventDAOImpl.EVENT_DAO.read(1L);
    }

    @Test(expectedExceptions = {SQLException.class})
    public void testEventDAOReadAllWithSQLException() throws Exception {
        EventDAOImpl.EVENT_DAO.readAll();
    }

    @Test(expectedExceptions = {SQLException.class})
    public void testEventDAOReadSubLevelWithSQLException() throws Exception {
        EventDAOImpl.EVENT_DAO.readSubLevel(1L);
    }

    @Test(expectedExceptions = {SQLException.class})
    public void testEventDAOReadEventsByLeagueIdWithSQLException() throws Exception {
        EventDAOImpl.EVENT_DAO.readEventsByLeagueId(1L);
    }

    @Test(expectedExceptions = {BadRequestException.class})
    public void testOfferDAOReadAllWithBadRequestException() throws Exception {
        ConnectionPool.pool.dropDatabase();
        OfferDAOImpl.OFFER_DAO.readAll();
    }

    @Test(expectedExceptions = {SQLException.class})
    public void testOfferDAOReadWithSQLException() throws Exception {
        OfferDAOImpl.OFFER_DAO.read(1L);
    }

    @Test(expectedExceptions = {SQLException.class})
    public void testOfferDAOCreateWithSQLException() throws Exception {
        OfferDAOImpl.OFFER_DAO.create(new Offer(1, 1, "111", 111, true));
    }

    @Test(expectedExceptions = {SQLException.class})
    public void testOfferDAOUpdateWithSQLException() throws Exception {
        OfferDAOImpl.OFFER_DAO.update(1L, new Offer(1, 1, "111", 111, true));
    }

    @Test(expectedExceptions = {SQLException.class})
    public void testOfferDAODeleteWithSQLException() throws Exception {
        OfferDAOImpl.OFFER_DAO.delete(1L);
    }

    @Test(expectedExceptions = {SQLException.class})
    public void testPersonDAOReadWithSQLException() throws Exception {
        PersonDAOImpl.PERSON_DAO.read(1L);


    }

    @Test(expectedExceptions = {SQLException.class})
    public void testPersonDAOCreateWithSQLException() throws Exception {
        PersonDAOImpl.PERSON_DAO.create(new Person(1, "1", 1, 1));


    }

    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithSQLException14() throws Exception {
        PersonDAOImpl.PERSON_DAO.update(1L, new Person(1, "1", 1, 1));


    }

    @Test(expectedExceptions = {SQLException.class})
    public void testPersonDAOReadSubLevelWithSQLException() throws Exception {
        PersonDAOImpl.PERSON_DAO.readSubLevel(1L);
    }

    @Test(expectedExceptions = {SQLException.class})
    public void testPersonDAOReadAllWithSQLException() throws Exception {
        PersonDAOImpl.PERSON_DAO.readAll();


    }

    @Test(expectedExceptions = {SQLException.class})
    public void testPersonDAODeleteWithSQLException() throws Exception {
        PersonDAOImpl.PERSON_DAO.delete(1L);
    }
}
