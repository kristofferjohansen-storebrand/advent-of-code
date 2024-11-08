package no.krazyglitch.aoc2015.day9;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TravelsTest {

    @Test
    void testPartOneExampleA() {
        final List<String> data = List.of("London to Dublin = 464",
                "London to Belfast = 518",
                "Dublin to Belfast = 141");
        assertEquals(605, Travels.getShortestDistance(data));
    }

    @Test
    void testPartTwoExampleA() {
        final List<String> data = List.of("London to Dublin = 464",
                "London to Belfast = 518",
                "Dublin to Belfast = 141");
        assertEquals(982, Travels.getLongestDistance(data));
    }
}