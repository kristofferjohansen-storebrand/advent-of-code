package no.krazyglitch.aoc2025.day7;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class Tachyonic {

    public Tachyonic() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The amount of reached splitters is %d%n", getSplitCount(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The amount of timelines is %d%n", getTimelineCount(data));
            System.out.printf("Part two took %d ms\n\n", getMillisSince(start));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static int getSplitCount(final List<String> data) {
        final Tile[][] grid = parseGrid(data);
        final Coordinate beamCoordinate = getBeamCoordinate(grid);
        final Set<Coordinate> hitSplitterLocations = new HashSet<>();

        traverse(beamCoordinate, grid, hitSplitterLocations);
        return hitSplitterLocations.size();
    }

    public static long getTimelineCount(final List<String> data) {
        final Tile[][] grid = parseGrid(data);
        final Coordinate beamCoordinate = getBeamCoordinate(grid);
        final Map<Integer, Map<Integer, Long>> timelineTrackMap = new HashMap<>();

        return traverseTimelines(beamCoordinate, grid, timelineTrackMap);
    }

    private static void traverse(final Coordinate coordinate, final Tile[][] grid, final Set<Coordinate> hitSplitterLocations) {
        if (isCoordinateOffGrid(coordinate, grid) || hitSplitterLocations.contains(coordinate)) {
            return;
        }

        final Tile currentTile = grid[coordinate.y()][coordinate.x()];
        if (currentTile == Tile.EMPTY || currentTile == Tile.BEAM) {
            traverse(new Coordinate(coordinate.x(), coordinate.y() + 1), grid, hitSplitterLocations);
        } else {
            hitSplitterLocations.add(coordinate);
            traverse(new Coordinate(coordinate.x() - 1, coordinate.y()), grid, hitSplitterLocations);
            traverse(new Coordinate(coordinate.x() + 1, coordinate.y()), grid, hitSplitterLocations);
        }
    }

    private static long traverseTimelines(final Coordinate coordinate, final Tile[][] grid, final Map<Integer, Map<Integer, Long>> timelineTrackMap) {
        if (isCoordinateOffGrid(coordinate, grid)) {
            return 1;
        }


        if (timelineTrackMap.containsKey(coordinate.y())) {
            final Map<Integer, Long> horizontalPlane = timelineTrackMap.get(coordinate.y());
            if (horizontalPlane.containsKey(coordinate.x())) {
                return horizontalPlane.get(coordinate.x());
            }
        }

        final Tile currentTile = grid[coordinate.y()][coordinate.x()];
        if (currentTile == Tile.BEAM || currentTile == Tile.EMPTY) {
            return traverseTimelines(new Coordinate(coordinate.x(), coordinate.y() + 1), grid, timelineTrackMap);
        }

        final long splitterSum = traverseTimelines(new Coordinate(coordinate.x() - 1, coordinate.y()), grid, timelineTrackMap) +
                traverseTimelines(new Coordinate(coordinate.x() + 1, coordinate.y()), grid, timelineTrackMap);
        final Map<Integer, Long> horizontalPlane = timelineTrackMap.computeIfAbsent(coordinate.y(), i -> new HashMap<>());
        horizontalPlane.put(coordinate.x(), splitterSum);

        return splitterSum;
    }

    private static boolean isCoordinateOffGrid(final Coordinate coordinate, final Tile[][] grid) {
        return (coordinate.x() < 0 || coordinate.x() >= grid[0].length || coordinate.y() < 0 || coordinate.y() >= grid.length);
    }

    private static Tile[][] parseGrid(final List<String> data) {
        final Tile[][] grid = new Tile[data.size()][data.getFirst().length()];
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                grid[y][x] = Tile.fromValue(data.get(y).charAt(x));
            }
        }

        return grid;
    }

    private static Coordinate getBeamCoordinate(final Tile[][] grid) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] == Tile.BEAM) {
                    return new Coordinate(x, y);
                }
            }
        }

        throw new IllegalStateException("Grid does not contain beam: " + Arrays.deepToString(grid));
    }

    enum Tile {
        EMPTY('.'),
        SPLITTER('^'),
        BEAM('S');

        private final char symbol;

        Tile(final char symbol) {
            this.symbol = symbol;
        }

        public static Tile fromValue(final char symbol) {
            for (final Tile tile : Tile.values()) {
                if (tile.symbol == symbol) {
                    return tile;
                }
            }

            throw new IllegalArgumentException("No valid tile found for symbol: " + symbol);
        }
    }

    record Coordinate(int x, int y) {
    }

    static void main() {
        new Tachyonic();
    }
}
