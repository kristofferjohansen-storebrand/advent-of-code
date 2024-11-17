package no.krazyglitch.aoc2015.day14;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class ReindeerOlympics {

    private static final Pattern REINDEER_PATTERN = Pattern.compile("(\\w+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.");

    public ReindeerOlympics() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The winning reindeer travelled %d km\n", calculateWinnerDistance(data, 2503));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The winning reindeer has %d points\n", calculateWinnerPoints(data, 2503));
            System.out.printf("Part two took %d ms", getMillisSince(start));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static int calculateWinnerDistance(final List<String> data,
                                              final int raceLength) {
        final List<Reindeer> reindeerList = parseReindeer(data, raceLength);

        return reindeerList.stream()
                .mapToInt(reindeer -> reindeer.getDistanceTravelled(raceLength))
                .max()
                .orElseThrow(() -> new RuntimeException("Could not calculate race winner"));
    }

    public static int calculateWinnerPoints(final List<String> data,
                                            final int raceLength) {
        final List<Reindeer> reindeerList = parseReindeer(data, raceLength);
        reindeerList.forEach(Reindeer::generateRaceTimes);

        IntStream.range(1, raceLength).forEach(second -> awardPoints(reindeerList, second));
        return reindeerList.stream()
                .mapToInt(Reindeer::getPoints)
                .max()
                .orElseThrow(() -> new RuntimeException("Could not calculate the winner by points"));
    }

    private static void awardPoints(final List<Reindeer> reindeerList, final int second) {
        final int leadingPoints = reindeerList.stream()
                .mapToInt(reindeer -> reindeer.getDistanceAtSecond(second))
                .max()
                .orElseThrow(() -> new RuntimeException("Could not get leading reindeer at second " + second));

        reindeerList.stream()
                .filter(reindeer -> reindeer.getDistanceAtSecond(second) == leadingPoints)
                .forEach(Reindeer::awardPoint);
    }

    private static List<Reindeer> parseReindeer(final List<String> data, final int raceLength) {
        return data.stream()
                .map(line -> parseReindeer(line, raceLength))
                .toList();
    }

    private static Reindeer parseReindeer(final String data, final int raceLength) {
        final Matcher matcher = REINDEER_PATTERN.matcher(data);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Could not parse reindeer data: " + data);
        }

        final String name = matcher.group(1);
        final int speed = Integer.parseInt(matcher.group(2));
        final int stamina = Integer.parseInt(matcher.group(3));
        final int restingTime = Integer.parseInt(matcher.group(4));

        return new Reindeer(name, speed, stamina, restingTime, raceLength);
    }

    public static void main(final String[] args) {
        new ReindeerOlympics();
    }
}

class Reindeer {
    private final String name;
    private final int speed;
    private final int stamina;
    private final int setTime;
    private final int[] distanceArray;
    private int points;

    public Reindeer(final String name, final int speed, final int stamina, final int restingTime, final int raceLength) {
        this.name = name;
        this.speed = speed;
        this.stamina = stamina;
        this.setTime = stamina + restingTime;

        this.distanceArray = new int[raceLength];
    }

    public int getDistanceTravelled(final int elapsedTime) {
        final int setsTravelled = Math.floorDiv(elapsedTime, setTime);
        final int currentSetTime = elapsedTime - setTime * setsTravelled;
        final int realSetTravelTime = Math.min(currentSetTime, stamina);

        return (speed * stamina * setsTravelled) + (realSetTravelTime * speed);
    }

    public void generateRaceTimes() {
        IntStream.range(0, distanceArray.length)
                .forEach(time -> distanceArray[time] = getDistanceTravelled(time));
    }

    public void awardPoint() {
        points++;
    }

    public int getPoints() {
        return points;
    }

    public int getDistanceAtSecond(final int second) {
        if (second > distanceArray.length) {
            throw new IllegalArgumentException("Can't get distance at second " + second + " because the highest calculated second is " + distanceArray.length);
        }

        return distanceArray[second];
    }

    @Override
    public String toString() {
        return "Reindeer{" +
                "name='" + name + '\'' +
                ", speed=" + speed +
                ", stamina=" + stamina +
                '}';
    }
}