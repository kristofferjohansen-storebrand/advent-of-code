package no.krazyglitch.aoc2024.day2;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class Reports {

    public Reports() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The number of safe reports is %d\n", getSafeReportCount(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The number of safe reports is %d when ignoring up to one faulty level\n", getSafeReportCountWithRetry(data));
            System.out.printf("Part two took %d ms", getMillisSince(start));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getSafeReportCount(final List<String> data) {
        return (int) data.stream()
                .map(Reports::createLevels)
                .filter(Reports::isReportSafe)
                .count();
    }

    public static int getSafeReportCountWithRetry(final List<String> data) {
        return (int) data.stream()
                .map(Reports::createLevels)
                .filter(report -> isReportSafe(report) || retryReportLevels(report))
                .count();
    }

    private static boolean isReportSafe(final List<Integer> levels) {
        final boolean isIncreasing = levels.getFirst() < levels.get(1);
        for (int i = 0; i < levels.size()-1; i++) {
            if (levelIsUnsafe(levels.get(i), levels.get(i+1), isIncreasing)) {
                return false;
            }
        }

        return true;
    }

    private static boolean levelIsUnsafe(final int firstLevel, final int secondLevel, final boolean isIncreasing) {
        final int difference = secondLevel - firstLevel;

        if (difference == 0) {
            return true;
        } else if (isIncreasing && difference < 0) {
            return true;
        } else if (!isIncreasing && difference > 0) {
            return true;
        }

        return Math.abs(difference) > 3;
    }

    private static List<Integer> createLevels(final String report) {
        final List<Integer> levels = Arrays.stream(report.split("\\s+"))
                .mapToInt(Integer::parseInt)
                .boxed()
                .toList();

        if (levels.size() < 2) {
            throw new IllegalArgumentException("Report must contain at least two levels");
        }

        return levels;
    }

    private static boolean retryReportLevels(final List<Integer> levels) {
        for (int i = 0; i < levels.size(); i++) {
            final List<Integer> levelsCopy = new ArrayList<>(levels);
            levelsCopy.remove(i);

            if (isReportSafe(levelsCopy)) {
                return true;
            }
        }

        return false;
    }

    public static void main(final String[] args) {
        new Reports();
    }
}
