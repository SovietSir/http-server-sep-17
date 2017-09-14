package com.epam.dao;
import com.epam.model.League;
import com.epam.store.ConnectionPool;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.testng.AssertJUnit.assertEquals;

public class LeagueDAOImplTest {

    private LeagueDAOImpl leagueDAO;
    private ArrayList<League> leaguesList;

    @BeforeClass
    void setup(){
        leagueDAO = new LeagueDAOImpl();
        leaguesList = new ArrayList<>();

        leaguesList.add(new League(1, "RFPL"));
        leaguesList.add(new League(2, "APL"));
        leaguesList.add(new League(3, "PDL"));
        leaguesList.add(new League(4, "USPL"));
        ConnectionPool.pool.dropDatabase();
        ConnectionPool.pool.initDatabase();
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
        assertEquals(leagueDAO.readById((long)1),leaguesList.get(0));
        assertEquals(leagueDAO.readById((long)2),leaguesList.get(1));
        assertEquals(leagueDAO.readById((long)3),leaguesList.get(2));
        assertEquals(leagueDAO.readById((long)4),leaguesList.get(3));
    }

    @Test
    public void testCreate() throws Exception {
        leagueDAO.create(new League(5,"TEST LEAGUE"));
        leaguesList.add(new League(5,"TEST LEAGUE"));
        assertEquals(leagueDAO.readById((long)5),leaguesList.get(4));
    }

    @Test
    public void testUpdate() throws Exception {
        leagueDAO.update((long)1,new League(1,"NEW TEST LEAGUE"));
        leaguesList.set(0,new League(1,"NEW TEST LEAGUE"));
        assertEquals(leagueDAO.readById((long)1),leaguesList.get(0));
    }

    @Test
    public void testDeleteById() throws Exception {
        leagueDAO.deleteById((long)5);
        leaguesList.remove(4);
        assertEquals(leagueDAO.readAll(),leaguesList);
    }
}