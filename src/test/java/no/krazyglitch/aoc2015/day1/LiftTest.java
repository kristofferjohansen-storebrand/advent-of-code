package no.krazyglitch.aoc2015.day1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
class LiftTest {

    @Test
    void testPartOneA() {
        final String input = "(())";
        assertEquals(0, Lift.findFloor(input));
    }

    @Test
    void testPartOneB() {
        final String input = ")())())";
        assertEquals(-3, Lift.findFloor(input));
    }

    @Test
    void testPartTwoA() {
        final String input = ")";
        assertEquals(1, Lift.findBasement(input.toCharArray(), 0, 0, 0));
    }

    @Test
    void testPartTwoB() {
        final String input = "(()))";
        assertEquals(5, Lift.findBasement(input.toCharArray(), 0, 0, 0));
    }
}