package no.krazyglitch.aoc2025.day3;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PowerBankTest {

    private static final List<String> DATA = Arrays.stream(("""
                    987654321111111
                    811111111111119
                    234234234234278
                    818181911112111""").split("\n"))
            .toList();

    @Test
    void testDayThreePartOne() {
        final long result = PowerBank.findMaximumJoltageSum(DATA);
        assertEquals(357L, result);
    }

    @Test
    void testDayThreePartTwo() {
        final long result = PowerBank.findLargeMaximumJoltageSum(DATA);
        assertEquals(3121910778619L, result);
    }
}