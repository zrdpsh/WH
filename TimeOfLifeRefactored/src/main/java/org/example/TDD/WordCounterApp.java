package org.example.TDD;

import java.io.IOException;

public class WordCounterApp {

    public static void main(String[] args) throws IOException {
        WordCounter wordCounter = new WordCounter();

        // Ensure arguments are provided
        if (args.length == 0) {
            System.out.println("Usage: java WordCounterApp <file_path>");
            return;
        }

        if (args[0].endsWith(".txt")) {
            wordCounter.getTheText(args[0]);
        }
    }
}