package com.techieplanet.algorithms.question2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class DuplicateRemoverTest {
    @Test
    void shouldReplaceDuplicatesInEachRowWithZero() {
        int[][] input = {
                {1, 3, 1, 2, 3, 4, 4, 3, 5},
                {1, 1, 1, 1, 1, 1, 1}
        };

        int[][] expected = {
                {1, 3, 0, 2, 0, 4, 0, 0, 5},
                {1, 0, 0, 0, 0, 0, 0}
        };

        assertArrayEquals(expected, DuplicateRemover.removeDuplicates(input));
    }

    @Test
    void shouldTreatEachRowSeparately() {
        int[][] input = {
                {1, 2, 3},
                {1, 2, 1}
        };

        int[][] expected = {
                {1, 2, 3},
                {1, 2, 0}
        };

        assertArrayEquals(expected, DuplicateRemover.removeDuplicates(input));
    }

    @Test
    void shouldLeaveRowsWithoutDuplicatesUnchanged() {
        int[][] input = {
                {4, 5, 6}
        };

        int[][] expected = {
                {4, 5, 6}
        };

        assertArrayEquals(expected, DuplicateRemover.removeDuplicates(input));
    }

    @Test
    void shouldReturnEmptyArrayWhenInputIsEmpty() {
        assertArrayEquals(new int[0][], DuplicateRemover.removeDuplicates(new int[0][]));
    }

    @Test
    void shouldReturnEmptyArrayWhenInputIsNull() {
        assertArrayEquals(new int[0][], DuplicateRemover.removeDuplicates(null));
    }
}
