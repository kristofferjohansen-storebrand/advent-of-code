package no.krazyglitch.aoc2024.day5;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class PrintQueue {

    public PrintQueue() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The sum of the middle numbers of all valid updates is: %d\n", getCorrectMiddleNumbers(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The sum of the middle numbers of all corrected updates is: %d\n", getCorrectedMiddleNumbers(data));
            System.out.printf("Part two took %d ms\n", getMillisSince(start));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static int getCorrectMiddleNumbers(final List<String> data) {
        final int emptyLineIndex = data.indexOf("");
        final Map<Integer, List<Integer>> ruleMap = parseRules(data.subList(0, emptyLineIndex));
        final List<List<Integer>> updates = parseUpdates(data.subList(emptyLineIndex+1, data.size()));

        return updates.stream()
                .filter(update -> isUpdateValid(update, ruleMap))
                .mapToInt(PrintQueue::getMiddleUpdate)
                .sum();
    }

    public static int getCorrectedMiddleNumbers(final List<String> data) {
        final int emptyLineIndex = data.indexOf("");
        final Map<Integer, List<Integer>> ruleMap = parseRules(data.subList(0, emptyLineIndex));
        final List<List<Integer>> updates = parseUpdates(data.subList(emptyLineIndex+1, data.size()));

        return updates.stream()
                .filter(update -> !isUpdateValid(update, ruleMap))
                .map(update -> getCorrectedUpdate(update, ruleMap))
                .mapToInt(PrintQueue::getMiddleUpdate)
                .sum();
    }

    private static List<Integer> getCorrectedUpdate(final List<Integer> update,
                                                   final Map<Integer, List<Integer>> ruleMap) {
        final List<Integer> correctedUpdate = new ArrayList<>();
        final Set<Integer> updateSet = new HashSet<>();

        for (final int pageNumber : update) {
            final List<Integer> breakingChangeIndexes = ruleMap.getOrDefault(pageNumber, new ArrayList<>()).stream()
                    .filter(updateSet::contains)
                    .mapToInt(correctedUpdate::indexOf)
                    .boxed()
                    .sorted()
                    .toList();

            if (breakingChangeIndexes.isEmpty()) {
                correctedUpdate.add(pageNumber);
            } else {
                correctedUpdate.add(breakingChangeIndexes.getFirst(), pageNumber);
            }

            updateSet.add(pageNumber);
        }

        return correctedUpdate;
    }

    private static int getMiddleUpdate(final List<Integer> update) {
        return update.get(Math.floorDiv(update.size(), 2));
    }

    private static boolean isUpdateValid(final List<Integer> update,
                                         final Map<Integer, List<Integer>> ruleMap) {
        final Set<Integer> updateSet = new HashSet<>();
        for (final int pageNumber : update) {
            final boolean breakingChange = ruleMap.getOrDefault(pageNumber, new ArrayList<>())
                    .stream()
                    .anyMatch(updateSet::contains);

            if (breakingChange) {
                return false;
            }

            updateSet.add(pageNumber);
        }

        return true;
    }

    private static List<List<Integer>> parseUpdates(final List<String> data) {
        final List<List<Integer>> updates = new ArrayList<>();
        data.forEach(line -> {
            updates.add(Arrays.stream(line.split(","))
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .toList());
        });

        return updates;
    }

    private static Map<Integer, List<Integer>> parseRules(final List<String> data) {
        final Map<Integer, List<Integer>> ruleMap = new HashMap<>();
        data.forEach(line -> {
            final int[] rules = Arrays.stream(line.split("\\|")).mapToInt(Integer::parseInt).toArray();
            ruleMap.computeIfAbsent(rules[0], key -> new ArrayList<>());
            ruleMap.get(rules[0]).add(rules[1]);
        });

        return ruleMap;
    }

    public static void main(final String[] args) {
        new PrintQueue();
    }
}
