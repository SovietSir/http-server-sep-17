package com.epam.model;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class BetTest {
    @Test
    public void testHashCode() throws Exception {
        Bet bet1 = new Bet(1,1,1,1,1);
        Bet bet2 = new Bet(1,1,1,1,1);
        assertEquals(bet1.hashCode(),bet2.hashCode());
    }

}