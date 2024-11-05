package no.krazyglitch.aoc2015.day4;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdventCoinsTest {

    @Test
    void testPartOneExampleOne() {
        assertEquals(609043, AdventCoins.findLowestSalt("abcdef", 5));
    }

    @Test
    void testPartOneExampleTwo() {
        assertEquals(1048970, AdventCoins.findLowestSalt("pqrstuv", 5));
    }
}