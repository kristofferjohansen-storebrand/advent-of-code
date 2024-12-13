package no.krazyglitch.aoc2024.day10;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class Hoofing {

    public Hoofing() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The trailhead score is %d\n", getUniqueTrailheadScores(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The total trail score is %d\n", getTotalTrailScores(data));
            System.out.printf("Part two took %d ms", getMillisSince(start));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static int getUniqueTrailheadScores(final List<String> data) {
        final int[][] map = parseMap(data);

        int score = 0;
        final int mapHeight = map.length;
        final int mapWidth = map[0].length;
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                if (map[y][x] == 0) {
                    final Set<Coordinate> trailheads = new HashSet<>();
                    addTrailheads(map, trailheads, x, y, -1);

                    score += trailheads.size();
                }
            }
        }

        return score;
    }

    public static int getTotalTrailScores(final List<String> data) {
        final int[][] map = parseMap(data);

        int score = 0;
        final int mapHeight = map.length;
        final int mapWidth = map[0].length;
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                if (map[y][x] == 0) {
                    final List<Coordinate> trailheads = new ArrayList<>();
                    addTrailheads(map, trailheads, x, y, -1);

                    score += trailheads.size();
                }
            }
        }

        return score;
    }

    private static void addTrailheads(final int[][] map, final Collection<Coordinate> trailHeads, final int x, final int y, final int elevation) {
        if (y < 0 || y >= map.length || x < 0 || x >= map[0].length) {
            return;
        }

        final int elevationDifference = map[y][x] - elevation;

        if (elevationDifference != 1) {
            return;
        }

        if (map[y][x] == 9) {
            trailHeads.add(new Coordinate(x, y));
        }

        Arrays.stream(Direction.values())
                .forEach(direction -> addTrailheads(map, trailHeads, x + direction.x, y + direction.y, elevation + 1));
    }

    private static int[][] parseMap(final List<String> data) {
        final int mapHeight = data.size();
        final int mapWidth = data.getFirst().length();
        final int[][] map = new int[mapHeight][mapWidth];

        for (int y = 0; y < data.size(); y++) {
            final String line = data.get(y);

            for (int x = 0; x < line.length(); x++) {
                map[y][x] = Character.getNumericValue(line.charAt(x));
            }
        }

        return map;
    }

    private record Coordinate(int x, int y) {
    }

    enum Direction {
        N(0, -1),
        E(1, 0),
        S(0, 1),
        W(-1, 0);

        private final int x;
        private final int y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(final String[] args) {
        new Hoofing();
    }
}
