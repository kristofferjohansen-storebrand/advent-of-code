package no.krazyglitch.aoc2025.day4;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ForkliftTest {

    private static final List<String> DATA = Arrays.stream(("""
                    ..@@.@@@@.
                    @@@.@.@.@@
                    @@@@@.@.@@
                    @.@@@@..@.
                    @@.@@@@.@@
                    .@@@@@@@.@
                    .@.@.@.@@@
                    @.@@@.@@@@
                    .@@@@@@@@.
                    @.@.@@@.@.
                    """).split("\n"))
            .toList();

    @Test
    void testDayFourPartOne() {
        final int result = Forklift.getAccessibleRolls(DATA);
        assertEquals(13, result);
    }

    @Test
    void testDayFourPartTwo() {
        final int result = Forklift.getAccessibleRollsRecursive(DATA);
        assertEquals(43, result);
    }
}