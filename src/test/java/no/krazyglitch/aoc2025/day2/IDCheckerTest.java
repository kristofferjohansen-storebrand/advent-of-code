package no.krazyglitch.aoc2025.day2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IDCheckerTest {
    private static final String DATA = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224," +
            "1698522-1698528,446443-446449,38593856-38593862,565653-565659," +
            "824824821-824824827,2121212118-2121212124";

    @Test
    void testDayTwoPartOne() {
        final long result = IDChecker.getSumOfDuplicateInvalidIDs(DATA);
        assertEquals(1227775554L, result);
    }

    @Test
    void testDayTwoPartTwo() {
        final long result = IDChecker.getSumOfRepeatingInvalidIDs(DATA);
        assertEquals(4174379265L, result);
    }
}