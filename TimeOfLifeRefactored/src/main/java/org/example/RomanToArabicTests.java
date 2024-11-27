package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void testComplexNumerals() {
        assertEquals(58, converter.convertRomanToArabic("LVIII"));
        assertEquals(1994, converter.convertRomanToArabic("MCMXCIV"));
        assertEquals(2023, converter.convertRomanToArabic("MMXXIII"));
    }

    @Test
    void testNullInputs() {
        assertThrows(NullPointerException.class, () -> converter.convertRomanToArabic(null));
    }

    @Test
    void testEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> converter.convertRomanToArabic(""));
    }

    @Test
    void testingInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () -> converter.convertRomanToArabic("IIII"));
        assertThrows(IllegalArgumentException.class, () -> converter.convertRomanToArabic("VX"));
    }



}
