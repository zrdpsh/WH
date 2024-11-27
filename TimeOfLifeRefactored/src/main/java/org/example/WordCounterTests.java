package org.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class WordCounterTests {

    @Test
    void normalizeTextTest() {
        String actual = "Saint -petersburg#1703";
        String expected = "saint petersburg";
        assertEquals(expected, WordCounter.normalizeText(actual));
    }

    @Test
    void splitWordsTest() {
        String actual = "Saint -petersburg#1703";
        String[] expected = {"saint", "petersburg"};
        String[] result = WordCounter.splitIntoWords(WordCounter.normalizeText(actual));
        assertTrue(Arrays.equals(expected, result));
    }

//    @Test
//    void testNullInputs() {
//        assertThrows(NullPointerException.class, () -> converter.convertRomanToArabic(null));
//    }
}
