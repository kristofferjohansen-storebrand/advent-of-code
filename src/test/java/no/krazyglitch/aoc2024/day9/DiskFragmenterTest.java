package no.krazyglitch.aoc2024.day9;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiskFragmenterTest {

    @Test
    void testPartOneExampleA() {
        final String data = "2333133121414131402";
        assertEquals(1928, DiskFragmenter.getDefragmentedChecksum(data));
    }

    @Test
    void testPartTwoExampleA() {
        final String data = "2333133121414131402";
        assertEquals(2858, DiskFragmenter.getDefragmentedChecksumRetainingFiles(data));
    }
}