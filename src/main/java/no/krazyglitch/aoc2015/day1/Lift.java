package no.krazyglitch.aoc2015.day1;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Lift {

    public Lift() {
        try {
            final String data = FileUtil.readInputFile(this.getClass()).getFirst();
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("Santa ends up on floor %d\n", findFloor(data));
            System.out.printf("Part one took %d ms\n", ChronoUnit.MILLIS.between(start, LocalDateTime.now()));

            start = LocalDateTime.now();
            System.out.printf("Santa ends up in the basement in position %d\n", findBasement(data.toCharArray(), 0, 0, 0));
            System.out.printf("Part two took %d ms", ChronoUnit.MILLIS.between(start, LocalDateTime.now()));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static int findFloor(final String input) {

        return input.chars().map(parenthesis -> moveFloor((char) parenthesis)).sum();
    }

    public static int findBasement(final char[] data, final int index, final int upMoves, final int downMoves) {
        if (index == data.length-1) {
            return index+1;
        }

        final int move = moveFloor(data[index]);
        final int nextUpMoves = move > 0 ? upMoves+1 : upMoves;
        final int nextDownMoves = move < 0 ? downMoves+1 : downMoves;

        if (nextDownMoves > nextUpMoves) {
            return index+1;
        }

        return findBasement(data, index+1, nextUpMoves, nextDownMoves);
    }

    private static int moveFloor(final char data) {
        return data == '(' ? 1 : -1;
    }

    public static void main(String[] args) {
        new Lift();
    }
}
