package no.krazyglitch.aoc2025.day5;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KitchenTest {
    private static final List<String> DATA = Arrays.stream(("""
            3-5
            10-14
            16-20
            12-18
            
            1
            5
            8
            11
            17
            32
            """).split("\n"))
            .toList();

    @Test
    void testDayFivePartOne() {
        final int result = Kitchen.getFreshIngredients(DATA);
        assertEquals(3, result);
    }

    @Test
    void testDayFivePartTwo() {
        final long result = Kitchen.getAmountOfFreshIngredients(DATA);
        assertEquals(14, result);
    }
}