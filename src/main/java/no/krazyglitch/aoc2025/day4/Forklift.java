package no.krazyglitch.aoc2025.day4;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class Forklift {

    public Forklift() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The sum of accessible rolls of paper is %d%n", getAccessibleRolls(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The sum of accessible rolls of paper with removal is %d%n", getAccessibleRollsRecursive(data));
            System.out.printf("Part two took %d ms\n\n", getMillisSince(start));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static int getAccessibleRolls(final List<String> data) {
        final Tile[][] grid = buildGrid(data);
        final Set<Coordinate> removableRolls = getRemovableRolls(grid);

        return removableRolls.size();
    }

    public static int getAccessibleRollsRecursive(final List<String> data) {
        final Tile[][] grid = buildGrid(data);
        return getAccessibleRollsRecursive(grid);
    }

    private static int getAccessibleRollsRecursive(final Tile[][] grid) {
        final Set<Coordinate> removableRolls = getRemovableRolls(grid);
        if (removableRolls.isEmpty()) {
            return 0;
        }

        removeRolls(grid, removableRolls);
        return removableRolls.size() + getAccessibleRollsRecursive(grid);
    }

    private static Set<Coordinate> getRemovableRolls(final Tile[][] grid) {
        final Set<Coordinate> removableRolls = new HashSet<>();
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid.length; x++) {
                if (grid[y][x] == Tile.ROLL && getNeighborRolls(grid, x, y) < 4) {
                    removableRolls.add(new Coordinate(x, y));
                }
            }
        }
        return removableRolls;
    }

    private static int getNeighborRolls(final Tile[][] grid, final int targetX, final int targetY) {
        int count = 0;

        for (int y = targetY - 1; y <= targetY + 1; y++) {
            for (int x = targetX - 1; x <= targetX + 1; x++) {
                if (x == targetX && y == targetY) {
                    continue;
                }

                if (isRoll(grid, x, y)) {
                    count++;
                }
            }
        }

        return count;
    }

    private static boolean isRoll(final Tile[][] grid, final int x, final int y) {
        if (x < 0 || x >= grid[0].length || y < 0 || y >= grid.length) {
            return false;
        }

        return grid[y][x] == Tile.ROLL;
    }

    private static Tile[][] buildGrid(final List<String> data) {
        final Tile[][] tileGrid = new Tile[data.size()][data.getFirst().length()];
        for (int y = 0; y < tileGrid.length; y++) {
            for (int x = 0; x < tileGrid[0].length; x++) {
                tileGrid[y][x] = Tile.fromValue(data.get(y).charAt(x));
            }
        }

        return tileGrid;
    }

    private static void removeRolls(final Tile[][] grid, final Set<Coordinate> removedRolls) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] == Tile.ROLL && removedRolls.contains(new Coordinate(x, y))) {
                    grid[y][x] = Tile.GROUND;
                }
            }
        }
    }

    static void main() {
        new Forklift();
    }

    record Coordinate(int x, int y) {}

    enum Tile {
        GROUND('.'),
        ROLL('@');

        private final char symbol;

        Tile(final char symbol) {
            this.symbol = symbol;
        }

        public static Tile fromValue(final char value) {
            for (final Tile tile : Tile.values()) {
                if (tile.symbol == value) {
                    return tile;
                }
            }

            throw new IllegalArgumentException("No tile found matching: " + value);
        }
    }
}
