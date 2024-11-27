package org.example;

import java.util.HashMap;
import java.util.Map;

public class RomanToArabic {
    private static Map<Character, Integer> romanToArabicMap = new HashMap<>();
    static {
        romanToArabicMap.put('I', 1);
        romanToArabicMap.put('I', 1);
        romanToArabicMap.put('V', 5);
        romanToArabicMap.put('X', 10);
        romanToArabicMap.put('L', 50);
        romanToArabicMap.put('C', 100);
        romanToArabicMap.put('D', 500);
        romanToArabicMap.put('M', 1000);
    }


    public static void main(String[] args) {

    }

    public static int convertRomanToArabic(String roman) {
        int sum = 0;
        int previousValue = 0;

        for (int i = roman.length() -1; i >= 0; i--) {
            char currentCharacter = roman.charAt(i);

            int currentValue = romanToArabicMap.get(currentCharacter);
            if (currentValue < previousValue) {
                 sum -= currentValue;
            }
            if (currentValue >= previousValue) {
                sum += currentValue;
            }

            previousValue = currentValue;

        }
        return sum;
    }

}
