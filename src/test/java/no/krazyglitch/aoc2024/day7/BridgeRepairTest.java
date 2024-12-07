package no.krazyglitch.aoc2024.day7;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BridgeRepairTest {

    private final List<String> data = List.of("190: 10 19",
            "3267: 81 40 27",
            "83: 17 5",
            "156: 15 6",
            "7290: 6 8 6 15",
            "161011: 16 10 13",
            "192: 17 8 14",
            "21037: 9 7 18 13",
            "292: 11 6 16 20");

    @Test
    void testPartOneExampleA() {
        assertEquals(3749, BridgeRepair.getPossibleEquations(data));
    }

    @Test
    void testPartTwoExampleA() {
        assertEquals(11387, BridgeRepair.getPossibleConcatEquations(data));
    }
}