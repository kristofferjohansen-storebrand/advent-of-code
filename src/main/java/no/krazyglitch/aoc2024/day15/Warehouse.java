package no.krazyglitch.aoc2024.day15;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class Warehouse {

    public Warehouse() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The sum of the box positions is %d\n", getBoxPositionSum(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The sum of the crate positions is %d\n", getCratePositionSum(data, false));
            System.out.printf("Part two took %d ms\n", getMillisSince(start));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static int getBoxPositionSum(final List<String> data) {
        final int emptyLinePosition = data.indexOf("");

        final Layout layout = parseLayout(data.subList(0, emptyLinePosition));
        final List<Direction> commands = parseCommands(data, emptyLinePosition);

        processCommands(layout, commands);
        return calculateScore(layout.grid, Tile.BOX);
    }

    private static List<Direction> parseCommands(final List<String> data, final int emptyLinePosition) {
        return String.join("", data.subList(emptyLinePosition + 1, data.size())).chars()
                .mapToObj(symbol -> switch (symbol) {
                    case '^' -> Direction.NORTH;
                    case '>' -> Direction.EAST;
                    case 'v' -> Direction.SOUTH;
                    case '<' -> Direction.WEST;
                    default -> throw new IllegalStateException("Unexpected value: " + symbol);
                })
                .toList();
    }

    public static int getCratePositionSum(final List<String> data, final boolean debug) {
        final int emptyLinePosition = data.indexOf("");

        final Layout layout = parseCrateLayout(data.subList(0, emptyLinePosition));
        final List<Direction> commands = parseCommands(data, emptyLinePosition);

        processCrateCommands(layout, commands, debug);
        return calculateScore(layout.grid, Tile.LEFT_CRATE);
    }

    private static void processCommands(final Layout layout, final List<Direction> commands) {
        Coordinate position = layout.robotPosition;
        for (Direction direction : commands) {
            position = processCommand(layout.grid, direction, position);
        }
    }

    private static Coordinate processCommand(final Tile[][] grid, final Direction direction, final Coordinate position) {
        final Coordinate targetPosition = position.translate(direction);
        final Tile target = getTarget(grid, targetPosition);

        if (target == Tile.EMPTY) {
            return targetPosition;
        } else if (target == Tile.WALL) {
            return position;
        }

        final boolean boxWasPushed = pushBox(grid, direction, targetPosition);

        return boxWasPushed ? targetPosition : position;
    }

    private static void processCrateCommands(final Layout layout, final List<Direction> commands, final boolean debug) {
        Coordinate position = layout.robotPosition;
        if (debug) {
            System.out.println("Initial position");
            System.out.println(printGrid(layout.grid, position));
        }
        for (Direction direction : commands) {
            if (debug) {
                System.out.println("Moving " + direction.symbol);
            }
            position = processCrateCommand(layout.grid, direction, position);
            if (debug) {
                System.out.println(printGrid(layout.grid, position));
            }
        }
    }

    private static Coordinate processCrateCommand(final Tile[][] grid, final Direction direction, final Coordinate position) {
        final Coordinate targetPosition = position.translate(direction);
        final Tile target = getTarget(grid, targetPosition);

        if (target == Tile.EMPTY) {
            return targetPosition;
        } else if (target == Tile.WALL) {
            return position;
        }

        final boolean crateWasPushed = canPushCrate(grid, direction, targetPosition, new HashSet<>());

        if (crateWasPushed) {
            pushCrate(grid, direction, targetPosition, new HashSet<>());
            return targetPosition;
        }

        return position;
    }

    private static boolean pushBox(final Tile[][] grid, final Direction direction, final Coordinate position) {
        final Coordinate pushedPosition = position.translate(direction);
        final Tile targetTile = getTarget(grid, pushedPosition);

        if (targetTile == Tile.WALL) {
            return false;
        } else if (targetTile == Tile.EMPTY || pushBox(grid, direction, pushedPosition)) {
            grid[position.y][position.x] = Tile.EMPTY;
            grid[pushedPosition.y][pushedPosition.x] = Tile.BOX;

            return true;
        }

        return false;
    }

    private static void pushCrate(final Tile[][] grid, final Direction direction, final Coordinate position, final Set<Coordinate> visited) {
        if (visited.contains(position)) {
            return;
        }

        visited.add(position);

        final Tile pushedCrate = getTarget(grid, position);

        final Coordinate pushedPosition = position.translate(direction);
        final Tile targetTile = getTarget(grid, pushedPosition);

        if (targetTile != Tile.EMPTY) {
            pushCrate(grid, direction, pushedPosition, visited);
        }

        if (Direction.verticalDirections().contains(direction)) {
            if (pushedCrate == Tile.LEFT_CRATE) {
                pushCrate(grid, direction, position.translate(Direction.EAST), visited);
            } else {
                pushCrate(grid, direction, position.translate(Direction.WEST), visited);
            }
        }

        grid[pushedPosition.y][pushedPosition.x] = pushedCrate;
        grid[position.y][position.x] = Tile.EMPTY;
    }

    private static boolean canPushCrate(final Tile[][] grid, final Direction direction, final Coordinate pushedPosition, final Set<Coordinate> visited) {
        if (visited.contains(pushedPosition)) {
            return true;
        }

        visited.add(pushedPosition);

        final Tile pushedCrate = getTarget(grid, pushedPosition);
        final Coordinate targetPosition = pushedPosition.translate(direction);
        final Tile targetTile = getTarget(grid, targetPosition);

        if (Direction.verticalDirections().contains(direction)) {
            final boolean neighborCanBePushed = pushedCrate == Tile.LEFT_CRATE ? canPushCrate(grid, direction, pushedPosition.translate(Direction.EAST), visited) : canPushCrate(grid, direction, pushedPosition.translate(Direction.WEST), visited);

            if (targetTile == Tile.EMPTY) {
                return neighborCanBePushed;
            }

            if (targetTile == Tile.WALL) {
                return false;
            }

            return neighborCanBePushed && canPushCrate(grid, direction, targetPosition, visited);
        } else {
            if (targetTile == Tile.WALL) {
                return false;
            }

            if (targetTile == Tile.EMPTY) {
                return true;
            }

            return canPushCrate(grid, direction, targetPosition, visited);
        }
    }

    private static int calculateScore(final Tile[][] grid, final Tile targetTile) {
        int sum = 0;
        final int gridHeight = grid.length;
        final int gridWidth = grid[0].length;

        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                if (grid[y][x] == targetTile) {
                    sum += x + y * 100;
                }
            }
        }

        return sum;
    }

    private static Tile getTarget(final Tile[][] grid, final Coordinate target) {
        final int targetX = target.x;
        final int targetY = target.y;

        if (targetX < 0 || targetX >= grid[0].length || targetY < 0 || targetY >= grid.length) {
            return Tile.WALL;
        }

        return grid[targetY][targetX];
    }

    private static Layout parseLayout(final List<String> data) {
        final int gridHeight = data.size();
        final int gridWidth = data.getFirst().length();

        final Tile[][] grid = new Tile[data.size()][data.getFirst().length()];
        Coordinate startingCoordinate = new Coordinate(-1, -1);

        for (int y = 0; y < gridHeight; y++) {
            final String line = data.get(y);
            for (int x = 0; x < gridWidth; x++) {
                switch (line.charAt(x)) {
                    case '#':
                        grid[y][x] = Tile.WALL;
                        break;
                    case '.':
                        grid[y][x] = Tile.EMPTY;
                        break;
                    case 'O':
                        grid[y][x] = Tile.BOX;
                        break;
                    case '@':
                        grid[y][x] = Tile.EMPTY;
                        startingCoordinate = new Coordinate(x, y);
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected symbol " + line.charAt(x) + " found at coordinate " + new Coordinate(x, y));
                }
            }
        }

        return new Layout(startingCoordinate, grid);
    }

    private static Layout parseCrateLayout(final List<String> data) {
        final int gridHeight = data.size();
        final int gridWidth = data.getFirst().length();

        final Tile[][] grid = new Tile[gridHeight][gridWidth * 2];
        Coordinate startingCoordinate = new Coordinate(-1, -1);

        for (int y = 0; y < gridHeight; y++) {
            final String line = data.get(y);
            for (int x = 0; x < gridWidth; x++) {
                final int actualX = x * 2;
                switch (line.charAt(x)) {
                    case '#':
                        grid[y][actualX] = Tile.WALL;
                        grid[y][actualX + 1] = Tile.WALL;
                        break;
                    case '.':
                        grid[y][actualX] = Tile.EMPTY;
                        grid[y][actualX + 1] = Tile.EMPTY;
                        break;
                    case 'O':
                        grid[y][actualX] = Tile.LEFT_CRATE;
                        grid[y][actualX + 1] = Tile.RIGHT_CRATE;
                        break;
                    case '@':
                        grid[y][actualX] = Tile.EMPTY;
                        grid[y][actualX + 1] = Tile.EMPTY;
                        startingCoordinate = new Coordinate(actualX, y);
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected symbol " + line.charAt(x) + " found at coordinate " + new Coordinate(x, y));
                }
            }
        }

        return new Layout(startingCoordinate, grid);
    }

    private record Coordinate(int x, int y) {
        Coordinate translate(Direction direction) {
            return new Coordinate(x + direction.x, y + direction.y);
        }
    }

    private record Layout(Coordinate robotPosition, Tile[][] grid) {
    }

    enum Direction {
        NORTH(0, -1, "^"),
        EAST(1, 0, ">"),
        SOUTH(0, 1, "v"),
        WEST(-1, 0, "<");

        final int x;
        final int y;
        final String symbol;

        Direction(final int x, final int y, final String symbol) {
            this.x = x;
            this.y = y;
            this.symbol = symbol;
        }

        static Set<Direction> horizontalDirections() {
            return EnumSet.of(EAST, WEST);
        }

        static Set<Direction> verticalDirections() {
            return EnumSet.of(NORTH, SOUTH);
        }
    }

    private static String printGrid(final Tile[][] grid, final Coordinate robotPosition) {
        final StringBuilder sb = new StringBuilder();
        final int gridHeight = grid.length;
        final int gridWidth = grid[0].length;

        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                final Tile tile = grid[y][x];
                if (Objects.equals(robotPosition, new Coordinate(x, y))) {
                    sb.append("@");
                } else {
                    switch (tile) {
                        case WALL -> sb.append("#");
                        case EMPTY -> sb.append(".");
                        case BOX -> sb.append("O");
                        case LEFT_CRATE -> sb.append("[");
                        case RIGHT_CRATE -> sb.append("]");
                    }
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(final String[] args) {
        new Warehouse();
    }
}
