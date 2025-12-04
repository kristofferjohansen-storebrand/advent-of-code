package no.krazyglitch.aoc2025.day3;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.List;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class PowerBank {

    public PowerBank() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The sum of maximum joltage is %d%n", findMaximumJoltageSum(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The sum of maximum large joltage is %d%n", findLargeMaximumJoltageSum(data));
            System.out.printf("Part two took %d ms\n\n", getMillisSince(start));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static long findMaximumJoltageSum(final List<String> data) {
        return data.stream()
                .mapToLong(PowerBank::findMaximumJoltage)
                .sum();
    }

    public static long findLargeMaximumJoltageSum(final List<String> data) {
        return data.stream()
                .mapToLong(bank -> findMaximumJoltage(bank, 12))
                .sum();
    }

    private static long findMaximumJoltage(final String bank) {
        return findMaximumJoltage(bank, 2);
    }

    private static long findMaximumJoltage(final String bank, final int batteries) {
        final StringBuilder stringBuilder = new StringBuilder();
        String remainingBank = bank;
        for (int i = 0; i < batteries; i++) {
            final int endPos = i == batteries - 1 ? remainingBank.length() : remainingBank.length() - (batteries - i - 1);
            final int highestDigit = findHighestNumber(remainingBank.substring(0, endPos));
            final int leftPos = remainingBank.indexOf(String.valueOf(highestDigit)) + 1;
            remainingBank = remainingBank.substring(leftPos);
            stringBuilder.append(highestDigit);
        }

        return Long.parseLong(stringBuilder.toString());
    }

    private static int findHighestNumber(final String bank) {
        return bank.chars()
                .map(Character::getNumericValue)
                .max()
                .orElseThrow(() -> new IllegalArgumentException("Could not find highest number in " + bank));
    }

    static void main() {
        new PowerBank();
    }
}
