package org.example;

import java.util.HashMap;
import java.util.Map;

public class RomanToArabic {
    private static Map<Character, Integer> romanToArabicMap = new HashMap<>();
    static {
        romanToArabicMap.put('I', 1);
        romanToArabicMap.put('V', 5);
        romanToArabicMap.put('X', 10);
        romanToArabicMap.put('L', 50);
        romanToArabicMap.put('C', 100);
        romanToArabicMap.put('D', 500);
        romanToArabicMap.put('M', 1000);
    }

    private static Map<Character, Integer> nonRepeatingValues = new HashMap<>();
    static {
        nonRepeatingValues.put('V', 5);
        nonRepeatingValues.put('L', 50);
        nonRepeatingValues.put('D', 500);
    }


    public static void main(String[] args) {

    }

    public static int convertRomanToArabic(String roman) {
        int sum = 0;
        int previousValue = 0;

        int repeatsCount = 0;
        char previousChar = '\0';

        if (roman.length() == 0) throw new IllegalArgumentException("empty input!");


        for (int i = roman.length() -1; i >= 0; i--) {
            char currentCharacter = roman.charAt(i);

            if (Character.compare(currentCharacter, previousChar) == 0) {
                repeatsCount++;
            }

            if (currentCharacter == previousChar && nonRepeatingValues.containsKey(previousChar)) {
                throw new IllegalArgumentException(previousChar + " follows " + currentCharacter + ", unacceptable");
            }

            if (repeatsCount > 2) {
                throw new IllegalArgumentException("too many identical characters");
            }

            if (Character.compare(currentCharacter, previousChar) != 0) {
                repeatsCount = 0;
            }

            int currentValue = romanToArabicMap.get(currentCharacter);

            boolean isAcceptableCharacter = nonRepeatingValues.containsKey(currentCharacter);

            if (currentValue < previousValue && isAcceptableCharacter) {
                throw new IllegalArgumentException(previousChar + " follows " + currentCharacter + ", unacceptable");
            }

            if (currentValue < previousValue) {
                 sum -= currentValue;
            }
            if (currentValue >= previousValue) {
                sum += currentValue;
            }

            previousValue = currentValue;
            previousChar = currentCharacter;

        }
        return sum;
    }

}
