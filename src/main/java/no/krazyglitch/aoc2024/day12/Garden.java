package no.krazyglitch.aoc2024.day12;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class Garden {

    public Garden() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The price of the fencing is %d\n", getFencingPrice(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The price of the bulk fencing is %d\n", getBulkFencingPrice(data));
            System.out.printf("Part two took %d ms", getMillisSince(start));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static int getFencingPrice(final List<String> data) {
        final Plant[][] garden = parseGarden(data);
        final List<Set<Plot>> plots = getPlots(garden, false);

        return plots.stream()
                .flatMap(Collection::stream)
                .mapToInt(plot -> plot.area * plot.perimeter)
                .sum();
    }

    public static int getBulkFencingPrice(final List<String> data) {
        final Plant[][] garden = parseGarden(data);
        final List<Set<Plot>> plots = getPlots(garden, true);

        return plots.stream()
                .flatMap(Collection::stream)
                .mapToInt(plot -> plot.area * plot.perimeter)
                .sum();
    }

    private static List<Set<Plot>> getPlots(final Plant[][] garden, final boolean useBulkPerimeter) {
        final Set<Coordinate> visited = new HashSet<>();
        final List<Set<Plot>> plots = new ArrayList<>();

        for (final Plant[] plants : garden) {
            for (final Plant plant : plants) {
                if (visited.contains(plant.coordinate)) {
                    continue;
                }

                final Set<Plant> plot = new HashSet<>();
                visit(garden, plant, visited, plot, plant.symbol);

                createPlot(garden, plot, plant, plots, useBulkPerimeter);
            }
        }

        return plots;
    }

    private static int getCorners(final Plant plant, final Plant[][] garden) {
        final Direction[] directions = new Direction[]{Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT};

        return IntStream.range(0, directions.length)
                .map(index -> {
                    final Direction directionOne = directions[index];
                    final Direction directionTwo = directions[(index + 1) % directions.length];
                    final Plant neighborOne = getNeighbor(plant, directionOne.getCoordinate(), garden);
                    final Plant neighborTwo = getNeighbor(plant, directionTwo.getCoordinate(), garden);
                    final Plant corner = getNeighbor(plant, directionOne.add(directionTwo), garden);

                    return (!plant.compareSymbol(neighborOne) && !plant.compareSymbol(neighborTwo)) || (plant.compareSymbol(neighborOne) && plant.compareSymbol(neighborTwo) && !plant.compareSymbol(corner)) ? 1 : 0;
                })
                .sum();
    }

    private static Plant getNeighbor(final Plant plant, final Coordinate coordinate, final Plant[][] garden) {
        final int x = plant.coordinate.x() + coordinate.x();
        final int y = plant.coordinate.y() + coordinate.y();
        return x >= 0 && x < garden[0].length && y >= 0 && y < garden.length ? garden[y][x] : null;
    }

    private static void createPlot(final Plant[][] garden, final Set<Plant> plot, final Plant plant, final List<Set<Plot>> plots, final boolean useBulkPerimeter) {
        final int area = plot.size();
        final int perimeter = useBulkPerimeter ? getCorners(garden, plot) : getPerimeter(garden, plot, plant);

        plots.add(Set.of(new Plot(plot.toArray(Plant[]::new), area, perimeter)));
    }

    private static int getPerimeter(final Plant[][] garden, final Set<Plant> plot, final Plant plant) {

        return plot.stream()
                .mapToInt(p -> {
                    final int px = p.coordinate.x();
                    final int py = p.coordinate.y();
                    return (px == 0 || garden[py][px - 1].symbol != plant.symbol ? 1 : 0) +
                            (px == garden[0].length - 1 || garden[py][px + 1].symbol != plant.symbol ? 1 : 0) +
                            (py == 0 || garden[py - 1][px].symbol != plant.symbol ? 1 : 0) +
                            (py == garden.length - 1 || garden[py + 1][px].symbol != plant.symbol ? 1 : 0);
                })
                .sum();
    }

    private static int getCorners(final Plant[][]garden, final Set<Plant> plot) {
        return plot.stream()
                .mapToInt(p -> getCorners(p, garden))
                .sum();
    }

    private static void visit(final Plant[][] garden, final Plant plant, final Set<Coordinate> visited, final Set<Plant> plot, final char symbol) {
        if (plant == null || plant.symbol != symbol || visited.contains(plant.coordinate)) {
            return;
        }

        visited.add(plant.coordinate);
        plot.add(plant);

        Arrays.stream(Direction.values())
                .map(direction -> getNeighbor(plant, direction.getCoordinate(), garden))
                .forEach(neighbor -> visit(garden, neighbor, visited, plot, symbol));
    }

    private static Plant[][] parseGarden(final List<String> data) {
        final int gardenHeight = data.size();
        final int gardenWidth = data.getFirst().length();
        final Plant[][] garden = new Plant[gardenHeight][gardenWidth];

        for (int y = 0; y < gardenHeight; y++) {
            final String row = data.get(y);
            for (int x = 0; x < gardenWidth; x++) {
                garden[y][x] = new Plant(row.charAt(x), new Coordinate(x, y));
            }
        }

        return garden;
    }

    private enum Direction {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        private final int x;
        private final int y;

        Direction(final int x, final int y) {
            this.x = x;
            this.y = y;
        }

        Coordinate add(final Direction other) {
            return new Coordinate(x + other.x, y + other.y);
        }

        Coordinate getCoordinate() {
            return new Coordinate(x, y);
        }
    }

    private record Coordinate(int x, int y) {
    }

    private record Plant(char symbol, Coordinate coordinate) {
        boolean compareSymbol(final Plant other) {
            return other != null && symbol == other.symbol;
        }
    }

    private record Plot(Plant[] plants, int area, int perimeter) {
    }

    public static void main(final String[] args) {
        new Garden();
    }
}
