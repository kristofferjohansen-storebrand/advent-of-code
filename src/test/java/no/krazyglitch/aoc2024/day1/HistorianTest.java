package no.krazyglitch.aoc2024.day1;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistorianTest {

    @Test
    void testPartOneExampleA() {
        final List<String> data = List.of("3   4",
        "4   3",
        "2   5",
        "1   3",
        "3   9",
        "3   3");

        assertEquals(11, Historian.getListDifference(data));
    }

    @Test
    void testPartOneExampleB() {
        final List<String> data = List.of("3   4",
                "4   3",
                "2   5",
                "1   3",
                "3   9",
                "3   3");

        assertEquals(31, Historian.getSimilarityScore(data));
    }
}