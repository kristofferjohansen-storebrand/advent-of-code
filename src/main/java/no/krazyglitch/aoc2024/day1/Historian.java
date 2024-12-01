package no.krazyglitch.aoc2024.day1;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class Historian {

    public Historian() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The sum of the differences between the two lists is %d\n", getListDifference(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The similarity score of the two lists is %d\n", getSimilarityScore(data));
            System.out.printf("Part two took %d ms", getMillisSince(start));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static int getListDifference(final List<String> data) {
        final List<Integer> firstList = new ArrayList<>();
        final List<Integer> secondList = new ArrayList<>();

        data.forEach(str -> addLocationsToLists(str, firstList, secondList));
        firstList.sort(Integer::compareTo);
        secondList.sort(Integer::compareTo);

        return IntStream.range(0, firstList.size())
                .map(i -> Math.abs(firstList.get(i)-secondList.get(i)))
                .sum();
    }

    public static int getSimilarityScore(final List<String> data) {
        final List<Integer> firstList = new ArrayList<>();
        final Map<Integer, Integer> frequencyMap = new HashMap<>();

        data.forEach(str -> addLocationFrequencies(str, firstList, frequencyMap));

        return firstList.stream()
                .mapToInt(id -> id * frequencyMap.getOrDefault(id, 0))
                .sum();
    }

    private static void addLocationsToLists(final String input, final List<Integer> firstList, final List<Integer> secondList) {
        final String[] locations = getLocations(input);

        firstList.add(Integer.parseInt(locations[0]));
        secondList.add(Integer.parseInt(locations[1]));
    }

    private static void addLocationFrequencies(final String input, final List<Integer> firstList, final Map<Integer, Integer> frequencyMap) {
        final String[] locations = getLocations(input);

        firstList.add(Integer.parseInt(locations[0]));
        frequencyMap.compute(Integer.parseInt(locations[1]), (key, value) -> (value == null) ? 1 : value + 1);
    }

    private static String[] getLocations(final String input) {
        final String[] locations = input.split("\\s+");
        if (locations.length != 2) {
            throw new IllegalArgumentException(String.format("Input did not contain two locations: %s", input));
        }
        return locations;
    }

    public static void main(final String[] args) {
        new Historian();
    }
}
