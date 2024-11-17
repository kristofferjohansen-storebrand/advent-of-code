package no.krazyglitch.aoc2015.day12;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonExplorerTest {

    @Test
    void testPartOneExampleA() {
        final int result = JsonExplorer.addJSONValues("[1,2,3]");
        assertEquals(6, result);
    }

    @Test
    void testPartOneExampleB() {
        final int result = JsonExplorer.addJSONValues("{\"a\":2,\"b\":4}");
        assertEquals(6, result);
    }

    @Test
    void testPartOneExampleC() {
        final int result = JsonExplorer.addJSONValues("[[[3]]]");
        assertEquals(3, result);
    }

    @Test
    void testPartOneExampleD() {
        final int result = JsonExplorer.addJSONValues("{\"a\":{\"b\":4},\"c\":-1}");
        assertEquals(3, result);
    }

    @Test
    void testPartOneExampleE() {
        final int result = JsonExplorer.addJSONValues("{\"a\":[-1,1]}");
        assertEquals(0, result);
    }

    @Test
    void testPartOneExampleF() {
        final int result = JsonExplorer.addJSONValues("[-1,{\"a\":1}]");
        assertEquals(0, result);
    }

    @Test
    void testPartOneExampleG() {
        final int result = JsonExplorer.addJSONValues("[]");
        assertEquals(0, result);
    }

    @Test
    void testPartOneExampleH() {
        final int result = JsonExplorer.addJSONValues("{}");
        assertEquals(0, result);
    }

    @Test
    void testPartTwoExampleA() {
        final int result = JsonExplorer.addJSONValuesButIgnoreRed("[1,2,3]");
        assertEquals(6, result);
    }

    @Test
    void testPartTwoExampleB() {
        final int result = JsonExplorer.addJSONValuesButIgnoreRed("[1,{\"c\":\"red\",\"b\":2},3]");
        assertEquals(4, result);
    }

    @Test
    void testPartTwoExampleC() {
        final int result = JsonExplorer.addJSONValuesButIgnoreRed("{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}");
        assertEquals(0, result);
    }

    @Test
    void testPartTwoExampleD() {
        final int result = JsonExplorer.addJSONValuesButIgnoreRed("[1,\"red\",5]");
        assertEquals(6, result);
    }
}