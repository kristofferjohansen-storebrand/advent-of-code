package no.krazyglitch.aoc2024.day13;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class ClawContraption {
    private static final Pattern BUTTON_PATTERN = Pattern.compile("Button .: X\\+(\\d+), Y\\+(\\d+)");
    private static final Pattern PRIZE_PATTERN = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)");

    public ClawContraption() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The least amount of tokens required for all possible prizes is %d tokens\n", getLeastRequiredTokens(data, false));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The least amount of tokens required for all possible prizes when adding 10000000000000 to each prize location is %d tokens\n", getLeastRequiredTokens(data, true));
            System.out.printf("Part two took %d ms", getMillisSince(start));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static long getLeastRequiredTokens(final List<String> data, final boolean add10000000000000) {
        final List<ClawMachine> clawMachines = parseData(data, add10000000000000);

        return clawMachines.stream()
                .map(ClawContraption::findLowestTokenCost)
                .filter(Objects::nonNull)
                .mapToLong(value -> value)
                .sum();
    }

    private static Long findLowestTokenCost(final ClawMachine clawMachine) {
        final Button buttonA = clawMachine.buttonA();
        final Button buttonB = clawMachine.buttonB();
        final Prize prize = clawMachine.prizeLocation();

        final long aPresses = (prize.x() * buttonB.y() - buttonB.x() * prize.y()) / (buttonA.x() * buttonB.y() - buttonB.x() * buttonA.y());
        final long bPresses = (prize.x() * buttonA.y() - buttonA.x() * prize.y()) / (buttonB.x() * buttonA.y() - buttonA.x() * buttonB.y());

        return isEquationValid(clawMachine, aPresses, bPresses) ? buttonA.cost() * aPresses + buttonB.cost() * bPresses : null;
    }

    private static boolean isEquationValid(final ClawMachine clawMachine, final long a, final long b) {
        final Button buttonA = clawMachine.buttonA();
        final Button buttonB = clawMachine.buttonB();
        final Prize prize = clawMachine.prizeLocation();

        return prize.x() == buttonA.x() * a + buttonB.x() * b && prize.y() == buttonA.y() * a + buttonB.y() * b;
    }

    private static long getMaxValidPresses(final Button button, final Prize prize, final long maxPresses) {
        final long maxXPresses = Math.floorDiv(prize.x(), button.x());
        final long maxYPresses = Math.floorDiv(prize.y(), button.y());
        final long maxButtonPresses = Math.max(maxXPresses, maxYPresses);

        return Math.min(maxButtonPresses, maxPresses);
    }

    private static List<ClawMachine> parseData(final List<String> data, final boolean add10000000000000) {
        final List<ClawMachine> clawMachines = new ArrayList<>();
        for (int i = 0; i < data.size(); i += 4) {
            clawMachines.add(new ClawMachine(parseButton(data.get(i), 3), parseButton(data.get(i + 1), 1), parsePrize(data.get(i + 2), add10000000000000)));
        }

        return clawMachines;
    }

    private static Button parseButton(final String line, final int tokenCost) {
        final Matcher matcher = BUTTON_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid button line: " + line);
        }

        return new Button(Long.parseLong(matcher.group(1)), Long.parseLong(matcher.group(2)), tokenCost);
    }

    private static Prize parsePrize(final String line, final boolean add10000000000000) {
        final Matcher matcher = PRIZE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid prize line: " + line);
        }

        final long xPos = Long.parseLong(matcher.group(1));
        final long yPos = Long.parseLong(matcher.group(2));
        
        return new Prize(add10000000000000 ? xPos + 10_000_000_000_000L : xPos, add10000000000000 ? yPos + 10_000_000_000_000L : yPos);
    }

    private record Button(long x, long y, long cost) {
    }

    private record Prize(long x, long y) {
    }

    private record ClawMachine(Button buttonA, Button buttonB, Prize prizeLocation) {
    }

    public static void main(final String[] args) {
        new ClawContraption();
    }
}
