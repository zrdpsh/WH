import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class WordCounter {

    String textFromFile = "no input yet";

    public WordCounter(String pathToFile) throws IOException {
        this.textFromFile = getTheText(pathToFile);
    }

    public WordCounter() {
        // TODO
    }

    public String getTheText(String aPathToText) throws IOException {

        String lines = "";
        StringBuilder sb = new StringBuilder();

        // TODO - open in the current directory

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(aPathToText), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }

        return sb.toString();
    }

    public ArrayList<String> breakdownText(String txt) {
        String[] tmp = txt.split("\\s+");

        return new ArrayList<>(Arrays.asList(tmp));
    }

    public ArrayList<String> getDigraphs(ArrayList<String> aText) {

        ArrayList<String> result = new ArrayList<String>();
        for (String word : aText) {
            ArrayList<String> tmp = wordIntoDigraphs(word);
            for (String t: tmp) result.add(t);
        }
        return result;
    }

    private ArrayList<String> wordIntoDigraphs(String aWord) {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < aWord.length()-1; i++) {
            result.add("" + aWord.charAt(i) + aWord.charAt(i+1));
        }
        return result;
    }

    public Map<String, Integer> countStrings(List<String> anArray) {
        Map<String, Integer> result = new HashMap<>();


        for (String word : anArray) {
            if (result.containsKey(word)) {
                result.put(word, result.get(word) + 1);
            } else {
                result.put(word, 1);
            }
        }

        return result;
    }

    public void writeCountedToFile(Map<String, Integer> aMap) {
        String fileName = "result.txt";

        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(aMap.entrySet());
        sortedEntries.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<String, Integer> entry : sortedEntries) {
                writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
            }
            System.out.println("List has been written to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void countWords() {
        Map<String, Integer> tmp = countStrings(breakdownText(textFromFile));
        writeCountedToFile(tmp);
    }

    public void countDigraphs() {
        Map<String, Integer> tmp = countStrings(getDigraphs(breakdownText(textFromFile)));
        writeCountedToFile(tmp);
    }

}