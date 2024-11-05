package no.krazyglitch.aoc2015.day5;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NiceListTest {

    @Test
    void testPartOneExampleOne() {
        final List<String> data = List.of("ugknbfddgicrmopn");
        assertEquals(1, NiceList.findNiceWords(data));
    }

    @Test
    void testPartOneExampleTwo() {
        final List<String> data = List.of("aaa");
        assertEquals(1, NiceList.findNiceWords(data));
    }

    @Test
    void testPartOneExampleThree() {
        final List<String> data = List.of("jchzalrnumimnmhp");
        assertEquals(0, NiceList.findNiceWords(data));
    }

    @Test
    void testPartOneExampleFour() {
        final List<String> data = List.of("haegwjzuvuyypxyu");
        assertEquals(0, NiceList.findNiceWords(data));
    }

    @Test
    void testPartOneExampleFive() {
        final List<String> data = List.of("haegwjzuvuyypxyu");
        assertEquals(0, NiceList.findNiceWords(data));
    }

    @Test
    void testPartTwoExampleOne() {
        final List<String> data = List.of("qjhvhtzxzqqjkmpb");
        assertEquals(1, NiceList.findSillyWords(data));
    }

    @Test
    void testPartTwoExampleTwo() {
        final List<String> data = List.of("xxyxx");
        assertEquals(1, NiceList.findSillyWords(data));
    }

    @Test
    void testPartTwoExampleThree() {
        final List<String> data = List.of("uurcxstgmygtbstg");
        assertEquals(0, NiceList.findSillyWords(data));
    }

    @Test
    void testPartTwoExampleFour() {
        final List<String> data = List.of("ieodomkazucvgmuy");
        assertEquals(0, NiceList.findSillyWords(data));
    }
}