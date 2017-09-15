package com.epam.model;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class OfferTest {
    @Test
    public void testHashCode() throws Exception {
        Offer offer1 = new Offer(1, 1, "1", 1, true);
        Offer offer2 = new Offer(1, 1, "1", 1, true);
        assertEquals(offer1.hashCode(), offer2.hashCode());
    }

}