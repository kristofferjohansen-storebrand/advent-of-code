package no.krazyglitch.aoc2025.day9;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TheatreTest {
    private static final List<String> DATA = Arrays.stream(("""
            7,1
            11,1
            11,7
            9,7
            9,5
            2,5
            2,3
            7,3""").split("\n"))
            .toList();

    @Test
    void testDayNinePartOne() {
        final long result = Theatre.getLargestRectangle(DATA);
        assertEquals(50, result);
    }

    @Test
    void testDayNinePartTwo() {
        final long result = Theatre.getLargestContainedRectangle(DATA);
        assertEquals(24, result);
    }
}