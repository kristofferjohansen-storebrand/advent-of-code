package no.krazyglitch.aoc2025.day8;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JunctionBoxesTest {

    private static final List<String> DATA = Arrays.stream(("""
            162,817,812
            57,618,57
            906,360,560
            592,479,940
            352,342,300
            466,668,158
            542,29,236
            431,825,988
            739,650,466
            52,470,668
            216,146,977
            819,987,18
            117,168,530
            805,96,715
            346,949,466
            970,615,88
            941,993,340
            862,61,35
            984,92,344
            425,690,689
            """).split("\n"))
            .toList();

    @Test
    void testDayEightPartOne() {
        final long result = JunctionBoxes.calculateCircuitsNewer(DATA, 10);
        assertEquals(40L, result);
    }

    @Test
    void testDayEightPartTwo() {
        final long result = JunctionBoxes.frazzled(DATA);
        assertEquals(25272, result);
    }
}