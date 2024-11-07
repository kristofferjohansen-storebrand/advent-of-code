package no.krazyglitch.aoc2015.day7;

import no.krazyglitch.util.FileUtil;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class Circuitry {

    private final static Pattern WIRE_PATTERN = Pattern.compile("([\\w ]+) -> (\\w+)");

    public Circuitry() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("Wire a has the value %d\n", simulateWire(data, "a"));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("Wire a has the value %d after b is assigned the original a value\n", simulateOverriddenWire(data, "a", "b"));
            System.out.printf("Part two took %d ms", getMillisSince(start));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static int simulateWire(final List<String> data, final String targetLabel) {
        final Map<String, Wire> wireMap = createWires(data);

        return calculateValue(targetLabel, wireMap, new HashMap<>());
    }

    public static int simulateOverriddenWire(final List<String> data, final String targetLabel, final String overriddenLabel) {
        final Map<String, Wire> wireMap = createWires(data);
        char overrideValue = calculateValue(targetLabel, wireMap, new HashMap<>());
        wireMap.get(overriddenLabel).setValue(overrideValue);

        return calculateValue(targetLabel, wireMap, new HashMap<>());
    }

    private static Map<String, Wire> createWires(final List<String> data) {
        return data.stream()
                .map(Circuitry::parseWireInstruction)
                .collect(Collectors.toMap(Wire::getLabel, wire -> wire));
    }

    private static char calculateValue(final String wireLabel, final Map<String, Wire> wireMap, final Map<String, Character> wireValueMap) {
        if (wireValueMap.containsKey(wireLabel)) {
            return wireValueMap.get(wireLabel);
        }

        if (StringUtils.isNumeric(wireLabel)) {
            char value = (char) Integer.parseInt(wireLabel);
            wireValueMap.put(wireLabel, value);
            return value;
        }

        final Wire targetWire = wireMap.get(wireLabel);
        if (targetWire == null) {
            throw new RuntimeException("No wire found with label: " + wireLabel);
        }

        final char calculatedValue = switch (targetWire.getSignal()) {
            case VALUE -> targetWire.getValue();
            case AND -> (char) (calculateValue(targetWire.getSourceWireLabels()[0], wireMap, wireValueMap) & calculateValue(targetWire.getSourceWireLabels()[1], wireMap, wireValueMap));
            case OR -> (char) (calculateValue(targetWire.getSourceWireLabels()[0], wireMap, wireValueMap) | calculateValue(targetWire.getSourceWireLabels()[1], wireMap, wireValueMap));
            case LSHIFT -> (char) (calculateValue(targetWire.getSourceWireLabels()[0], wireMap, wireValueMap) << targetWire.getShiftValue());
            case RSHIFT -> (char) (calculateValue(targetWire.getSourceWireLabels()[0], wireMap, wireValueMap) >>> targetWire.getShiftValue());
            case NOT -> (char) (~calculateValue(targetWire.getSourceWireLabels()[0], wireMap, wireValueMap));
            case DIRECT -> calculateValue(targetWire.getSourceWireLabels()[0], wireMap, wireValueMap);
        };

        wireValueMap.put(wireLabel, calculatedValue);
        return calculatedValue;
    }

    private static Wire parseWireInstruction(final String wireInstruction) {
        final Matcher wireMatcher = WIRE_PATTERN.matcher(wireInstruction);
        if (!wireMatcher.matches()) {
            throw new IllegalArgumentException("Wire instruction did not match pattern: " + wireInstruction);
        }

        final String wireLabel = wireMatcher.group(2);
        final String wireSource = wireMatcher.group(1);
        if (StringUtils.isNumeric(wireSource)) {
            return new Wire(wireLabel, wireInstruction, (char) Integer.parseInt(wireSource));
        }

        if (wireSource.charAt(0) == 'N') {
            return new Wire(wireLabel, wireInstruction, new String[]{wireSource.split(" ")[1]}, Wire.Signal.NOT);
        }

        final String[] wireSourceComponents = wireSource.split(" ");
        if (wireSourceComponents.length == 1) {
            return new Wire(wireLabel, wireInstruction, wireSourceComponents, Wire.Signal.DIRECT);
        } else if (wireSourceComponents.length != 3) {
            throw new IllegalArgumentException("Invalid wire source components encountered: " + Arrays.toString(wireSourceComponents));
        }

        if (StringUtils.isNumeric(wireSourceComponents[2])) {
            return new Wire(wireLabel, wireInstruction, new String[]{wireSourceComponents[0]}, Wire.Signal.valueOf(wireSourceComponents[1]), (char) Integer.parseInt(wireSourceComponents[2]));
        }

        return new Wire(wireLabel, wireInstruction, new String[]{wireSourceComponents[0], wireSourceComponents[2]}, Wire.Signal.valueOf(wireSourceComponents[1]));
    }

    public static void main(final String[] args) {
        new Circuitry();
    }
}

class Wire {

    enum Signal {
        VALUE,
        AND,
        OR,
        LSHIFT,
        RSHIFT,
        NOT,
        DIRECT
    }

    private char value;
    private final String label;
    private final String instruction;
    private final String[] sourceWireLabels;
    private final char shiftValue;
    private final Signal signal;

    public Wire(final String label, final String instruction, final char value) {
        this.label = label;
        this.instruction = instruction;
        this.value = value;
        this.signal = Signal.VALUE;
        this.shiftValue = 0;
        sourceWireLabels = new String[0];
    }

    public Wire(final String label, final String instruction, final String[] sourceWireLabels, final Signal signal) {
        this.label = label;
        this.instruction = instruction;
        this.sourceWireLabels = sourceWireLabels;
        this.signal = signal;
        this.shiftValue = 0;
    }

    public Wire(final String label, final String instruction, final String[] sourceWireLabels, final Signal signal, final char shiftValue) {
        this.label = label;
        this.instruction = instruction;
        this.sourceWireLabels = sourceWireLabels;
        this.signal = signal;
        this.shiftValue = shiftValue;
    }

    public char getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setValue(final char value) {
        this.value = value;
    }

    public String[] getSourceWireLabels() {
        return sourceWireLabels;
    }

    public char getShiftValue() {
        return shiftValue;
    }

    public Signal getSignal() {
        return signal;
    }
}
