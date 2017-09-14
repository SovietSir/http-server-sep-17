package com.epam.dao;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.internal.junit.ArrayAsserts.assertArrayEquals;
import static ru.yandex.qatools.embed.postgresql.distribution.Version.Main.V9_6;

import com.epam.model.League;
import com.epam.store.ConnectionPool;
//import org.hamcrest.core.Is;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
//import static org.hamcrest.MatcherAssert.assertThat;

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
//
//    @Test
//    public void testReadById() throws Exception {
////        assertEquals(leagueDAO.readById((long)1),leaguesList.get(0));
////        assertEquals(leagueDAO.readById((long)2),leaguesList.get(1));
////        assertEquals(leagueDAO.readById((long)3),leaguesList.get(2));
////        assertEquals(leagueDAO.readById((long)4),leaguesList.get(3));
//    }

}