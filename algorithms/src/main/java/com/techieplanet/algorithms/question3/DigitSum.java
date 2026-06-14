package com.techieplanet.algorithms.question3;

import java.util.Scanner;

public class DigitSum {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String digits = scanner.nextLine();

        try {
            System.out.println("Sum of digits: " + sumDigits(digits));
            System.out.println("Digital root: " + digitalRoot(digits));
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static int sumDigits(String digits) {
        validate(digits);
        return sumDigits(digits, 0);
    }

    public static int digitalRoot(String digits) {
        int sum = sumDigits(digits);

        while (sum >= 10) {
            sum = sumDigits(String.valueOf(sum));
        }

        return sum;
    }

    private static int sumDigits(String digits, int index) {
        if (index == digits.length()) {
            return 0;
        }

        int currentDigit = digits.charAt(index) - '0';
        return currentDigit + sumDigits(digits, index + 1);
    }

    private static void validate(String digits) {
        if (digits == null || digits.isBlank()) {
            throw new IllegalArgumentException("Input must contain at least one digit");
        }

        if (digits.length() > 100) {
            throw new IllegalArgumentException("Input must not be longer than 100 digits");
        }

        for (int index = 0; index < digits.length(); index++) {
            if (!Character.isDigit(digits.charAt(index))) {
                throw new IllegalArgumentException("Input must contain only digits");
            }
        }
    }
}
