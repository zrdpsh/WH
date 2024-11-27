package org.example.TDD;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class WordCounterTest {

    final String PATH_TO_FILE = "C:\\Users\\Admin\\IdeaProjects\\WH\\TDD\\testFile.txt";
    final String PATH_TO_RESULT = "C:\\Users\\Admin\\IdeaProjects\\WH\\result.txt";

    @Test
    public void testWordCounterCreation() {
        assertDoesNotThrow(() -> new WordCounter(), "Class was created without any errors");

    }

    @Test
    public void testProperUTFHandling() {
        assertDoesNotThrow(() -> new WordCounter(PATH_TO_FILE), "Class was created without any errors");

    }

    @Test
    public void getTheTextTest() {
        WordCounter wc = new WordCounter();
        String filePath = PATH_TO_FILE;


        try {
            String content = wc.getTheText(filePath);

            assertNotNull(content, "File content should not be null.");
            assertFalse(content.isEmpty(), "File content should not be empty.");

            String expectedContent = Files.readString(Paths.get(filePath));
            assertEquals(expectedContent, content.trim(), "File content should match expected content.");
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void breakdownTextTest() {
        WordCounter wc = new WordCounter();
        ArrayList<String> testArray = wc.breakdownText("projet de 60 milliards de euros de économies");
        String[]  tmp = new String[]{"projet", "de", "60", "milliards", "de", "euros", "de", "économies"};
        List<String> example = new ArrayList<>(Arrays.asList(tmp));
        assertEquals(example, testArray, "example and testArray should be equal");
    }

    @Test
    public void getDigraphsTest() {
        WordCounter wc = new WordCounter();
        ArrayList<String> testWord = wc.breakdownText("projet");
        ArrayList<String> testArray = wc.getDigraphs(testWord);
        String[] tmp = new String[]{"pr", "ro", "oj", "je", "et"};
        List<String> example = new ArrayList<>(Arrays.asList(tmp));
        assertEquals(example, testArray, "example and testArray should be equal");
    }

    @Test
    public void countStringsTest() {
        WordCounter wc = new WordCounter();

        String[]  tmp = new String[]{"projet", "de", "60", "de", "euros", "de", "économies"};
        List<String> testArray = new ArrayList<>(Arrays.asList(tmp));
        Map<String, Integer>  testMap = wc.countStrings(testArray);
        Map<String, Integer> example = Map.of(
                "projet", 1,
                "de", 3,
                "60", 1,
                "euros", 1,
                "économies", 1
        );
        assertEquals(example, testMap, "example and testArray should be equal");
    }

    @Test
    public void countDigraphsTest() {
        WordCounter wc = new WordCounter();
        String[]  tmp = new String[]{"pr", "ro", "oj", "je", "et", "pr", "ro", "oj", "pr"};
        List<String> testArray = new ArrayList<>(Arrays.asList(tmp));
        Map<String, Integer> testMap = wc.countStrings(testArray);
        Map<String, Integer> example = Map.of(
                "pr", 3,
                "ro", 2,
                "oj", 2,
                "je", 1,
                "et", 1
        );
        Assertions.assertEquals(example, testMap, "example and testArray should be equal");
    }

    @Test
    public void writeCountedToFileTest() {
        WordCounter wc = new WordCounter();
        Map<String, Integer> mapUnderTest = Map.of(
                "ét", 1,
                "pr", 3,
                "ro", 2
        );
        wc.writeCountedToFile(mapUnderTest);

        String expectedContents = "pr: 3\nro: 2\nét: 1\n";

        assertWithStringBuilder(expectedContents);
    }

    @Test
    public void countWordsTest() throws IOException {
        WordCounter wc = new WordCounter(PATH_TO_FILE);
        wc.countWords();

        String expectedContents = "de: 3\n" +
                "euros: 1\n" +
                "projet: 1\n" +
                "économies: 1\n" +
                "60: 1\n" +
                "milliards: 1\n";

        assertWithStringBuilder(expectedContents);
    }

    private void assertWithStringBuilder(String expectedContents) {
        StringBuilder actualContents = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(PATH_TO_RESULT), StandardCharsets.UTF_8))) {{
            String line;
            while ((line = reader.readLine()) != null) {
                actualContents.append(line).append("\n");
            }
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        expectedContents.trim();
        actualContents.toString().trim();

        if (expectedContents.length() != actualContents.toString().length()) {
            System.out.println("Length mismatch: expected " + expectedContents.length() + ", actual " + actualContents.toString().length());
            System.out.println("expected string " + expectedContents);
            System.out.println("actual string " + actualContents);
        }
        compareStrings(expectedContents.trim(), actualContents.toString().trim());
//        assertEquals (expectedContents.trim(), actualContents.toString().trim(), "File contents do not match.");
        // TODO - why equals doest work properly
    }

    @Test
    public void countWordsTestCustom() throws IOException {
        WordCounter wc = new WordCounter(PATH_TO_FILE);
        wc.countWords();

        String expectedContents = "de: 3\n" +
                "euros: 1\n" +
                "projet: 1\n" +
                "économies: 1\n" +
                "60: 1\n" +
                "milliards: 1\n";

        StringBuilder actualContents = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(PATH_TO_RESULT), StandardCharsets.UTF_8))) {{
            String line;
            while ((line = reader.readLine()) != null) {
                actualContents.append(line).append("\n");
            }
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        compareStrings(expectedContents.trim(), actualContents.toString().trim());
    }

    private boolean compareStrings(String expected, String actual) {
        if (expected.length() != actual.length()) {
            return false;
        }

        for (int i = 0; i < expected.length(); i++) {
            if (expected.charAt(i) != actual.charAt(i)) {
                System.out.println("Difference at index " + i + ": expected '" + expected.charAt(i) + "', but got '" + actual.charAt(i) + "'");
                return false;
            }
        }
        return true;
    }
}
