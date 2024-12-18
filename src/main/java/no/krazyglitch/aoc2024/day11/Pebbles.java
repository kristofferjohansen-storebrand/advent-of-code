package no.krazyglitch.aoc2024.day11;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class Pebbles {

    public Pebbles() {
        try {
            final String data = FileUtil.readInputFile(this.getClass()).getFirst();
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("There are %d stones present after 25 blinks\n", getStonesAfterRecursiveBlinks(data, 25));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("There are %d stones present after 75 blinks\n", getStonesAfterRecursiveBlinks(data, 75));
            System.out.printf("Part two took %d ms", getMillisSince(start));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static long getStonesAfterRecursiveBlinks(final String data, final int blinks) {
        final List<Pebble> pebbles = parsePebbles(data);
        final Map<Long, long[]> resolutionMap = new HashMap<>();
        final Map<Pebble, Long> pebbleMap = new HashMap<>();

        return pebbles.stream()
                .mapToLong(pebble -> getStonesAfterBlinks(pebble, blinks, 0, resolutionMap, pebbleMap))
                .sum();
    }

    private static long getStonesAfterBlinks(final Pebble pebble, final int blinks, final int iteration, final Map<Long, long[]> resolutionMap, final Map<Pebble, Long> pebbleMap) {
        if (iteration == blinks) {
            return 1;
        }

        if (pebbleMap.containsKey(pebble)) {
            return pebbleMap.get(pebble);
        }

        final long number = pebble.number();
        Pebble leftPebble;
        Pebble rightPebble = null;
        if (resolutionMap.containsKey(number)) {
            final long[] resolvedNumber = resolutionMap.get(number);
            if (resolvedNumber.length == 2) {
                leftPebble = new Pebble(resolvedNumber[0], iteration + 1);
                rightPebble = new Pebble(resolvedNumber[1], iteration + 1);
            } else {
                leftPebble = new Pebble(resolvedNumber[0], iteration + 1);
            }
        } else {
            if (number == 0) {
                leftPebble = new Pebble(1, iteration + 1);
                resolutionMap.put(number, new long[]{1});
            } else if (((long) Math.log10(number) + 1) % 2 == 0) {
                final String numberAsString = String.valueOf(number);
                final long leftNumber = Long.parseLong(numberAsString.substring(0, numberAsString.length() / 2));
                final long rightNumber = Long.parseLong(numberAsString.substring(numberAsString.length() / 2));
                leftPebble = new Pebble(leftNumber, iteration + 1);
                rightPebble = new Pebble(rightNumber, iteration + 1);
                resolutionMap.put(number, new long[]{leftNumber, rightNumber});
            } else {
                leftPebble = new Pebble(number * 2024, iteration + 1);
                resolutionMap.put(number, new long[]{number * 2024});
            }
        }

        final long leftStones = getStonesAfterBlinks(leftPebble, blinks, iteration + 1, resolutionMap, pebbleMap);
        final long rightStones = rightPebble != null ? getStonesAfterBlinks(rightPebble, blinks, iteration + 1, resolutionMap, pebbleMap) : 0;

        pebbleMap.put(pebble, leftStones + rightStones);
        return leftStones + rightStones;
    }

    private static List<Pebble> parsePebbles(final String data) {
        return Arrays.stream(data.split(" "))
                .map(number -> new Pebble(Long.parseLong(number), 0))
                .toList();
    }

    private record Pebble(long number, int iteration) {
    }

    public static void main(final String[] args) {
        new Pebbles();
    }
}
