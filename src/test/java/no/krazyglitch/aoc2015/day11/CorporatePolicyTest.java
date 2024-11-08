package no.krazyglitch.aoc2015.day11;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CorporatePolicyTest {

    @Test
    void testPartOneExampleA() {
        assertEquals("abcdffaa", CorporatePolicy.getNextValidPassword("abcdefgh"));
    }

    @Test
    void testPartOneExampleB() {
        assertEquals("ghjaabcc", CorporatePolicy.getNextValidPassword("ghijklmn"));
    }
}