package no.krazyglitch.aoc2024.day2;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportsTest {

    @Test
    void testPartOneExampleA() {
        final List<String> data = List.of("7 6 4 2 1",
                "1 2 7 8 9",
                "9 7 6 2 1",
                "1 3 2 4 5",
                "8 6 4 4 1",
                "1 3 6 7 9");

        assertEquals(2, Reports.getSafeReportCount(data));
    }

    @Test
    void testPartTwoExampleA() {
        final List<String> data = List.of("7 6 4 2 1",
                "1 2 7 8 9",
                "9 7 6 2 1",
                "1 3 2 4 5",
                "8 6 4 4 1",
                "1 3 6 7 9");

        assertEquals(4, Reports.getSafeReportCountWithRetry(data));
    }
}