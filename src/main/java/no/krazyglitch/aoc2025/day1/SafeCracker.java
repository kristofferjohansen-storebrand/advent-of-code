package no.krazyglitch.aoc2025.day1;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class SafeCracker {

    private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("^(\\w)(\\d+)");

    public SafeCracker() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The amount of times zero was encountered is %d%n", getTimesEncountered(new Safe(50), data, 0));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The amount of times zero was encountered is %d%n", getTimesEncounteredZero(new Safe(50), data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static int getTimesEncountered(final Safe originalSafe, final List<String> data, final int targetValue) {
        int count = 0;
        final List<Instruction> instructions = data.stream()
                .map(SafeCracker::parseInstruction)
                .toList();

        Safe safe = originalSafe;
        for (final Instruction instruction : instructions) {
            safe = turnDial(safe, instruction);
            if (safe.position() == targetValue) {
                count++;
            }
        }

        return count;
    }

    public static int getTimesEncounteredZero(final Safe originalSafe, final List<String> data) {
        int count = 0;
        final List<Instruction> instructions = data.stream()
                .map(SafeCracker::parseInstruction)
                .toList();

        Safe safe = originalSafe;
        for (final Instruction instruction : instructions) {
            final int newTotalPosition = getTotalPosition(safe, instruction);
            count += getTimesPassedZero(newTotalPosition, safe.position());

            safe = turnDial(safe, instruction);
        }

        return count;
    }

    private static int getTimesPassedZero(final int totalPosition, final int lastPosition) {
        final boolean changedSign = lastPosition != 0 && Math.signum(totalPosition) != Math.signum(lastPosition);

        final int diffRotations = Math.abs(totalPosition) / 100;
        return changedSign ? diffRotations + 1 : diffRotations;
    }

    private static Instruction parseInstruction(final String data) {
        final Matcher matcher = INSTRUCTION_PATTERN.matcher(data);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid instruction line: " + data);
        }

        return new Instruction("R".equals(matcher.group(1)), Integer.parseInt(matcher.group(2)));
    }

    private static Safe turnDial(final Safe safe, final Instruction instruction) {
        final int multiplier = instruction.clockwise() ? 1 : -1;
        final int newPosition = ((instruction.turns() * multiplier) + safe.position()) % 100;
        return new Safe(newPosition);
    }

    private static int getTotalPosition(final Safe safe, final Instruction instruction) {
        final int multiplier = instruction.clockwise() ? 1 : -1;
        return ((instruction.turns()) * multiplier) + safe.position();
    }

    public static void main(final String[] args) {
        new SafeCracker();
    }

    public record Safe(int position) {
    }

    private record Instruction(boolean clockwise, int turns) {
    }
}
