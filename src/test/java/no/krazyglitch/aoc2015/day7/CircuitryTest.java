package no.krazyglitch.aoc2015.day7;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CircuitryTest {

    private final static List<String> testData = List.of("123 -> x",
            "456 -> y",
            "x AND y -> d",
            "x OR y -> e",
            "x LSHIFT 2 -> f",
            "y RSHIFT 2 -> g",
            "NOT x -> h",
            "NOT y -> i",
            "y -> z",
            "1 LSHIFT 2 -> j");

    @Test
    void testPartOneLabelD() {
        assertEquals(72, Circuitry.simulateWire(testData, "d"));
    }

    @Test
    void testPartOneLabelE() {
        assertEquals(507, Circuitry.simulateWire(testData, "e"));
    }

    @Test
    void testPartOneLabelF() {
        assertEquals(492, Circuitry.simulateWire(testData, "f"));
    }

    @Test
    void testPartOneLabelG() {
        assertEquals(114, Circuitry.simulateWire(testData, "g"));
    }

    @Test
    void testPartOneLabelH() {
        assertEquals(65412, Circuitry.simulateWire(testData, "h"));
    }

    @Test
    void testPartOneLabelI() {
        assertEquals(65079, Circuitry.simulateWire(testData, "i"));
    }

    @Test
    void testPartOneLabelX() {
        assertEquals(123, Circuitry.simulateWire(testData, "x"));
    }

    @Test
    void testPartOneLabelY() {
        assertEquals(456, Circuitry.simulateWire(testData, "y"));
    }

    @Test
    void testPartOneLabelZ() {
        assertEquals(456, Circuitry.simulateWire(testData, "z"));
    }

    @Test
    void testPartOneLabelJ() {
        assertEquals(4, Circuitry.simulateWire(testData, "j"));
    }
}