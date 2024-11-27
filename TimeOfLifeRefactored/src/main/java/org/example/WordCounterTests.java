package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WordCounterTests {

    @Test
    void normalizeTextTest() {
        String actual = "Saint -petersburg#1703";
        String expected = "saint petersburg";
        assertEquals(expected, WordCounter.normalizeText(actual));
    }

//    @Test
//    void testNullInputs() {
//        assertThrows(NullPointerException.class, () -> converter.convertRomanToArabic(null));
//    }
}
