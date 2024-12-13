package no.krazyglitch.aoc2024.day8;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResonanceTest {

    final List<String> data = List.of("............",
            "........0...",
            ".....0......",
            ".......0....",
            "....0.......",
            "......A.....",
            "............",
            "............",
            "........A...",
            ".........A..",
            "............",
            "............");

    @Test
    void testPartOneExampleA() {
        assertEquals(14, Resonance.getAntiNodeCount(data));
    }

    @Test
    void testPartTwoExampleA() {
        assertEquals(34, Resonance.getAntiNodeCountWithInfiniteRange(data));
    }
}