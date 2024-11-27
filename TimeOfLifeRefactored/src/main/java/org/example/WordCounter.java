package org.example;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WordCounter {
    public static String normalizeText(String text) {
        String txt = text.replaceAll("[^a-zA-Z\\s]", "").toLowerCase();
        if (txt.matches("\\s")){
            throw new IllegalArgumentException("empty string has no words or letters");
        }
        return txt;
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

    public static Map<Character, Integer> countLetterFrequency(String[] words) {
        Map<Character, Integer> unionMap = new HashMap<>();
        ArrayList<Map<Character, Integer>> temp = new ArrayList<>();

        for (String word : words) temp.add(countSeparateWordLetters(word));

        for (Map<Character, Integer> map: temp) {
            map.forEach((key, value) ->
                    unionMap.merge(key, value, Integer::sum)
            );
        }
        return unionMap;
    }

    public static Map<Character, Integer> countSeparateWordLetters(String text) {
        Map<Character, Integer> frequency = new HashMap<>();
        for (char letter: text.toCharArray()) {
            frequency.put(letter, frequency.getOrDefault(letter, 0) + 1);
        }
        return frequency;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a text:");
        String input = scanner.nextLine();

        String normalizedText = normalizeText(input);
        String[] words = splitIntoWords(normalizedText);
        Map<String, Integer> wordFrequency = countWordFrequency(words);

        Map<Character, Integer> letterFrequency = countLetterFrequency(words);

        System.out.println("\nResults:");
        System.out.println("Number of words: " + words.length);
        System.out.println("Word frequencies:");
        wordFrequency.forEach((word, count) -> System.out.println("  " + word + ": " + count));

        System.out.println("\nNumber of letters: " + letterFrequency.keySet().size());
        System.out.println("Letter frequencies:");
        letterFrequency.forEach((letter, count) -> System.out.println("  " + letter + ": " + count));
    }

}
