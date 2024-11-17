package no.krazyglitch.aoc2015.day15;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MilkDunkingTest {

    @Test
    void testPartOneExampleA() {
        final List<String> data = List.of("Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8",
                "Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3");
        assertEquals(62842880, MilkDunking.calculateWinningCombination(data, 100));
    }

    @Test
    void testPartTwoExampleA() {
        final List<String> data = List.of("Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8",
                "Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3");
        assertEquals(57600000, MilkDunking.calculateWinningCombinationWithCalories(data, 100, 500));
    }
}