package no.krazyglitch.aoc2015.day14;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReindeerOlympicsTest {

    @Test
    void testPartOneExampleA() {
        final List<String> data = List.of("Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.",
                "Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.");
        assertEquals(1120, ReindeerOlympics.calculateWinnerDistance(data, 1000));
    }

    @Test
    void testPartTwoExampleA() {
        final List<String> data = List.of("Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.",
                "Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.");
        assertEquals(689, ReindeerOlympics.calculateWinnerPoints(data, 1000));
    }
}