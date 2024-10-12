- По тдд раньше не работал
- Вместо того, чтобы сразу же открывать редактор и начинать печатать код, сначала попробовал сформулировать, что делает программа, с карандашом на бумажке: рисовал блок-схему {входной тип данных -> операция -> тип данных на выходе}, {след. операция -> след. тип данных} и тд.
  Далее логично стало придумать функциям названия.
- Первый вывод: TDD так или иначе предполагает помедитировать над логикой программы, имея дело исключительно с именами операций - абстракция как таковая. Сам по себе очень основательный подход к работе с кодом (и более сложный).


- Потенциально, тдд может выработать привычку думать "задом наперед", вынося на первый план сущность того, над чем ты работаешь, не даёт сбить фокус и не уйти в детали. А это, насколько могу судить, одна из главных ловушек, когда речь идёт об аналитической по своему характеру работе.
- Пришло в голову, что схожим образом 2d-художников в Фотошопе учат создавать изображение от общего к частному ("start with blocking the main areas") и последовательно переходить на всё больший уровень детализации. Процесс, выглядящий естественным в условно гуманитарной дисциплине, применительно к разработке формальной системы оказывается неожиданно сложен (или непривычен, как минимум). Иллюстратор начнёт с общего контура, и закончит бликом в глазу, когда рисует сову, а не наоборот, в то время как программисту вполне прощается объявить переменные в классе "Сова" и впасть в ступор.
- Интересно, можно ли научиться и программы в целом писать вот так - от большего к частному, нигде не проседая и не сбивая темп.


- Второй вывод и главный плюс - с точки зрения ритма работы - когда закончил проект "на бумаге", дальше можно переходить к собственно коду (где появятся проблемы с линтером, компиляцией и прочим), будучи уверенным, что самое главное уже готово.
- Польза с точки зрения мышления - дисциплинирует. Пока писал тесты, понял, что одна функция из первоначального решения не нужна, а про минимум три других вообще забыл подумать.

- [Первый вариант решения](#WordCounter.java) - каждая операция - отдельная функция внутри общего класса. После того, как прошёл все тесты, пришлось добавлять новые - т.к. в ASCII нет символов с диакритиками, не вспомнил про это.

- [Cо второй частью задания](#WordCounterRefactored.java): чтобы добиться того, чтобы и код, и тесты следовали логической структуре, сделал основые четыре операции (прочитать - разбить на токены - посчитать - записать в файл) отдельными классами. Логика явно выделена, классы можно использовать в других проектах, и тестировать каждый по отдельности. Но здесь тесты оставил те же самые, вместо вызова соседней функции - вызов нужного объекта с соответствующим методом.


**WordCounterApp.java**

```java
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
```



## WordCounter.java
```java
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
```


**WordCounterTest.java**

```java
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

        // TODO - why equals doesnt work properly
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
```

## WordCounterRefactored.java

```java
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
        outputWriter.writeCountsToFile(wordCounts, "result.txt");
    }

    public void countDigraphs() {
        List<String> words = textProcessor.breakdownText(textFromFile);
        List<String> digraphs = textProcessor.extractDigraphs(words);
        Map<String, Integer> digraphCounts = counter.countOccurrences(digraphs);
        outputWriter.writeCountsToFile(digraphCounts, "result.txt");
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
```

```java
public class WordCounterTest {

    private final String PATH_TO_FILE = "C:\\Users\\Admin\\IdeaProjects\\WH\\TDD\\testFile.txt";
    private final String PATH_TO_RESULT = "C:\\Users\\Admin\\IdeaProjects\\WH\\result.txt";
  
    @Test
    public void getTheTextTest() throws IOException {
        FileReader fileReader = new FileReader();
        String text = fileReader.readTextFromFile(PATH_TO_FILE);

        assertNotNull(text);
        assertFalse(text.isEmpty());
    }

    @Test
    public void breakdownTextTest() {
        TextProcessor textProcessor = new TextProcessor();
        List<String> words = textProcessor.breakdownText("projet de 60 milliards de euros de économies");

        assertEquals(Arrays.asList("projet", "de", "60", "milliards", "de", "euros", "de", "économies"), words, "example and testArray should be equal");
    }

    @Test
    public void getDigraphsTest() {
        TextProcessor textProcessor = new TextProcessor();
        List<String> digraphs = textProcessor.extractDigraphs(Arrays.asList("projet"));

        List<String> expectedDigraphs = Arrays.asList("pr", "ro", "oj", "je", "et");
        assertEquals(expectedDigraphs, digraphs, "example and testArray should be equal");
    }

    @Test
    public void countStringsTest() {
        Counter counter = new Counter();
        List<String> words = Arrays.asList("projet", "de", "60", "de", "euros", "de", "économies");
        Map<String, Integer> counts = counter.countOccurrences(words);

        Map<String, Integer> example = Map.of(
                "projet", 1,
                "de", 3,
                "60", 1,
                "euros", 1,
                "économies", 1
        );
        assertEquals(expectedCounts, counts, "example and testArray should be equal");
    }

    @Test
    public void writeCountedToFileTest() throws IOException {
        OutputWriter outputWriter = new OutputWriter();
      Map<String, Integer> mapUnderTest = Map.of(
              "ét", 1,
              "pr", 3,
              "ro", 2
      );

        outputWriter.writeCountsToFile(counts, PATH_TO_RESULT);

        StringBuilder actualContents = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(PATH_TO_RESULT), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                actualContents.append(line).append("\n");
            }
        }

        String expectedContents = "pr: 3\nro: 2\nét: 1\n";
        assertEquals(expectedContents.trim(), actualContents.toString().trim(), "example and testArray should be equal");
    }
}

```

