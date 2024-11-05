package no.krazyglitch.aoc2015.day3;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Delivery {

    public Delivery() {
        try {
            final String data = FileUtil.readInputFile(this.getClass()).getFirst();
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("Santa visited %d unique houses\n", getGiftedHouses(data));
            System.out.printf("Part one took %d ms\n\n", ChronoUnit.MILLIS.between(start, LocalDateTime.now()));

            start = LocalDateTime.now();
            System.out.printf("Santa and RoboSanta visited %d unique houses between them\n", getGiftedHousesWithRobot(data));
            System.out.printf("Part two took %d ms", ChronoUnit.MILLIS.between(start, LocalDateTime.now()));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static int getGiftedHouses(final String data) {
        final Map<Integer, Map<Integer, Integer>> route = calculateRoute(data);

        return route.values().stream()
                .mapToInt(Map::size)
                .sum();
    }

    public static int getGiftedHousesWithRobot(final String data) {
        final Map<Integer, Map<Integer, Integer>> route = calculateRoboRoute(data);

        return route.values().stream()
                .mapToInt(Map::size)
                .sum();
    }

    private static Map<Integer, Map<Integer, Integer>> calculateRoute(final String data) {
        final Map<Integer, Map<Integer, Integer>> route = initializeRoute();
        final Santa santa = new Santa();
        data.chars().forEach(move -> santa.move(move, route));

        return route;
    }

    private static Map<Integer, Map<Integer, Integer>> calculateRoboRoute(final String data) {
        final Map<Integer, Map<Integer, Integer>> route = initializeRoute();
        final Santa santa = new Santa();
        final Santa roboSanta = new Santa();

        final char[] instructions = data.toCharArray();
        IntStream.range(0, instructions.length).forEach(i -> {
            if (i % 2 == 0) {
                santa.move(instructions[i], route);
            } else {
                roboSanta.move(instructions[i], route);
            }
        });

        return route;
    }

    private static Map<Integer, Map<Integer, Integer>> initializeRoute() {
        final Map<Integer, Map<Integer, Integer>> route = new HashMap<>();
        final Map<Integer, Integer> initialPosition = new HashMap<>();
        initialPosition.put(0, 1);
        route.put(0, initialPosition);
        return route;
    }

    public static void main(final String[] args) {
        new Delivery();
    }
}

class Santa {
    private int positionX;
    private int positionY;

    public void move(final int direction, final Map<Integer, Map<Integer, Integer>> route) {
        switch (direction) {
            case '>':
                positionX++;
                break;
            case '<':
                positionX--;
                break;
            case '^':
                positionY++;
                break;
            case 'v':
                positionY--;
                break;
            default:
                return;
        }

        final Map<Integer, Integer> horizontalPlane = route.computeIfAbsent(positionX, (key) -> new HashMap<>());
        horizontalPlane.merge(positionY, 1, (key, value) -> value+1);
    }
}
