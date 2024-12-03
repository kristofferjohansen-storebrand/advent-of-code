package no.krazyglitch.aoc2024.day3;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class Mulling {
    private static final Pattern MULTIPLIER_PATTERN = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
    private static final Pattern MULTIPLIER_TOGGLE_PATTERN = Pattern.compile("mul\\((\\d+),(\\d+)\\)|(don't\\(\\))|(do\\(\\))");

    public Mulling() {
        try {
            final String data = String.join("", FileUtil.readInputFile(this.getClass()));
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The sum of all multipliers is %d\n", sumMultipliers(data));
            System.out.printf("Part one took: %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The sum of all multipliers with toggles is %d\n", sumMultipliersWithToggles(data));
            System.out.printf("Part two took: %d ms\n", getMillisSince(start));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static int sumMultipliers(final String data) {
        final Matcher matcher = MULTIPLIER_PATTERN.matcher(data);

        int sum = 0;
        while (matcher.find()) {
            sum += resolveMultiplier(matcher);
        }

        return sum;
    }

    public static int sumMultipliersWithToggles(final String data) {
        final Matcher matcher = MULTIPLIER_TOGGLE_PATTERN.matcher(data);

        int sum = 0;
        boolean enabled = true;
        while (matcher.find()) {
            if (matcher.group(3) != null) {
                enabled = false;
            } else if (matcher.group(4) != null) {
                enabled = true;
            } else if (enabled) {
                sum += resolveMultiplier(matcher);
            }
        }

        return sum;
    }

    private static int resolveMultiplier(final Matcher matcher) {
        return Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
    }

    public static void main(final String[] args) {
        new Mulling();
    }
}
