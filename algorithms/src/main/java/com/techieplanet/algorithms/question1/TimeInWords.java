package com.techieplanet.algorithms.question1;

import java.util.Scanner;

public class TimeInWords {
    /*
     * Lookup table for number words.
     * We only need values from 0 to 29 because minutes above 30 are converted
     * to "minutes to next hour", for example 47 becomes 13 minutes to ten.
     */
    private static final String[] NUMBERS = {
            "zero",
            "one",
            "two",
            "three",
            "four",
            "five",
            "six",
            "seven",
            "eight",
            "nine",
            "ten",
            "eleven",
            "twelve",
            "thirteen",
            "fourteen",
            "quarter",
            "sixteen",
            "seventeen",
            "eighteen",
            "nineteen",
            "twenty",
            "twenty-one",
            "twenty-two",
            "twenty-three",
            "twenty-four",
            "twenty-five",
            "twenty-six",
            "twenty-seven",
            "twenty-eight",
            "twenty-nine"
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            int hour = scanner.nextInt();
            int minute = scanner.nextInt();
            System.out.println(convert(hour, minute));
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static String convert(int hour, int minute) {
        validate(hour, minute);

        if (minute == 0) {
            return numberToWords(hour) + " o'clock";
        }

        // 30 minutes has a special phrase: "half past".
        if (minute == 30) {
            return "half past " + numberToWords(hour);
        }

        // From 1 to 30 minutes, the time is described as minutes past the hour.
        if (minute <= 30) {
            return minutePhrase(minute) + " past " + numberToWords(hour);
        }

        // From 31 to 59 minutes, describe how many minutes remain to the next hour.
        int minutesToNextHour = 60 - minute;
        return minutePhrase(minutesToNextHour) + " to " + numberToWords(nextHour(hour));
    }

    private static void validate(int hour, int minute) {
        if (hour < 1 || hour > 12) {
            throw new IllegalArgumentException("Hour must be between 1 and 12");
        }

        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Minute must be between 0 and 59");
        }
    }

    private static String minuteToWords(int minute) {
        return NUMBERS[minute];
    }

    private static String numberToWords(int number) {
        return NUMBERS[number];
    }

    private static String minuteLabel(int minute) {
        return minute == 1 ? "minute" : "minutes";
    }

    private static String minutePhrase(int minute) {
        // 15 minutes is written as "quarter", not "quarter minutes".
        if (minute == 15) {
            return minuteToWords(minute);
        }

        return minuteToWords(minute) + " " + minuteLabel(minute);
    }

    private static int nextHour(int hour) {
        return hour == 12 ? 1 : hour + 1;
    }
}
