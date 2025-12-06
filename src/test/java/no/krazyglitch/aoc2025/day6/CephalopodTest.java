package no.krazyglitch.aoc2025.day6;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CephalopodTest {
    private static final List<String> DATA = Arrays.stream(("""
                    123 328  51 64
                     45 64  387 23
                      6 98  215 314
                    *   +   *   +
                    """).split("\n"))
            .toList();

    @Test
    void testDaySixPartOne() {
        final long result = Cephalopod.solveCephalopodMath(DATA);
        assertEquals(4277556, result);
    }

    @Test
    void testDaySixPartTwo() {
        final long result = Cephalopod.solveCephalopodMathRightToLeft(DATA);
        assertEquals(3263827, result);
    }
}