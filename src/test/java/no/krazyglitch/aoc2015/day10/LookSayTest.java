package no.krazyglitch.aoc2015.day10;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LookSayTest {

    @Test
    void testPartOneExampleA() {
        assertEquals(2, LookSay.getLookSayLength("1", 1));
    }

    @Test
    void testPartOneExampleB() {
        assertEquals(2, LookSay.getLookSayLength("11", 1));
    }

    @Test
    void testPartOneExampleC() {
        assertEquals(4, LookSay.getLookSayLength("21", 1));
    }

    @Test
    void testPartOneExampleD() {
        assertEquals(6, LookSay.getLookSayLength("1211", 1));
    }

    @Test
    void testPartOneExampleE() {
        assertEquals(6, LookSay.getLookSayLength("111221", 1));
    }
}