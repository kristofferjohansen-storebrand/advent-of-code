package no.krazyglitch.aoc2025.day1;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SafeCrackerTest {
    final static List<String> TEST_DATA = Arrays.stream(("""
                    L68
                    L30
                    R48
                    L5
                    R60
                    L55
                    L1
                    L99
                    R14
                    L82""").split("\n"))
            .toList();

    @Test
    void testPartOne() {
        final int result = SafeCracker.getTimesEncountered(new SafeCracker.Safe(50), TEST_DATA, 0);
        assertEquals(3, result);
    }

    @Test
    void testPartTwo() {
        final int result = SafeCracker.getTimesEncounteredZero(new SafeCracker.Safe(50), TEST_DATA);
        assertEquals(6, result);
    }
}