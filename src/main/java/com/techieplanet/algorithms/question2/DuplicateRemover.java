package com.techieplanet.algorithms.question2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DuplicateRemover {
    public static void main(String[] args) {
        int[][] sample = {
                {1, 3, 1, 2, 3, 4, 4, 3, 5},
                {1, 1, 1, 1, 1, 1, 1}
        };

        int[][] result = removeDuplicates(sample);
        System.out.println(Arrays.deepToString(result));
    }

    /*
     * Approach:
     * Each row gets its own HashSet. As we scan the row from left to right,
     * HashSet.add(value) returns true only when the value has not been seen before.
     * If add returns false, that value is a duplicate in the current row and is
     * replaced with 0. This avoids using contains() or containsKey().
     *
     * Time complexity: O(total number of elements) on average.
     * Space complexity: O(m) per row, where m is the row length.
     */
    public static int[][] removeDuplicates(int[][] array) {
        if (array == null) {
            return new int[0][];
        }

        int[][] result = new int[array.length][];

        for (int rowIndex = 0; rowIndex < array.length; rowIndex++) {
            int[] row = array[rowIndex];

            if (row == null) {
                result[rowIndex] = new int[0];
                continue;
            }

            result[rowIndex] = removeDuplicatesFromRow(row);
        }

        return result;
    }

    private static int[] removeDuplicatesFromRow(int[] row) {
        int[] result = new int[row.length];
        Set<Integer> seenNumbers = new HashSet<>();

        for (int index = 0; index < row.length; index++) {
            int value = row[index];

            if (seenNumbers.add(value)) {
                result[index] = value;
            } else {
                result[index] = 0;
            }
        }

        return result;
    }
}
