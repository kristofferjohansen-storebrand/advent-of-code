package no.krazyglitch.aoc2015.day6;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class LightShow {
    private static final Pattern COMMAND_PATTERN = Pattern.compile("([\\w ]+) (\\d+,\\d+) through (\\d+,\\d+)");

    public LightShow() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("After processing, %d lights are lit\n", processInstructions(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("After processing the disco lights, the total brightness is %d\n", processDiscoInstructions(data));
            System.out.printf("Part two took %d ms\n\n", getMillisSince(start));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static int processInstructions(final List<String> data) {
        final boolean[][] grid = new boolean[1000][1000];
        data.forEach(input -> readInstruction(input, grid));

        return (int) Arrays.stream(grid)
                .flatMap(row -> IntStream.range(0, row.length)
                        .mapToObj(index -> row[index]))
                .filter(Boolean::booleanValue)
                .count();
    }

    private static void readInstruction(final String data, final boolean[][] grid) {
        final Instruction instruction = getInstruction(data);
        if (instruction == null) return;
        processInstruction(instruction, grid);
    }

    private static void processInstruction(final Instruction instruction, final boolean[][] grid) {
        for (int x = instruction.getXStart(); x <= instruction.getXEnd(); x++) {
            for (int y = instruction.getYStart(); y <= instruction.getYEnd(); y++) {
                switch (instruction.getCommand()) {
                    case ON:
                        grid[x][y] = true;
                        break;
                    case OFF:
                        grid[x][y] = false;
                        break;
                    case TOGGLE:
                        grid[x][y] = !grid[x][y];
                        break;
                }
            }
        }
    }

    public static int processDiscoInstructions(final List<String> data) {
        final int[][] grid = new int[1000][1000];
        data.forEach(input -> readInstruction(input, grid));

        return Arrays.stream(grid).flatMapToInt(Arrays::stream).sum();
    }

    private static void readInstruction(final String data, final int[][] grid) {
        final Instruction instruction = getInstruction(data);
        if (instruction == null) return;
        processInstruction(instruction, grid);
    }

    private static void processInstruction(final Instruction instruction, final int[][] grid) {
        for (int x = instruction.getXStart(); x <= instruction.getXEnd(); x++) {
            for (int y = instruction.getYStart(); y <= instruction.getYEnd(); y++) {
                switch (instruction.getCommand()) {
                    case ON:
                        grid[x][y]++;
                        break;
                    case OFF:
                        if (grid[x][y] > 0) {
                            grid[x][y]--;
                        }
                        break;
                    case TOGGLE:
                        grid[x][y] += 2;
                        break;
                }
            }
        }
    }

    private static Instruction getInstruction(final String data) {
        final Matcher matcher = COMMAND_PATTERN.matcher(data);
        if (!matcher.matches()) {
            System.out.println("Instruction didn't match regex: " + data);
            return null;
        }

        final Instruction instruction = new Instruction(new String[]{matcher.group(1), matcher.group(2), matcher.group(3)});
        return instruction;
    }

    public static void main(final String[] args) {
        new LightShow();
    }
}

class Instruction {
    enum Command {
        ON("turn on"), OFF("turn off"), TOGGLE("toggle");

        private final String val;

        Command(final String val) {
            this.val = val;
        }

        static Command fromString(final String input) {
            for (final Command command : Command.values()) {
                if (command.val.equals(input)) {
                    return command;
                }
            }

            throw new IllegalArgumentException("No command was found for the input " + input);
        }
        }

    private final int xStart;
    private final int xEnd;
    private final int yStart;
    private final int yEnd;
    private final Command command;

    public Instruction(final String[] components) {
        this.command = Command.fromString(components[0]);

        final String[] startComponents = components[1].split(",");
        final String[] endComponents = components[2].split(",");

        this.xStart = Integer.parseInt(startComponents[0]);
        this.yStart = Integer.parseInt(startComponents[1]);

        this.xEnd = Integer.parseInt(endComponents[0]);
        this.yEnd = Integer.parseInt(endComponents[1]);
    }

    public int getXStart() {
        return xStart;
    }

    public int getXEnd() {
        return xEnd;
    }

    public int getYStart() {
        return yStart;
    }

    public int getYEnd() {
        return yEnd;
    }

    public Command getCommand() {
        return command;
    }
}
