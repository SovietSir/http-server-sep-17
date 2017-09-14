package com.epam.dao;

import com.epam.model.Person;
import com.epam.store.ConnectionPool;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.testng.Assert.assertEquals;

public class PersonDAOImplTest {

    private PersonDAOImpl personDAO;
    private ArrayList<Person> personList;

     @BeforeClass
     void setup(){
         personDAO = new PersonDAOImpl();
         personList = new ArrayList<>();

         personList.add(new Person(1,"user1",111,111));
         personList.add(new Person(2,"user2",222,222));
         personList.add(new Person(3,"user3",333,333));

         ConnectionPool.pool.dropDatabase();
         ConnectionPool.pool.initDatabase();

         personDAO.create(personList.get(0));
         personDAO.create(personList.get(1));
         personDAO.create(personList.get(2));
    }

    @Test
    public void testReadAll() throws Exception {
         assertEquals(personDAO.readAll(),personList);
    }

    @Test
    public void testCreate() throws Exception {
         personList.add(new Person(4,"user4",444,444));
         personDAO.create(personList.get(3));
         assertEquals(personDAO.readById(4L),personList.get(3));
    }

    @Test
    public void testReadById() throws Exception {
        assertEquals(personDAO.readById(2L),personList.get(1));
    }

    @Test
    public void testUpdate() throws Exception {
         personList.set(1,new Person(2,"User2_1",2221,2221));
         personDAO.update(2L,personList.get(1));
         assertEquals(personDAO.readById(2L),personList.get(1));
    }

    @Test
    public void testDeleteById() throws Exception {
         personDAO.deleteById(4L);
         personList.remove(3);
         assertEquals(personDAO.readAll(),personList);
    }
}