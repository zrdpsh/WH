package org.example;

public class WordCounter {
    public static String normalizeText(String text) {
        return text.replaceAll("[^a-zA-Z\\s]", "").toLowerCase();
    }
    public static String[] splitIntoWords(String text) {
        return text.split("\\s+");
    }
}
