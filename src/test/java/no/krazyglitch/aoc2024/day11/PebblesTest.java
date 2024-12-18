package no.krazyglitch.aoc2024.day11;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PebblesTest {

    @Test
    void testPartOneExampleA() {
        final String data = "125 17";
        assertEquals(22, Pebbles.getStonesAfterRecursiveBlinks(data, 6));
    }

    @Test
    void testPartOneExampleB() {
        final String data = "125 17";
        assertEquals(55312, Pebbles.getStonesAfterRecursiveBlinks(data, 25));
    }
}