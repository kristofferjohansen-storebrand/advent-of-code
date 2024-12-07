package no.krazyglitch.aoc2024.day7;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class BridgeRepair {

    private static final Pattern EQUATION_PATTERN = Pattern.compile("(\\d+): (.+)");

    public BridgeRepair() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The sum of all possible equations is %d\n", getPossibleEquations(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The sum of all possible equations with concatenation is %d\n", getPossibleConcatEquations(data));
            System.out.printf("Part two took %d ms", getMillisSince(start));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static long getPossibleEquations(final List<String> data) {
        return data.stream()
                .map(BridgeRepair::parseEquation)
                .filter(BridgeRepair::isEquationPossible)
                .mapToLong(Equation::result)
                .sum();
    }

    public static long getPossibleConcatEquations(final List<String> data) {
        return data.stream()
                .map(BridgeRepair::parseEquation)
                .filter(BridgeRepair::isConcatEquationPossible)
                .mapToLong(Equation::result)
                .sum();
    }

    private static boolean isEquationPossible(final Equation equation) {
        return isEquationPossible(equation.result(), equation.operands().getFirst(), equation.operands(), 1, false);
    }

    private static boolean isConcatEquationPossible(final Equation equation) {
        return isEquationPossible(equation.result(), equation.operands().getFirst(), equation.operands(), 1, true);
    }

    private static boolean isEquationPossible(final long target,
                                              final long currentValue,
                                              final List<Long> operands,
                                              final int iteration,
                                              final boolean allowConcatenation) {
        if (currentValue > target) {
            return false;
        }

        if (iteration == operands.size()) {
            return target == currentValue;
        }

        return isEquationPossible(target, currentValue + operands.get(iteration), operands, iteration + 1, allowConcatenation) ||
                isEquationPossible(target, currentValue * operands.get(iteration), operands, iteration + 1, allowConcatenation) ||
                (allowConcatenation && isEquationPossible(target, getConcatValue(currentValue, operands.get(iteration)), operands, iteration+1, allowConcatenation));
    }

    private static long getConcatValue(final long leftOperand, final long rightOperand) {
        return Long.parseLong(String.valueOf(leftOperand) + String.valueOf(rightOperand));
    }

    private static Equation parseEquation(final String line) {
        final Matcher matcher = EQUATION_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Equation could not be parsed from " + line);
        }

        final List<Long> operands = Arrays.stream(matcher.group(2).split(" "))
                .mapToLong(Long::parseLong)
                .boxed()
                .toList();

        return new Equation(Long.parseLong(matcher.group(1)), operands);
    }

    private record Equation(long result, List<Long> operands) {
    }

    public static void main(final String[] args) {
        new BridgeRepair();
    }
}
