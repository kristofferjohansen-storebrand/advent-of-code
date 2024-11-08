package no.krazyglitch.aoc2015.day9;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class Travels {

    private static final Pattern INPUT_PATTERN = Pattern.compile("(\\w+) to (\\w+) = (\\d+)");

    public Travels() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The shortest distance that goes through each location is %d\n", getShortestDistance(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            System.out.printf("The longest distance that goes through each location is %d\n", getLongestDistance(data));
            System.out.printf("Part two took %d ms", getMillisSince(start));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static int getShortestDistance(final List<String> data) {
        final Map<String, Location> locationMap = new HashMap<>();
        data.forEach(str -> processLine(str, locationMap));

        final List<List<Location>> allPermutations = createPermutations(new ArrayList<>(locationMap.values()));

        return allPermutations.stream()
                .mapToInt(Travels::traversePermutation)
                .min()
                .orElseThrow(() -> new RuntimeException("Could not get min for traverse permutations. Possibly empty list."));
    }

    public static int getLongestDistance(final List<String> data) {
        final Map<String, Location> locationMap = new HashMap<>();
        data.forEach(str -> processLine(str, locationMap));

        final List<List<Location>> allPermutations = createPermutations(new ArrayList<>(locationMap.values()));

        return allPermutations.stream()
                .mapToInt(Travels::traversePermutation)
                .max()
                .orElseThrow(() -> new RuntimeException("Could not get max for traverse permutations. Possibly empty list."));
    }

    private static int traversePermutation(final List<Location> permutation) {
        int distance = 0;
        for (int i = 0; i < permutation.size()-1; i++) {
            distance += permutation.get(i).getDistanceToLocation(permutation.get(i+1));
        }

        return distance;
    }

    private static List<List<Location>> createPermutations(final List<Location> locations) {
        if (locations.isEmpty()) {
            final List<List<Location>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        }

        final List<List<Location>> permutations = new ArrayList<>();
        final Location firstLocation = locations.removeFirst();
        final List<List<Location>> recursivePermutations = createPermutations(locations);

        for (final List<Location> innerList : recursivePermutations) {
            for (int i = 0; i <= innerList.size(); i++) {
                final List<Location> tempLocations = new ArrayList<>(innerList);
                tempLocations.add(i, firstLocation);
                permutations.add(tempLocations);
            }
        }

        return permutations;
    }

    private static void processLine(final String data, final Map<String, Location> locationMap) {
        final Matcher matcher = INPUT_PATTERN.matcher(data);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Input line did not match the pattern: " + data);
        }

        final Location sourceLocation = locationMap.computeIfAbsent(matcher.group(1), Location::new);
        final Location targetLocation = locationMap.computeIfAbsent(matcher.group(2), Location::new);
        sourceLocation.addNeighbor(targetLocation, Integer.parseInt(matcher.group(3)));
        targetLocation.addNeighbor(sourceLocation, Integer.parseInt(matcher.group(3)));
    }

    public static void main(final String[] args) {
        new Travels();
    }
}

class Location {
    private final String name;
    private final Set<Set<Location>> permutations;
    private final Map<Location, Integer> distanceMap;

    public Location(final String name) {
        this.name = name;
        this.permutations = new HashSet<>();
        this.distanceMap = new HashMap<>();
    }

    public void addNeighbor(final Location location, final int distance) {
        distanceMap.put(location, distance);
    }

    public int getDistanceToLocation(final Location other) {
        return distanceMap.get(other);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Location location = (Location) o;
        return Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}