package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RomanToArabicTests {

    private final RomanToArabic converter = new RomanToArabic();

    @Test
    void testingSingleNumeral() {
        assertEquals(1, converter.convertRomanToArabic("I"));
    }

    @Test
    void testingAllSingleNumerals() {
        assertEquals(1, converter.convertRomanToArabic("I"));
        assertEquals(5, converter.convertRomanToArabic("V"));
        assertEquals(10, converter.convertRomanToArabic("X"));
        assertEquals(50, converter.convertRomanToArabic("L"));
        assertEquals(100, converter.convertRomanToArabic("C"));
        assertEquals(500, converter.convertRomanToArabic("D"));
        assertEquals(1000, converter.convertRomanToArabic("M"));
    }

    @Test
    void testSimpleCombinations() {
        assertEquals(2, converter.convertRomanToArabic("II"));
        assertEquals(6, converter.convertRomanToArabic("VI"));
        assertEquals(15, converter.convertRomanToArabic("XV"));
        assertEquals(55, converter.convertRomanToArabic("LV"));
    }

}
