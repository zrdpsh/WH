package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RomanToArabicTests {

    private final RomanToArabic converter = new RomanToArabic();

    @Test
    void testingSingleNumerals() {
        assertEquals(1, converter.convertRomanToArabic("I"));
    }

}
