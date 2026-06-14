package com.techieplanet.algorithms.question3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DigitSumTest {
    @Test
    void shouldReturnSumOfDigitsForSampleInput() {
        String input = "1234445123444512344451234445123444512344451234445";

        assertEquals(161, DigitSum.sumDigits(input));
    }

    @Test
    void shouldReturnDigitalRoot() {
        assertEquals(5, DigitSum.digitalRoot("1234445"));
    }

    @Test
    void shouldReturnSingleDigitAsItsOwnDigitalRoot() {
        assertEquals(7, DigitSum.digitalRoot("7"));
    }

    @Test
    void shouldRejectBlankInput() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DigitSum.sumDigits(" ")
        );

        assertEquals("Input must contain at least one digit", exception.getMessage());
    }

    @Test
    void shouldRejectNonDigitInput() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DigitSum.sumDigits("123a")
        );

        assertEquals("Input must contain only digits", exception.getMessage());
    }

    @Test
    void shouldRejectInputLongerThanOneHundredDigits() {
        String input = "1".repeat(101);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DigitSum.sumDigits(input)
        );

        assertEquals("Input must not be longer than 100 digits", exception.getMessage());
    }
}
