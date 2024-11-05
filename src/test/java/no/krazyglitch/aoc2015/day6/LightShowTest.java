package no.krazyglitch.aoc2015.day6;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LightShowTest {

    @Test
    void testPartOneExampleOne() {
        final List<String> data = List.of("turn on 0,0 through 999,999");
        assertEquals(1000*1000, LightShow.processInstructions(data));
    }

    @Test
    void testPartTwoExampleOne() {
        final List<String> data = List.of("toggle 0,0 through 999,999");
        assertEquals(2000000, LightShow.processDiscoInstructions(data));
    }
}