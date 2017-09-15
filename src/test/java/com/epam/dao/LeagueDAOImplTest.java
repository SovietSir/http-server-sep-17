package com.epam.dao;
import com.epam.model.League;
import com.epam.store.ConnectionPool;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.testng.AssertJUnit.assertEquals;

@Test
public class LeagueDAOImplTest {

    private LeagueDAO leagueDAO;
    private ArrayList<League> leaguesList;

    @BeforeClass
    void setup() throws SQLException {
        ConnectionPool.pool.dropDatabase();
        ConnectionPool.pool.initDatabase();

        leagueDAO = LeagueDAOImpl.LEAGUE_DAO;
        leaguesList = new ArrayList<>();

        leaguesList.add(new League(1, "RFPL"));
        leaguesList.add(new League(2, "APL"));
        leaguesList.add(new League(3, "PDL"));
        leaguesList.add(new League(4, "USPL"));
        leagueDAO.create(new League(1,"RFPL"));
        leagueDAO.create(new League(2,"APL"));
        leagueDAO.create(new League(3,"PDL"));
        leagueDAO.create(new League(4,"USPL"));
    }

    @Test
    public void testReadAll() throws Exception {
       assertEquals(leagueDAO.readAll(),leaguesList);
    }

    @Test
    public void testReadById() throws Exception {
        assertEquals(leagueDAO.read(1L),leaguesList.get(0));
        assertEquals(leagueDAO.read(2L),leaguesList.get(1));
        assertEquals(leagueDAO.read(3L),leaguesList.get(2));
        assertEquals(leagueDAO.read(4L),leaguesList.get(3));
    }
    @Test(expectedExceptions = { NoSuchElementException.class})
    public void testReadWithNoSuchElementException() throws SQLException {
        leagueDAO.read(-1L);
    }

    @Test
    public void testCreate() throws Exception {
        leagueDAO.create(new League(5,"TEST LEAGUE"));
        leaguesList.add(new League(5,"TEST LEAGUE"));
        assertEquals(leagueDAO.read(5L),leaguesList.get(4));
    }

    @Test
    public void testUpdate() throws Exception {
        leagueDAO.update((long)1,new League(1,"NEW TEST LEAGUE"));
        leaguesList.set(0,new League(1,"NEW TEST LEAGUE"));
        assertEquals(leagueDAO.read(1L),leaguesList.get(0));
    }
    @Test(expectedExceptions = { NoSuchElementException.class})
    public void testUpdateByIdWithNoSuchElementException() throws SQLException {
        leagueDAO.update(-1L,leaguesList.get(0));
    }

    @Test
    public void testDeleteById() throws Exception {
        leagueDAO.deleteById(5L);
        leaguesList.remove(4);
        assertEquals(leagueDAO.readAll(),leaguesList);
    }
}