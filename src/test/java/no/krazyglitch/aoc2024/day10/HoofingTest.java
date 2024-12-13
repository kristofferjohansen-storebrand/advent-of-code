package no.krazyglitch.aoc2024.day10;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HoofingTest {

    private final List<String> data = List.of("89010123",
            "78121874",
            "87430965",
            "96549874",
            "45678903",
            "32019012",
            "01329801",
            "10456732");

    @Test
    void testExampleOnePartA() {
        assertEquals(36, Hoofing.getUniqueTrailheadScores(data));
    }

    @Test
    void testExampleOnePartB() {
        assertEquals(81, Hoofing.getTotalTrailScores(data));
    }
}