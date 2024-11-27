import org.junit.jupiter.api.Test;
import org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class TicTacToeGameTests {


    @Test
    void xWinsEvokingTest() {
        TicTacToeGame ttcMock = mock(TicTacToeGame.class);
        ttcMock.xWins(1, 2, 12);
        verify(ttcMock).xWins(1, 2, 3);

    }

    @Test
    void oWinsEvokingTest() {
        TicTacToeGame ttcMock = mock(TicTacToeGame.class);
        ttcMock.oWins(1, 2, 12);
        verify(ttcMock).oWins(1, 2, 3);

    }
}


//    @Test
//    void xWinsEvokingTest() {
//        TicTacToeGame ttc = new TicTacToeGame();
//        assertAll(
//                () ->
//                        assertEquals("E",
//                                decoder.decodePhrase(".")),
//                () ->
//                        assertEquals("HE",
//                                decoder.decodePhrase(".... . ")),
//                () ->
//                        assertEquals("JUDE",
//                                decoder.decodePhrase(".--- ..- -.. .")),
//                () ->
//                        assertEquals("HEY JUDE",
//                                decoder.decodePhrase(".... . -.--   .--- ..- -.. ."))
//        );
//    }
//}

