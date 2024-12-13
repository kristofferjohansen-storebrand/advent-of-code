package no.krazyglitch.aoc2024.day8;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class Resonance {

    public Resonance() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The unique number of anti-nodes within the grid is %d\n", getAntiNodeCount(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The unique number of anti-nodes within the grid is %d when the range is infinite\n", getAntiNodeCountWithInfiniteRange(data));
            System.out.printf("Part two took %d ms", getMillisSince(start));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static int getAntiNodeCount(final List<String> data) {
        final Map<Character, List<Antenna>> antennaMap = parseAntennas(data);
        final int gridHeight = data.size();
        final int gridWidth = data.getFirst().length();
        final Set<AntiNode> antiNodes = generateAntiNodes(antennaMap);

        return (int) antiNodes.stream()
                .filter(antiNode -> isAntiNodeValid(antiNode, gridHeight, gridWidth))
                .map(AntiNode::getCoordinate)
                .distinct()
                .count();
    }

    public static int getAntiNodeCountWithInfiniteRange(final List<String> data) {
        final Map<Character, List<Antenna>> antennaMap = parseAntennas(data);
        final int gridHeight = data.size();
        final int gridWidth = data.getFirst().length();
        final Set<AntiNode> antiNodes = generateInfiniteAntiNodes(antennaMap, gridHeight, gridWidth);

        return (int) antiNodes.stream()
                .filter(antiNode -> isAntiNodeValid(antiNode, gridHeight, gridWidth))
                .map(AntiNode::getCoordinate)
                .distinct()
                .count();
    }

    private static boolean isAntiNodeValid(final AntiNode antiNode, final int gridHeight, final int gridWidth) {
        return antiNode.x() >= 0 && antiNode.x() < gridWidth && antiNode.y() >= 0 && antiNode.y() < gridHeight;
    }

    private static Set<AntiNode> generateAntiNodes(final Map<Character, List<Antenna>> antennaMap) {
        final Set<AntiNode> antiNodes = new HashSet<>();

        antennaMap.forEach((symbol, antennas) -> {
            for (int i = 0; i < antennas.size(); i++) {
                final Antenna outerAntenna = antennas.get(i);
                for (int j = i + 1; j < antennas.size(); j++) {
                    final Antenna innerAntenna = antennas.get(j);
                    final int xDiff = innerAntenna.x() - outerAntenna.x();
                    final int yDiff = innerAntenna.y() - outerAntenna.y();

                    antiNodes.add(new AntiNode(outerAntenna.symbol(), outerAntenna.x() - xDiff, outerAntenna.y() - yDiff));
                    antiNodes.add(new AntiNode(innerAntenna.symbol(), innerAntenna.x() + xDiff, innerAntenna.y() + yDiff));
                }
            }
        });

        return antiNodes;
    }

    private static Set<AntiNode> generateInfiniteAntiNodes(final Map<Character, List<Antenna>> antennaMap, final int gridHeight, final int gridWidth) {
        final Set<AntiNode> antiNodes = new HashSet<>();

        antennaMap.forEach((symbol, antennas) -> {
            for (int i = 0; i < antennas.size(); i++) {
                final Antenna outerAntenna = antennas.get(i);
                for (int j = i + 1; j < antennas.size(); j++) {
                    final Antenna innerAntenna = antennas.get(j);
                    final int xDiff = innerAntenna.x() - outerAntenna.x();
                    final int yDiff = innerAntenna.y() - outerAntenna.y();

                    generateAntiNodes(antiNodes, outerAntenna.symbol(), outerAntenna.x(), outerAntenna.y(), xDiff, yDiff, gridHeight, gridWidth);
                }
            }
        });


        return antiNodes;
    }

    // This method proves an astounding lack of knowledge about mathematics, but it works
    private static void generateAntiNodes(final Set<AntiNode> antiNodes,
                                          final char symbol,
                                          final int xStart,
                                          final int yStart,
                                          final int xDiff,
                                          final int yDiff,
                                          final int gridHeight,
                                          final int gridWidth) {
        final int xTimesToZero = xStart / Math.abs(xDiff);
        final int yTimesToZero = yStart / Math.abs(yDiff);
        final int xTimesToMax = (gridWidth - xStart) / Math.abs(xDiff);
        final int yTimesToMax = (gridHeight - yStart) / Math.abs(yDiff);

        final int xLowerBound = -Math.max(xTimesToZero, yTimesToZero) - 1;
        final int xUpperBound = Math.max(xTimesToMax, yTimesToMax) + 1;

        for (int i = xLowerBound; i <= xUpperBound; i++) {
            antiNodes.add(new AntiNode(symbol, xStart + xDiff * i, yStart + yDiff * i));
        }
    }

    private static void generateAntiNodes(final Set<AntiNode> antiNodes,
                                          final Antenna outerAntenna,
                                          final Antenna innerAntenna,
                                          final int xDiff,
                                          final int yDiff,
                                          final int lowerBound,
                                          final int upperBound) {
        for (int i = lowerBound; i <= upperBound; i++) {
            antiNodes.add(new AntiNode(outerAntenna.symbol(), outerAntenna.x() - xDiff * i, outerAntenna.y() - yDiff * i));
            antiNodes.add(new AntiNode(innerAntenna.symbol(), innerAntenna.x() + xDiff * i, innerAntenna.y() + yDiff * i));
        }
    }

    private static Map<Character, List<Antenna>> parseAntennas(final List<String> data) {
        final Map<Character, List<Antenna>> antennaMap = new HashMap<>();
        final int gridHeight = data.size();
        final int gridWidth = data.getFirst().length();

        for (int y = 0; y < gridHeight; y++) {
            final String line = data.get(y);
            for (int x = 0; x < gridWidth; x++) {
                final char symbol = line.charAt(x);
                if (symbol == '.') {
                    continue;
                }

                final Antenna antenna = new Antenna(symbol, x, y);
                antennaMap.compute(symbol, (key, value) -> {
                    if (value == null) {
                        value = new ArrayList<>();
                    }

                    value.add(antenna);
                    return value;
                });
            }
        }

        return antennaMap;
    }

    private record Coordinate(int x, int y) {
    }

    private record Antenna(char symbol, int x, int y) {
    }

    private record AntiNode(char symbol, int x, int y) {
        Coordinate getCoordinate() {
            return new Coordinate(x, y);
        }
    }

    public static void main(final String[] args) {
        new Resonance();
    }
}
