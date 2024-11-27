package org.example;

import java.util.HashMap;
import java.util.Map;

public class WordCounter {
    public static String normalizeText(String text) {
        return text.replaceAll("[^a-zA-Z\\s]", "").toLowerCase();
    }
    public static String[] splitIntoWords(String text) {
        return text.split("\\s+");
    }

    public static Map<String, Integer> countWordFrequency(String[] words) {
        Map<String, Integer> frequency = new HashMap<>();
        for (String word: words) {
            frequency.put(word, frequency.getOrDefault(word, 0) + 1);
        }
        return frequency;
    }
}
