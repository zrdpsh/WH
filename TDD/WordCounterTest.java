import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class WordCounterTest {

    @Test
    public void testWordCounterCreation() {
        WordCounter wc = new WordCounter();
        assertDoesNotThrow(() -> new WordCounter(), "Class was created without any errors");

//        assertNotNull(car, "Car object should not be null after creation.");
    }

    @Test
    public void getTheTextTest() {
        WordCounter wc = new WordCounter();
        String filePath = "C:\\Users\\Admin\\IdeaProjects\\WH\\TDD\\testFile.txt";


        try {
            String content = wc.getTheText(filePath);

            assertNotNull(content, "File content should not be null.");
            assertFalse(content.isEmpty(), "File content should not be empty.");

            String expectedContent = Files.readString(Paths.get(filePath));
            assertEquals(expectedContent, content, "File content should match expected content.");
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

        String[]  tmp = new String[]{"projet", "de", "60", "milliards", "de", "euros", "de", "économies"};
        List<String> testArray = new ArrayList<>(Arrays.asList(tmp));
        Map<String, Integer>  testMap = wc.countStrings(testArray);
        Map<String, Integer> example = Map.of(
                "projet", 1,
                "de", 3,
                "60", 1,
                "euros", 1,
                "économies", 1
        );
        assertEquals(example, testArray, "example and testArray should be equal");
    }

    @Test
    public void countDigraphsTest() {
        WordCounter wc = new WordCounter();
        String[]  tmp = new String[]{"pr", "ro", "oj", "je", "et", "pr", "ro", "oj", "pr"};
        List<String> testArray = new ArrayList<>(Arrays.asList(tmp));
        Map<String, Integer> testMap= wc.countStrings(testArray);
        Map<String, Integer> example = Map.of(
                "pr", 3,
                "ro", 2,
                "oj", 2,
                "je", 1,
                "et", 1
        );
        assertEquals(example, testArray, "example and testArray should be equal");
    }

    @Test
    public void writeCountedToFileTest() {
        WordCounter wc = new WordCounter();
        Map<String, Integer> mapUnderTest = Map.of(
                "et", 1,
                "pr", 3,
                "ro", 2
        );
        wc.writeCountedToFile(mapUnderTest);

        String expectedContents = "pr: 3\nro: 2\net: 1\n"; // Adjust according to your expected output

        // Read the actual contents of the file
        StringBuilder actualContents = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("result.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                actualContents.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals(expectedContents.trim(), actualContents.toString().trim(), "File contents do not match.");
    }
}



//    @Test
//    public void testOperationsWithRandom() {
//        Random classRandom = new Random();
//        double numberRandom = classRandom.nextDouble() * 10000;
//        BankAccount newBA = new BankAccount(numberRandom);
//        assertEquals(newBA.getBalance(), numberRandom);
//        newBA.deposit(BIGGER);
//        newBA.withdraw(SMALLER);
//        assertEquals(newBA.getBalance(), numberRandom + BIGGER - SMALLER);
//
//    }
//
//    @Test
//    public void lessThanZero() {
//        assertDoesNotThrow( () -> smallerAccount.withdraw(SMALLER+1));
//        assertFalse(smallerAccount.getBalance() < 0);
//    }
//
//    @Test
//    public void addNegativeSum() {
//        assertDoesNotThrow( () -> smallerAccount.deposit(-(SMALLER+1)));
//        assertFalse(smallerAccount.getBalance() < 0);
//    }
//
//    @Test
//    public void takeNegativeSum() {
//        assertDoesNotThrow( () -> smallerAccount.withdraw(-(SMALLER+1)));
//        assertFalse(smallerAccount.getBalance() < 0);
//    }
//
//    @Test
//    public void testMakeSmallerDeposit() {
//        assertDoesNotThrow(() -> new BankAccount(SMALLER));
//    }
//
//    @Test
//    public void testGettingSmallerBalance() {
//        assertDoesNotThrow( () -> smallerAccount.getBalance());
//        assertEquals(smallerAccount.getBalance(), SMALLER);
//    }
//
//    @Test
//    public void testPutIntoSmallerDeposit() {
//        assertDoesNotThrow( () -> smallerAccount.deposit(10));
//        assertEquals(smallerAccount.getBalance(), 20);
//    }
//
//    @Test
//    public void testTakeFromSmallerDeposit() {
//        assertDoesNotThrow( () -> smallerAccount.withdraw(10));
//        assertEquals(smallerAccount.getBalance(), 0);
//    }
//
//    @Test
//    public void testMakeBiggerDeposit() {
//        assertDoesNotThrow(() -> new BankAccount(BIGGER));
//    }
//
//
//    @Test
//    public void testPutIntoBiggerDeposit() {
//        assertDoesNotThrow( () -> biggerAccount.deposit(10));
//        assertEquals(biggerAccount.getBalance(), BIGGER + 10);
//    }
//
//    @Test
//    public void testTakeFromBiggerDeposit() {
//        assertDoesNotThrow( () -> biggerAccount.withdraw(10));
//        assertEquals(biggerAccount.getBalance(), BIGGER - 10);
//    }
