import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class WordCounter {


    public String getTheText(String aPathToText) throws IOException {

        String lines = "";

        // TODO - open in the current directory

        try {
            lines = Files.readString(Paths.get(aPathToText));
            return lines;
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }

        return lines;
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

        return result;
    }

    public void writeCountedToFile(Map<String, Integer> aMap) {
        // TODO
    }

}