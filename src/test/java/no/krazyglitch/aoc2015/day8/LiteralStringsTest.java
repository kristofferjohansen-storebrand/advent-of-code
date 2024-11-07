package no.krazyglitch.aoc2015.day8;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LiteralStringsTest {

    @Test
    void testPartOneExampleA() {
        final List<String> data = List.of("\"\"",
                "\"abc\"",
                "\"aaa\\\"aaa\"",
                "\"\\x27\"");
        assertEquals(12, LiteralStrings.getUnescapedDifference(data));
    }

    @Test
    void testPartTwoEscapeQuotes() {
        final List<String> data = List.of("\"\"",
                "\"abc\"",
                "\"aaa\\\"aaa\"",
                "\"\\x27\"");
        assertEquals(19, LiteralStrings.getEscapedDifference(data));
    }
}