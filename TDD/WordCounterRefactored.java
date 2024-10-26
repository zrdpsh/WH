import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WordCounterRefactored {

    private final TextProcessor textProcessor;
    private final Counter counter;
    private final OutputWriter outputWriter;
    private String textFromFile;

    public WordCounterRefactored(TextProcessor textProcessor, Counter counter, OutputWriter outputWriter) {
        this.textProcessor = textProcessor;
        this.counter = counter;
        this.outputWriter = outputWriter;
    }

    public void loadTextFromFile(String pathToFile) throws IOException {
        this.textFromFile = new FileReader().readTextFromFile(pathToFile);
    }

    public void countWords() {
        List<String> words = textProcessor.breakdownText(textFromFile);
        Map<String, Integer> wordCounts = counter.countOccurrences(words);
        outputWriter.writeCountsToFile(wordCounts, "word_counts.txt");
    }

    public void countDigraphs() {
        List<String> words = textProcessor.breakdownText(textFromFile);
        List<String> digraphs = textProcessor.extractDigraphs(words);
        Map<String, Integer> digraphCounts = counter.countOccurrences(digraphs);
        outputWriter.writeCountsToFile(digraphCounts, "digraph_counts.txt");
    }
}

class FileReader {
    public String readTextFromFile(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
}

class TextProcessor {
    public List<String> breakdownText(String text) {
        return Arrays.asList(text.split("\\s+"));
    }

    public List<String> extractDigraphs(List<String> words) {
        List<String> digraphs = new ArrayList<>();
        for (String word : words) {
            for (int i = 0; i < word.length() - 1; i++) {
                digraphs.add(word.substring(i, i + 2));
            }
        }
        return digraphs;
    }
}

class Counter {
    public Map<String, Integer> countOccurrences(List<String> elements) {
        Map<String, Integer> counts = new HashMap<>();
        for (String element : elements) {
            counts.put(element, counts.getOrDefault(element, 0) + 1);
        }
        return counts;
    }
}

class OutputWriter {
    public void writeCountsToFile(Map<String, Integer> counts, String fileName) {
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(counts.entrySet());
        sortedEntries.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<String, Integer> entry : sortedEntries) {
                writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
            }
            System.out.println("Counts have been written to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

