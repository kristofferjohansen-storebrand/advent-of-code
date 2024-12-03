package no.krazyglitch.aoc2024.day3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MullingTest {

    @Test
    void testPartOneExampleA() {
        final String data = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))";
        assertEquals(161, Mulling.sumMultipliers(data));
    }

    @Test
    void testPartTwoExampleA() {
        final String data = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))";
        assertEquals(48, Mulling.sumMultipliersWithToggles(data));
    }
}