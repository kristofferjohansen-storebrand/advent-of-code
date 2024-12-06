package no.krazyglitch.aoc2024.day6;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class Patrol {

    public Patrol() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The guard visited %d tiles\n", getVisitedTiles(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("There are %d looping obstacles\n", getLoopingObstacleCount(data));
            System.out.printf("Part two took %d ms", getMillisSince(start));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static int getVisitedTiles(final List<String> data) {
        final ParseResult parseResult = parseData(data);
        return getTraversedTiles(parseResult.grid, new Tile(parseResult.guardStartX, parseResult.guardStartY)).size();
    }

    public static int getLoopingObstacleCount(final List<String> data) {
        final ParseResult parseResult = parseData(data);

        final boolean[][] grid = parseResult.grid();
        final Tile startingTile = new Tile(parseResult.guardStartX, parseResult.guardStartY);
        final Set<Tile> visitedTiles = getTraversedTiles(grid, startingTile);

        return (int) visitedTiles.stream()
                .filter(tile -> isPathLooping(grid, startingTile, tile))
                .count();
    }

    private static boolean isPathLooping(final boolean[][] grid, final Tile startingTile, final Tile obstacle) {
        final Set<GuardMovement> movements = new HashSet<>();
        grid[obstacle.y()][obstacle.x()] = false;
        Tile currentTile = startingTile;
        Direction currentDirection = Direction.N;

        while (true) {
            final GuardMovement guardMovement = getNextGuardMovement(grid, currentTile, currentDirection, 0);

            if (guardMovement == null) {
                grid[obstacle.y()][obstacle.x()] = true;
                return false;
            }

            if (movements.contains(guardMovement)) {
                grid[obstacle.y()][obstacle.x()] = true;
                return true;
            }

            movements.add(guardMovement);

            currentTile = guardMovement.getTile();
            currentDirection = guardMovement.direction();
        }
    }


    private static Set<Tile> getTraversedTiles(final boolean[][] grid, final Tile startingTile) {
        final Set<Tile> visitedTiles = new HashSet<>();
        Tile currentTile = startingTile;
        Direction currentDirection = Direction.N;

        while (true) {
            visitedTiles.add(currentTile);

            final GuardMovement guardMovement = getNextGuardMovement(grid, currentTile, currentDirection, 0);

            if (guardMovement == null) {
                break;
            }

            currentTile = guardMovement.getTile();
            currentDirection = guardMovement.direction();
        }

        return visitedTiles;
    }

    private static GuardMovement getNextGuardMovement(final boolean[][] grid, final Tile currentTile, final Direction direction, final int steps) {
        if (steps == 4) {
            throw new IllegalStateException("Guard is stuck in a box");
        }

        final int forwardX = currentTile.x + direction.x;
        final int forwardY = currentTile.y + direction.y;

        if (isCoordinateOutsideGrid(grid, forwardX, forwardY)) {
            return null;
        }

        if (grid[forwardY][forwardX]) {
            return new GuardMovement(direction, forwardX, forwardY);
        }

        return getNextGuardMovement(grid, currentTile, direction.getNextDirection(), steps + 1);
    }

    private static boolean isCoordinateOutsideGrid(final boolean[][] grid, final int x, final int y) {
        return x < 0 || x >= grid[0].length || y < 0 || y >= grid.length;
    }

    private static ParseResult parseData(final List<String> data) {
        final int gridHeight = data.size();
        final int gridWidth = data.getFirst().length();
        final boolean[][] grid = new boolean[gridHeight][gridWidth];
        int guardStartX = -1;
        int guardStartY = -1;

        for (int row = 0; row < gridHeight; row++) {
            final String line = data.get(row);
            for (int col = 0; col < gridWidth; col++) {
                switch(line.charAt(col)) {
                    case '^':
                        guardStartX = col;
                        guardStartY = row;
                        grid[row][col] = true;
                        break;
                    case '.':
                        grid[row][col] = true;
                        break;
                    default:
                        break;
                }
            }
        }

        return new ParseResult(grid, guardStartX, guardStartY);
    }

    private record GuardMovement(Direction direction, int x, int y) {
        Tile getTile() {
            return new Tile(x, y);
        }
    }
    private record ParseResult(boolean[][] grid, int guardStartX, int guardStartY) {}
    private record Tile(int x, int y) {}

    enum Direction {
        N(0, -1),
        E(1, 0),
        S(0, 1),
        W(-1, 0);

        private final int x;
        private final int y;

        Direction(final int x, final int y) {
            this.x = x;
            this.y = y;
        }

        Direction getNextDirection() {
            return switch (this) {
                case N -> E;
                case E -> S;
                case S -> W;
                case W -> N;
            };
        }
    }

    public static void main(final String[] args) {
        new Patrol();
    }
}
