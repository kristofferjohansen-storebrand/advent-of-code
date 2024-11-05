package no.krazyglitch.aoc2015.day2;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WrappingPaperTest {

    @Test
    void testPartOneExampleA() {
        final List<String> data = List.of("2x3x4");
        assertEquals(58, WrappingPaper.wrapPresents(data));
    }

    @Test
    void testPartOneExampleB() {
        final List<String> data = List.of("1x1x10");
        assertEquals(43, WrappingPaper.wrapPresents(data));
    }

    @Test
    void testPartTwoExampleA() {
        final List<String> data = List.of("2x3x4");
        assertEquals(34, WrappingPaper.tiePresents(data));
    }

    @Test
    void testPartTwoExampleB() {
        final List<String> data = List.of("1x1x10");
        assertEquals(14, WrappingPaper.tiePresents(data));
    }
}