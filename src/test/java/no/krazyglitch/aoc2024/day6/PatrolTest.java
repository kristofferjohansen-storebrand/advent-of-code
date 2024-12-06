package no.krazyglitch.aoc2024.day6;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatrolTest {
    private final List<String> data = List.of("....#.....",
            ".........#",
            "..........",
            "..#.......",
            ".......#..",
            "..........",
            ".#..^.....",
            "........#.",
            "#.........",
            "......#...");

    private final List<String> dataBoxLoop = List.of("....#.....",
            ".........#",
            "..........",
            "..#.......",
            ".......#..",
            "....#.....",
            ".#.#^#....",
            "....#...#.",
            "#.........",
            "......#...");

    @Test
    void testPartOneExampleA() {
        assertEquals(41, Patrol.getVisitedTiles(data));
    }

    @Test
    void testPartTwoExampleA() {
        assertEquals(6, Patrol.getLoopingObstacleCount(data));
    }

    @Test
    void testPartTwoExampleB() {
        assertThrows(IllegalStateException.class, () -> Patrol.getLoopingObstacleCount(dataBoxLoop));
    }
}