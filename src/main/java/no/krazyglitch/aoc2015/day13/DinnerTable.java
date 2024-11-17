package no.krazyglitch.aoc2015.day13;

import no.krazyglitch.util.FileUtil;
import no.krazyglitch.util.ListUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class DinnerTable {

    private final static Pattern GUEST_PATTERN = Pattern.compile("(\\w+) would (\\w+) (\\d+) happiness units by sitting next to (\\w+)\\.");

    public DinnerTable() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The optimal seating arrangement would result in a total happiness of %d\n", getOptimalSeatingArrangement(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The optimal seating arrangement with me would result in a total happiness of %d\n", getOptimalSeatingArrangementWitMe(data));
            System.out.printf("Part two took %d ms", getMillisSince(start));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static int getOptimalSeatingArrangement(final List<String> data) {
        final Map<String, Guest> guestList = parseGuests(data);
        final List<List<Guest>> guestPermutations = ListUtil.createPermutations(new ArrayList<>(guestList.values()));

        return guestPermutations.stream()
                .mapToInt(DinnerTable::calculateSeatingArrangement)
                .max()
                .orElseThrow(() -> new RuntimeException("Could not calculate seating arrangement"));
    }

    public static int getOptimalSeatingArrangementWitMe(final List<String> data) {
        final Map<String, Guest> guestList = parseGuests(data);
        guestList.put("Me", new Guest("Me"));

        final List<List<Guest>> guestPermutations = ListUtil.createPermutations(new ArrayList<>(guestList.values()));
        return guestPermutations.stream()
                .mapToInt(DinnerTable::calculateSeatingArrangement)
                .max()
                .orElseThrow(() -> new RuntimeException("Could not calculate seating arrangement"));
    }

    private static int calculateSeatingArrangement(final List<Guest> arrangement) {
        int sum = 0;
        final int guestListSize = arrangement.size();

        for (int i = 0; i < guestListSize; i++) {
            final Guest guest = arrangement.get(i);
            final Guest leftGuest = arrangement.get(ListUtil.getWrappedIndex(i - 1, arrangement));
            final Guest rightGuest = arrangement.get(ListUtil.getWrappedIndex(i + 1, arrangement));

            sum += guest.getHappiness(leftGuest.getName());
            sum += guest.getHappiness(rightGuest.getName());
        }

        return sum;
    }



    private static Map<String, Guest> parseGuests(final List<String> data) {
        final Map<String, Guest> guestList = new HashMap<>();
        data.forEach(line -> parseLine(line, guestList));

        return guestList;
    }

    private static void parseLine(final String data, final Map<String, Guest> guests) {
        final Matcher matcher = GUEST_PATTERN.matcher(data);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Guest input line is invalid: " + data);
        }

        final String guestName = matcher.group(1);
        final Guest guest = guests.computeIfAbsent(guestName, Guest::new);

        final boolean gainsHappiness = "gain".equals(matcher.group(2));
        final int happinessChange = Integer.parseInt(matcher.group(3));
        final String otherGuest = matcher.group(4);

        guest.addNeighbor(otherGuest, gainsHappiness ? happinessChange : -happinessChange);
    }

    public static void main(final String[] args) {
        new DinnerTable();
    }
}

class Guest {
    private final String name;
    private final Map<String, Integer> happinessMap;

    public Guest(final String name) {
        this.name = name;
        this.happinessMap = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public int getHappiness(final String name) {
        return happinessMap.getOrDefault(name, 0);
    }

    public void addNeighbor(final String name, final int happinessChange) {
        happinessMap.put(name, happinessChange);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Guest guest = (Guest) o;
        return Objects.equals(name, guest.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}