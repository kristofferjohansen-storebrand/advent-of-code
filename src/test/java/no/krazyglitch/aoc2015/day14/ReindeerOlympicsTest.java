package no.krazyglitch.aoc2015.day14;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReindeerOlympicsTest {

    @Test
    void testPartOneExampleA() {
        final int result = ReindeerOlympics.calculateWinnerDistance(List.of("Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.",
                "Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds."), 1000);
        assertEquals(1120, result);
    }

    @Test
    void testPartTwoExampleA() {
        final int result = ReindeerOlympics.calculateWinnerPoints(List.of("Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.",
                "Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds."), 1000);
        assertEquals(689, result);
    }
}