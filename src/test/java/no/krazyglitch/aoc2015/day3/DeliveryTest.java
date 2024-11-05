package no.krazyglitch.aoc2015.day3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {

    @Test
    void testPartOneExampleA() {
        final String data = ">";
        assertEquals(2, Delivery.getGiftedHouses(data));
    }

    @Test
    void testPartOneExampleB() {
        final String data = "^>v<";
        assertEquals(4, Delivery.getGiftedHouses(data));
    }

    @Test
    void testPartOneExampleC() {
        final String data = "^v^v^v^v^v";
        assertEquals(2, Delivery.getGiftedHouses(data));
    }

    @Test
    void testPartTwoExampleA() {
        final String data = "^v";
        assertEquals(3, Delivery.getGiftedHousesWithRobot(data));
    }

    @Test
    void testPartTwoExampleB() {
        final String data = "^>v<";
        assertEquals(3, Delivery.getGiftedHousesWithRobot(data));
    }

    @Test
    void testPartTwoExampleC() {
        final String data = "^v^v^v^v^v";
        assertEquals(11, Delivery.getGiftedHousesWithRobot(data));
    }
}