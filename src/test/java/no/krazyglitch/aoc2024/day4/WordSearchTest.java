package no.krazyglitch.aoc2024.day4;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordSearchTest {
    private final List<String> data = List.of("MMMSXXMASM",
            "MSAMXMSMSA",
            "AMXSXMAAMM",
            "MSAMASMSMX",
            "XMASAMXAMM",
            "XXAMMXXAMA",
            "SMSMSASXSS",
            "SAXAMASAAA",
            "MAMMMXMMMM",
            "MXMXAXMASX");

    @Test
    void testPartOneExampleA() {
        assertEquals(18, WordSearch.performWordSearch(data, "XMAS"));
    }

    @Test
    void testPartOneExampleB() {
        assertEquals(9, WordSearch.findCrossMASes(data));
    }
}