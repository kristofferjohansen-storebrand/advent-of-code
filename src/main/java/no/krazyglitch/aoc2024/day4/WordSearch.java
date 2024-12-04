package no.krazyglitch.aoc2024.day4;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class WordSearch {

    private static final SearchDirection[] SEARCH_DIRECTIONS = SearchDirection.values();

    public WordSearch() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("There are %d matches for the word 'XMAS'\n", performWordSearch(data, "XMAS"));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("There are %d MAS crosses\n", findCrossMASes(data));
            System.out.printf("Part two took %d ms", getMillisSince(start));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static int performWordSearch(final List<String> data, final String word) {
        final char[][] grid = createGrid(data);
        int wordMatchCount = 0;

        final int gridLength = grid[0].length;
        final int gridHeight = grid.length;
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridLength; x++) {
                for (final SearchDirection searchDirection : SEARCH_DIRECTIONS) {
                    if (findWord(grid, word, 0, x, y, searchDirection)) {
                        wordMatchCount++;
                    }
                }
            }
        }

        return wordMatchCount;
    }

    public static int findCrossMASes(final List<String> data) {
        final char[][] grid = createGrid(data);
        int crossMASCount = 0;

        final int gridLength = grid[0].length;
        final int gridHeight = grid.length;

        for (int y = 1; y < gridHeight-1; y++) {
            for (int x = 1; x < gridLength-1; x++) {
                if (grid[y][x] == 'A') {
                    if (hasDiagonalCross(grid, x, y)) {
                        crossMASCount++;
                    }
                }
            }
        }

        return crossMASCount;
    }

    private static boolean findWord(final char[][] grid, final String word, final int wordIndex, final int x, final int y, final SearchDirection direction) {
        if (wordIndex == word.length()) {
            return true;
        }

        if (!isCoordinateValid(grid, x, y)) {
            return false;
        }

        if (grid[y][x] != word.charAt(wordIndex)) {
            return false;
        }

        return findWord(grid, word, wordIndex+1, x+direction.x, y+direction.y, direction);
    }

    private static boolean hasDiagonalCross(final char[][] grid, final int x, final int y) {
        int matchCount = 0;

        for (final SearchDirection searchDirection : SearchDirection.diagonal()) {
            if (findWord(grid, "MAS", 0, x-searchDirection.x, y-searchDirection.y, searchDirection)) {
                matchCount++;
            }
        }

        return matchCount == 2;
    }

    private static boolean isCoordinateValid(final char[][] grid, final int x, final int y) {
        return x >= 0 && x < grid[0].length && y >= 0 && y < grid.length;
    }

    private static char[][] createGrid(final List<String> data) {
        final int gridLength = data.getFirst().length();
        final int gridHeight = data.size();
        final char[][] grid = new char[gridLength][gridHeight];
        for (int y = 0; y < gridHeight; y++) {
            final String row = data.get(y);
            for (int x = 0; x < gridLength; x++) {
                grid[y][x] = row.charAt(x);
            }
        }

        return grid;
    }

    enum SearchDirection {
        N(0, 1),
        NE(1, 1),
        E(1, 0),
        SE(1, -1),
        S(0, -1),
        SW(-1, -1),
        W(-1, 0),
        NW(-1,  1);

        private final int x;
        private final int y;

        SearchDirection(final int x, final int y) {
            this.x = x;
            this.y = y;
        }

        static Set<SearchDirection> diagonal() {
            return EnumSet.of(NE, SE, SW, NW);
        }
    }

    public static void main(final String[] args) {
        new WordSearch();
    }
}
