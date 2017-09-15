package com.epam.dao;

import com.epam.model.*;
import com.epam.store.ConnectionPool;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static java.time.LocalDateTime.parse;

public class Critical {
    @BeforeClass
    void setup(){
        ConnectionPool.pool.dropDatabase();
    }

    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB() throws Exception {
        EventDAOImpl.EVENT_DAO.delete(1L);
    }

    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB1() throws Exception {
        LeagueDAOImpl.LEAGUE_DAO.create(new League(5,"\'TEST LEAGUE"));
    }
    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB2() throws Exception {
       LeagueDAOImpl.LEAGUE_DAO.update(1L,new League(-5,"\'TEST LEAGUE"));
    }
    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB3() throws Exception {
       LeagueDAOImpl.LEAGUE_DAO.delete(1L);
    }

    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB31() throws Exception {
        LeagueDAOImpl.LEAGUE_DAO.readAll();
    }


    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB4() throws Exception {
        EventDAOImpl.EVENT_DAO.create(
                new Event(5,
                        2,
                        parse("2007-12-03T15:15:30"),
                        "Dinamo", "Sokol", "3:2"));
    }
    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB5() throws Exception {
        EventDAOImpl.EVENT_DAO.read(1L);
    }
    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB6() throws Exception {
        EventDAOImpl.EVENT_DAO.readAll();
    }
    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB7() throws Exception {
        EventDAOImpl.EVENT_DAO.readSubLevel(1L);
    }
    @Test(expectedExceptions = {BadRequestException.class})
    public void testCreateWithExceptionDB8() throws Exception {
        ConnectionPool.pool.dropDatabase();
        OfferDAOImpl.OFFER_DAO.readAll();
    }
    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB9() throws Exception {
        OfferDAOImpl.OFFER_DAO.read(1L);
    }
    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB10() throws Exception {
        OfferDAOImpl.OFFER_DAO.create(new Offer(1,1,"111",111,true));
    }
    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB11() throws Exception {
        OfferDAOImpl.OFFER_DAO.update(1L,new Offer(1,1,"111",111,true));
    }

    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB12() throws Exception {
    PersonDAOImpl.PERSON_DAO.read(1L);


    }

    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB13() throws Exception {
    PersonDAOImpl.PERSON_DAO.create(new Person(1,"1",1,1));


    }
    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB14() throws Exception {
        PersonDAOImpl.PERSON_DAO.update(1L,new Person(1,"1",1,1));


    }
    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB15() throws Exception {
        PersonDAOImpl.PERSON_DAO.readSubLevel(1L);


    }
    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB16() throws Exception {
        PersonDAOImpl.PERSON_DAO.readAll();


    }
    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB17() throws Exception {
        PersonDAOImpl.PERSON_DAO.delete(1L);
    }
    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB18() throws Exception {
        BetDAOImpl.BET_DAO.delete(1L);
    }
    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB19() throws Exception {
        BetDAOImpl.BET_DAO.create(new Bet(1,1,1,1,1));
    }
    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB20() throws Exception {
        BetDAOImpl.BET_DAO.readBetsByOfferId(1L);
    }
    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB21() throws Exception {
        EventDAOImpl.EVENT_DAO.readEventsByLeagueId(1L);
    }
    @Test(expectedExceptions = {SQLException.class})
    public void testCreateWithExceptionDB22() throws Exception {
        OfferDAOImpl.OFFER_DAO.delete(1L);
    }
}
