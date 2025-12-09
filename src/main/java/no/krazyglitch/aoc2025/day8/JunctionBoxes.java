package no.krazyglitch.aoc2025.day8;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.SequencedMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class JunctionBoxes {

    private static final Comparator<Circuit> CIRCUIT_COMPARATOR = Comparator.comparing(Circuit::getCircuitSize);
    private static final Comparator<Connection> CONNECTION_COMPARATOR = Comparator.comparing(Connection::getLength);

    public JunctionBoxes() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The product of the three largest circuits is %d%n", calculateCircuitsNewer(data, 1000));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The product of the two last X coordinates is %d%n", frazzled(data));
            System.out.printf("Part two took %d ms\n\n", getMillisSince(start));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static long calculateCircuitsNew(final List<String> data, final int connections) {
        final List<Coordinate> coordinates = parseJunctionBoxes(data);
        final List<Circuit> circuits = coordinates.stream()
                .map(Circuit::new)
                .collect(Collectors.toCollection(ArrayList::new));

        for (int i = 0; i < connections-1; i++) {
            fuckAroundAndFindOut(circuits);
        }

        final List<Circuit> sortedCircuits = new ArrayList<>(circuits);
        sortedCircuits.sort(CIRCUIT_COMPARATOR.reversed());

        return sortedCircuits.stream()
                .limit(3)
                .mapToInt(Circuit::getCircuitSize)
                .mapToLong(Long::valueOf)
                .reduce(1L, (a, b) -> a * b);
    }

    public static long calculateCircuitsNewer(final List<String> data, final int connectionAmount) {
        final List<Coordinate> coordinates = parseJunctionBoxes(data);
        final List<Circuit> circuits = coordinates.stream()
                .map(Circuit::new)
                .collect(Collectors.toCollection(ArrayList::new));
        final Map<Coordinate, Set<Coordinate>> existingConnections = new HashMap<>();
        final List<Connection> connections = new ArrayList<>();

        for (final Coordinate source : coordinates) {
            final Set<Coordinate> sourceConnections = existingConnections.computeIfAbsent(source, c -> new HashSet<>());
            for (final Coordinate target : coordinates) {
                if (source.equals(target) || sourceConnections.contains(target)) {
                    continue;
                }

                connections.add(new Connection(source, target));
                final Set<Coordinate> targetConnections = existingConnections.computeIfAbsent(target, c -> new HashSet<>());
                targetConnections.add(source);
            }
        }

        final List<Connection> sortedConnections = new ArrayList<>(connections);
        sortedConnections.sort(CONNECTION_COMPARATOR);

        for (int i = 0; i < connectionAmount; i++) {
            electricBoogaloo(circuits, sortedConnections.get(i));
        }

        final List<Circuit> sortedCircuits = new ArrayList<>(circuits);
        sortedCircuits.sort(CIRCUIT_COMPARATOR.reversed());

        return sortedCircuits.stream()
                .limit(3)
                .mapToInt(Circuit::getCircuitSize)
                .mapToLong(Long::valueOf)
                .reduce(1L, (a, b) -> a * b);
    }

    public static long frazzled(final List<String> data) {
        final List<Coordinate> coordinates = parseJunctionBoxes(data);
        final List<Circuit> circuits = coordinates.stream()
                .map(Circuit::new)
                .collect(Collectors.toCollection(ArrayList::new));
        final Map<Coordinate, Set<Coordinate>> existingConnections = new HashMap<>();
        final List<Connection> connections = new ArrayList<>();

        for (final Coordinate source : coordinates) {
            final Set<Coordinate> sourceConnections = existingConnections.computeIfAbsent(source, c -> new HashSet<>());
            for (final Coordinate target : coordinates) {
                if (source.equals(target) || sourceConnections.contains(target)) {
                    continue;
                }

                connections.add(new Connection(source, target));
                final Set<Coordinate> targetConnections = existingConnections.computeIfAbsent(target, c -> new HashSet<>());
                targetConnections.add(source);
            }
        }

        final List<Connection> sortedConnections = new ArrayList<>(connections);
        sortedConnections.sort(CONNECTION_COMPARATOR);

        int iteration = 0;
        while(circuits.size() > 1) {
            electricBoogaloo(circuits, sortedConnections.get(iteration));
            iteration++;
        }

        final Connection lastConnection = sortedConnections.get(iteration-1);
        return (long) lastConnection.getSource().getX() * lastConnection.getTarget().getX();
    }

    private static void electricBoogaloo(final List<Circuit> circuits, final Connection connection) {
        final Circuit sourceCircuit = getCircuit(circuits, connection.getSource());
        final Circuit targetCircuit = getCircuit(circuits, connection.getTarget());

        if (sourceCircuit.equals(targetCircuit)) {
            return;
        }

        //System.out.printf("Merging %s%n", connection);

        final Circuit mergedCircuit = mergeCircuits(sourceCircuit, targetCircuit);
        circuits.remove(sourceCircuit);
        circuits.remove(targetCircuit);
        circuits.add(mergedCircuit);
    }

    private static Circuit getCircuit(final List<Circuit> circuits, final Coordinate coordinate) {
        return circuits.stream()
                .filter(circuit -> circuit.contains(coordinate))
                .findFirst()
                .orElseThrow();
    }

    private static void fuckAroundAndFindOut(final List<Circuit> circuits) {
        double minDistance = Double.MAX_VALUE;
        Circuit closestInner = null;
        Circuit closestOuter = null;
        for (final Circuit outer : circuits) {
            for (final Circuit inner : circuits) {
                if (outer.equals(inner)) {
                    continue;
                }

                final double distance = getMinimumDistance(outer, inner);
                if (distance < minDistance) {
                    closestOuter = outer;
                    closestInner = inner;
                    minDistance = distance;
                }
            }
        }

        if (closestOuter == null || closestInner == null || closestInner.equals(closestOuter)) {
            throw new IllegalStateException(String.format("closestOuter: [%s], closestInner: [%s]", closestOuter, closestInner));
        }

        final Circuit mergedCircuit = mergeCircuits(closestInner, closestOuter);
        circuits.remove(closestInner);
        circuits.remove(closestOuter);
        circuits.add(mergedCircuit);
    }

    private static Circuit mergeCircuits(final Circuit source, final Circuit target) {
        //System.out.printf("Merging %s with %s%n", source, target);
        final List<Coordinate> mergedCircuits = Stream.concat(source.getCoordinates().stream(),
                target.getCoordinates().stream())
                .collect(Collectors.toCollection(ArrayList::new));
        return new Circuit(mergedCircuits);
    }

    private static double getMinimumDistance(final Circuit source, final Circuit target) {
        double minDistance = Double.MAX_VALUE;
        for (final Coordinate sourceCoordinate : source.getCoordinates()) {
            for (final Coordinate targetCoordinate : target.getCoordinates()) {
                final double distance = sourceCoordinate.distanceTo(targetCoordinate);
                if (distance < minDistance) {
                    minDistance = distance;
                    //System.out.printf("Smallest distance was between %s and %s%n", source, target);
                }
            }
        }

        return minDistance;
    }

    public static long calculateCircuits(final List<String> data, final int connections) {
        final List<Coordinate> junctionBoxes = parseJunctionBoxes(data);
        final SequencedMap<Double, List<Coordinate>> distanceMap = new TreeMap<>();
        final Map<Coordinate, Set<Coordinate>> seenCoordinatesMap = new HashMap<>();
        final Set<Coordinate> addedCoordinates = new HashSet<>();

        for (final Coordinate outer : junctionBoxes) {
            for (final Coordinate inner : junctionBoxes) {
                if (outer.equals(inner)) {
                    continue;
                }

                final Set<Coordinate> innerSeenCoordinates = seenCoordinatesMap.computeIfAbsent(inner, c -> new HashSet<>());
                if (innerSeenCoordinates.contains(outer)) {
                    continue;
                }

                distanceMap.put(outer.distanceTo(inner), List.of(outer, inner));
                final Set<Coordinate> outerSeenCoordinates = seenCoordinatesMap.computeIfAbsent(outer, c -> new HashSet<>());
                outerSeenCoordinates.add(inner);
                innerSeenCoordinates.add(outer);
            }
        }

        final List<Circuit> circuits = new ArrayList<>();
        //distanceMap.sequencedValues().forEach(coordinates -> createOrAddToCircuit(coordinates, circuits));
        //junctionBoxes.forEach(coordinate -> createOrAddToCircuit(coordinate, circuits));
        int createdConnections = 0;
        for (final List<Coordinate> coordinates : distanceMap.sequencedValues()) {
            if (createdConnections == connections) {
                break;
            }

            if (addedCoordinates.containsAll(coordinates)) {
                continue;
            }

            addedCoordinates.addAll(coordinates);
            createOrAddToCircuit(coordinates, circuits);
            createdConnections++;
        }

        circuits.sort(CIRCUIT_COMPARATOR.reversed());

        return circuits.stream()
                .limit(3)
                .mapToInt(Circuit::getCircuitSize)
                .mapToLong(Long::valueOf)
                .reduce(1L, (a, b) -> a * b);
    }

    private static void addNeighbors(final Coordinate outer,
                                     final Coordinate inner,
                                     final TreeMap<Double, Map<Coordinate, Coordinate>> distanceMap,
                                     final Map<Coordinate, Set<Coordinate>> seenCoordinatesMap) {

    }

    private static void createOrAddToCircuit(final List<Coordinate> coordinates, final List<Circuit> circuits) {
        final Optional<Circuit> existingCircuit = findCircuitContainingCoordinates(circuits, coordinates);

        if (existingCircuit.isEmpty()) {
            circuits.add(new Circuit(coordinates));
        } else {
            existingCircuit.get().addJunctionBoxes(coordinates);
        }
    }

    private static Optional<Circuit> findCircuitContainingCoordinates(final List<Circuit> circuits, final List<Coordinate> coordinates) {
        for (final Coordinate coordinate : coordinates) {
            for (final Circuit circuit : circuits) {
                if (circuit.contains(coordinate)) {
                    return Optional.of(circuit);
                }
            }
        }

        return Optional.empty();
    }

    private static List<Coordinate> parseJunctionBoxes(final List<String> data) {
        return data.stream()
                .map(line -> line.split(","))
                .map(splitString -> Arrays.stream(splitString).mapToInt(Integer::parseInt).toArray())
                .map(splitInts -> new Coordinate(splitInts[0], splitInts[1], splitInts[2]))
                .toList();
    }

    static class Coordinate {
        private final TreeMap<Double, Coordinate> distanceMap = new TreeMap<>();
        private final Set<Coordinate> neighbors = new HashSet<>();
        private final int x;
        private final int y;
        private final int z;

        public Coordinate(final int x, final int y, final int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double distanceTo(final Coordinate target) {
            return Math.sqrt(Math.pow((target.getX() - x), 2) + Math.pow(target.getY() - y, 2) + Math.pow(target.getZ() - z, 2));
        }

        public void addNeighbor(final Coordinate target) {
            neighbors.add(target);
            distanceMap.put(distanceTo(target), target);
        }

        public boolean hasSeenNeighbor(final Coordinate target) {
            return neighbors.contains(target);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        @Override
        public String toString() {
            return "Coordinate{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }
    }

    static class Connection {
        private final Coordinate source;
        private final Coordinate target;
        private final double length;

        public Connection(final Coordinate source, final Coordinate target) {
            this.source = source;
            this.target = target;
            this.length = calculateLength(source, target);
        }

        private static double calculateLength(final Coordinate source, final Coordinate target) {
            return Math.sqrt(Math.pow((target.getX() - source.getX()), 2) +
                    Math.pow(target.getY() - source.getY(), 2) +
                    Math.pow(target.getZ() - source.getZ(), 2));
        }

        public double getLength() {
            return length;
        }

        public Coordinate getTarget() {
            return target;
        }

        public Coordinate getSource() {
            return source;
        }

        @Override
        public boolean equals(final Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            final Connection that = (Connection) o;
            return Objects.equals(source, that.source) && Objects.equals(target, that.target);
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, target);
        }

        @Override
        public String toString() {
            return "Connection{" +
                    "source=" + source +
                    ", target=" + target +
                    '}';
        }
    }

    static class Circuit {
        private final List<Coordinate> coordinates = new ArrayList<>();

        public boolean contains(final Coordinate coordinate) {
            return coordinates.contains(coordinate);
        }

        public void addJunctionBox(final Coordinate coordinate) {
            coordinates.add(coordinate);
        }

        private void addJunctionBoxes(final List<Coordinate> coordinates) {
            coordinates.forEach(this::addJunctionBox);
        }

        public List<Coordinate> getCoordinates() {
            return this.coordinates;
        }

        public int getCircuitSize() {
            return coordinates.size();
        }

        public Circuit(final List<Coordinate> coordinates) {
            addJunctionBoxes(coordinates);
        }

        public Circuit(final Coordinate coordinate) {
            addJunctionBox(coordinate);
        }

        @Override
        public String toString() {
            return "Circuit{" +
                    "junctionBoxes=" + coordinates +
                    '}';
        }
    }

    static void main() {
        new JunctionBoxes();
    }
}
