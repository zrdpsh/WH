package org.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    @Test
    void twoWordsFrequencyTest() {
        Map<String, Integer> expectedMap = new HashMap<>();
        expectedMap.put("saint", 1);
        expectedMap.put("petersburg", 1);
        String[] actualString = {"saint", "petersburg"};
        assertTrue(expectedMap.equals(WordCounter.countWordFrequency(actualString)));
    }

//    @Test
//    void testNullInputs() {
//        assertThrows(NullPointerException.class, () -> converter.convertRomanToArabic(null));
//    }
}
