package no.krazyglitch.aoc2024.day14;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class RestRoom {

    private static final Pattern ROBOT_PATTERN = Pattern.compile("p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)");

    public RestRoom() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The safety vector is %d\n", getSafetyVector(data, 100, 101, 103));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The time it took for the robots to shape a Christmas tree is %d seconds\n", findEasterEggTime(data, 101, 103));
            System.out.printf("Part two took %d ms", getMillisSince(start));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static int getSafetyVector(final List<String> data, final int seconds, final int width, final int height) {
        final List<Robot> robots = parseRobots(data);
        final List<Robot> movedRobots = moveRobots(robots, seconds, width, height);
        final List<List<Robot>> quadrants = getRobotsInQuadrants(movedRobots, width, height);

        return quadrants.stream()
                .mapToInt(List::size)
                .reduce(1, (a, b) -> a * b);
    }

    public static int findEasterEggTime(final List<String> data, final int width, final int height) {
        final List<Robot> robots = parseRobots(data);
        int elapsedTime = 0;
        while (!noRobotsOverlap(robots)) {
            moveRobots(robots, width, height);
            elapsedTime++;
        }

        printMap(robots, width, height);
        return elapsedTime;
    }

    private static boolean noRobotsOverlap(final List<Robot> robots) {
        final Set<Robot.Coordinate> coordinates = new HashSet<>();
        for (final Robot robot : robots) {
            final Robot.Coordinate coordinate = robot.getCoordinate();
            if (coordinates.contains(coordinate)) {
                return false;
            }

            coordinates.add(coordinate);
        }

        return true;
    }

    private static void printMap(final List<Robot> robots, final int width, final int height) {
        final char[][] map = new char[height][width];
        robots.forEach(robot -> map[robot.getY()][robot.getX()] = 'X');

        for (final char[] row : map) {
            for (final char cell : row) {
                System.out.print(cell == 'X' ? 'X' : ' ');
            }
            System.out.println();
        }
    }

    private static void moveRobots(final List<Robot> robots, final int width, final int height) {
        robots.forEach(robot -> moveRobot(robot, width, height));
    }

    private static void moveRobot(final Robot robot, final int width, final int height) {
        robot.setX(Math.floorMod(robot.getX() + robot.getMoveX(), width));
        robot.setY(Math.floorMod(robot.getY() + robot.getMoveY(), height));
    }

    private static List<Robot> moveRobots(final List<Robot> robots, final int seconds, final int width, final int height) {
        return robots.stream()
                .map(robot -> moveRobot(robot, seconds, width, height))
                .toList();
    }

    private static Robot moveRobot(final Robot robot, final int seconds, final int maxX, final int maxY) {
        return new Robot(Math.floorMod(robot.getX() + (robot.getMoveX() * seconds), maxX),
                Math.floorMod(robot.getY() + (robot.getMoveY() * seconds), maxY),
                robot.getMoveX(),
                robot.getMoveY());
    }

    private static List<List<Robot>> getRobotsInQuadrants(final List<Robot> robots, final int width, final int height) {
        final List<Robot> northWest = new ArrayList<>();
        final List<Robot> northEast = new ArrayList<>();
        final List<Robot> southWest = new ArrayList<>();
        final List<Robot> southEast = new ArrayList<>();

        final int middleWidth = width / 2;
        final int middleHeight = height / 2;

        robots.stream()
                .filter(robot -> robot.getX() != middleWidth && robot.getY() != middleHeight)
                .forEach(robot -> {

                    if (robot.getY() < middleHeight) {
                        if (robot.getX() < middleWidth) {
                            northWest.add(robot);
                        } else {
                            northEast.add(robot);
                        }
                    } else {
                        if (robot.getX() < middleWidth) {
                            southWest.add(robot);
                        } else {
                            southEast.add(robot);
                        }
                    }
                });

        return List.of(northWest, northEast, southWest, southEast);
    }

    private static List<Robot> parseRobots(final List<String> data) {
        return data.stream()
                .map(RestRoom::parseRobot)
                .toList();
    }

    private static Robot parseRobot(final String input) {
        final Matcher matcher = ROBOT_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Robot could not be parsed from: " + input);
        }

        return new Robot(Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)),
                Integer.parseInt(matcher.group(4)));
    }

    public static void main(final String[] args) {
        new RestRoom();
    }
}

class Robot {
    private int x;
    private int y;
    private final int moveX;
    private final int moveY;

    public Robot(final int x, final int y, final int moveX, final int moveY) {
        this.x = x;
        this.y = y;
        this.moveX = moveX;
        this.moveY = moveY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getMoveX() {
        return moveX;
    }

    public int getMoveY() {
        return moveY;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public Coordinate getCoordinate() {
        return new Coordinate(x, y);
    }

    record Coordinate(int x, int y) {
    }

    @Override
    public String toString() {
        return "Robot{" +
                "x=" + x +
                ", y=" + y +
                ", moveX=" + moveX +
                ", moveY=" + moveY +
                '}';
    }
}