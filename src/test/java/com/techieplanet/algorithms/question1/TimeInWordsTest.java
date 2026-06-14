package com.techieplanet.algorithms.question1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TimeInWordsTest {
    @Test
    void shouldReturnOClockWhenMinuteIsZero() {
        assertEquals("five o'clock", TimeInWords.convert(5, 0));
    }

    @Test
    void shouldReturnSingularMinuteWhenMinuteIsOne() {
        assertEquals("one minute past five", TimeInWords.convert(5, 1));
    }

    @Test
    void shouldReturnQuarterPastWhenMinuteIsFifteen() {
        assertEquals("quarter past five", TimeInWords.convert(5, 15));
    }

    @Test
    void shouldReturnHalfPastWhenMinuteIsThirty() {
        assertEquals("half past five", TimeInWords.convert(5, 30));
    }

    @Test
    void shouldReturnQuarterToWhenMinuteIsFortyFive() {
        assertEquals("quarter to six", TimeInWords.convert(5, 45));
    }

    @Test
    void shouldReturnMinutesToNextHourWhenMinuteIsAboveThirty() {
        assertEquals("thirteen minutes to six", TimeInWords.convert(5, 47));
    }

    @Test
    void shouldReturnSingularMinuteToNextHourWhenMinuteIsFiftyNine() {
        assertEquals("one minute to six", TimeInWords.convert(5, 59));
    }

    @Test
    void shouldWrapFromTwelveToOne() {
        assertEquals("one minute to one", TimeInWords.convert(12, 59));
    }

    @Test
    void shouldRejectInvalidHour() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> TimeInWords.convert(13, 0)
        );

        assertEquals("Hour must be between 1 and 12", exception.getMessage());
    }

    @Test
    void shouldRejectInvalidMinute() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> TimeInWords.convert(5, 60)
        );

        assertEquals("Minute must be between 0 and 59", exception.getMessage());
    }
}
