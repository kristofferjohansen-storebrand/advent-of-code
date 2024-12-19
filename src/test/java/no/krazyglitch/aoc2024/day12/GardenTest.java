package no.krazyglitch.aoc2024.day12;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GardenTest {

    @Test
    void testPartOneExampleA() {
        final List<String> data = Arrays.stream(("AAAA\n" +
                "BBCD\n" +
                "BBCC\n" +
                "EEEC").split("\n"))
                .toList();

        assertEquals(140, Garden.getFencingPrice(data));
    }

    @Test
    void testPartOneExampleB() {
        final List<String> data = Arrays.stream(("OOOOO\n" +
                "OXOXO\n" +
                "OOOOO\n" +
                "OXOXO\n" +
                "OOOOO").split("\n"))
                .toList();

        assertEquals(772, Garden.getFencingPrice(data));
    }

    @Test
    void testPartOneExampleC() {
        final List<String> data = Arrays.stream(("RRRRIICCFF\n" +
                "RRRRIICCCF\n" +
                "VVRRRCCFFF\n" +
                "VVRCCCJFFF\n" +
                "VVVVCJJCFE\n" +
                "VVIVCCJJEE\n" +
                "VVIIICJJEE\n" +
                "MIIIIIJJEE\n" +
                "MIIISIJEEE\n" +
                "MMMISSJEEE").split("\n"))
                .toList();

        assertEquals(1930, Garden.getFencingPrice(data));
    }

    @Test
    void testPartTwoExampleA() {
        final List<String> data = Arrays.stream(("AAAA\n" +
                "BBCD\n" +
                "BBCC\n" +
                "EEEC").split("\n"))
                .toList();

        assertEquals(80, Garden.getBulkFencingPrice(data));
    }

    @Test
    void testPartTwoExampleB() {
        final List<String> data = Arrays.stream(("OOOOO\n" +
                "OXOXO\n" +
                "OOOOO\n" +
                "OXOXO\n" +
                "OOOOO").split("\n"))
                .toList();

        assertEquals(436, Garden.getBulkFencingPrice(data));
    }

    @Test
    void testPartTwoExampleC() {
        final List<String> data = Arrays.stream(("EEEEE\n" +
                "EXXXX\n" +
                "EEEEE\n" +
                "EXXXX\n" +
                "EEEEE").split("\n"))
                .toList();

        assertEquals(236, Garden.getBulkFencingPrice(data));
    }
}