package TicTacToe;

import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.stage.Stage;


public class TicTacToeClient2 extends Application implements TicTacToeConstants {

    // Immutable class to represent the state of the game
    public static final class GameState {
        private final boolean myTurn;
        private final char myToken;
        private final char otherToken;
        private final boolean continueToPlay;
        private final boolean waiting;
        private final int rowSelected;
        private final int columnSelected;
        private final Cell[][] cell;

        public GameState(boolean myTurn, char myToken, char otherToken,
                         boolean continueToPlay, boolean waiting,
                         int rowSelected, int columnSelected, Cell[][] cell) {
            this.myTurn = myTurn;
            this.myToken = myToken;
            this.otherToken = otherToken;
            this.continueToPlay = continueToPlay;
            this.waiting = waiting;
            this.rowSelected = rowSelected;
            this.columnSelected = columnSelected;
            this.cell = cell;
        }

        // Copy constructor to create new state
        public GameState withUpdatedTurn(boolean newTurn) {
            return new GameState(newTurn, myToken, otherToken, continueToPlay, waiting, rowSelected, columnSelected, cell);
        }

        public GameState withUpdatedCell(int row, int col, char token) {
            Cell[][] newCells = copyCells(cell);
            newCells[row][col].setToken(token);
            return new GameState(myTurn, myToken, otherToken, continueToPlay, waiting, row, col, newCells);
        }

        public GameState withWaiting(boolean waiting) {
            return new GameState(myTurn, myToken, otherToken, continueToPlay, waiting, rowSelected, columnSelected, cell);
        }

        private static Cell[][] copyCells(Cell[][] originalCells) {
            Cell[][] newCells = new Cell[originalCells.length][originalCells[0].length];
            for (int i = 0; i < originalCells.length; i++) {
                for (int j = 0; j < originalCells[i].length; j++) {
                    newCells[i][j] = new Cell(originalCells[i][j].getRow(), originalCells[i][j].getColumn(), originalCells[i][j].getToken());
                }
            }
            return newCells;
        }
    }

    private final DataInputStream fromServer;
    private final DataOutputStream toServer;
    private final String host = "localhost";

    public TicTacToeClient2() throws IOException {
        Socket socket = new Socket(host, 8000);
        this.fromServer = new DataInputStream(socket.getInputStream());
        this.toServer = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void start(Stage primaryStage) {
        GameState initialState = new GameState(false, ' ', ' ', true, true, -1, -1, new Cell[3][3]);
        GridPane pane = setupUI(initialState);

        BorderPane borderPane = new BorderPane();
        Label lblTitle = new Label();
        Label lblStatus = new Label();

        borderPane.setTop(lblTitle);
        borderPane.setCenter(pane);
        borderPane.setBottom(lblStatus);

        Scene scene = new Scene(borderPane, 320, 350);
        primaryStage.setTitle("TicTacToeClient");
        primaryStage.setScene(scene);
        primaryStage.show();

        connectToServer(initialState, lblTitle, lblStatus);
    }

    private GridPane setupUI(GameState initialState) {
        GridPane pane = new GridPane();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                pane.add(initialState.cell[i][j] = new Cell(i, j), j, i);
            }
        }
        return pane;
    }

    private void connectToServer(GameState state, Label lblTitle, Label lblStatus) {
        new Thread(() -> {
            try {
                int player = fromServer.readInt();
                GameState newState = initializePlayer(state, player, lblTitle, lblStatus);

                // Continue to play
                while (newState.continueToPlay) {
                    if (player == PLAYER1) {
                        newState = waitForPlayerAction(newState);
                        sendMove(newState);
                        newState = receiveInfoFromServer(newState, lblStatus);
                    }
                    if (player == PLAYER2) {
                        newState = receiveInfoFromServer(newState, lblStatus);
                        newState = waitForPlayerAction(newState);
                        sendMove(newState);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private GameState initializePlayer(GameState state, int player, Label lblTitle, Label lblStatus) throws IOException {
        if (player == PLAYER1) {
            Platform.runLater(() -> {
                lblTitle.setText("Player 1 with token 'X'");
                lblStatus.setText("Waiting for player 2 to join");
            });
            fromServer.readInt();

            Platform.runLater(() -> lblStatus.setText("Player 2 has joined. I start first"));
            return state.withUpdatedTurn(true).withUpdatedCell(-1, -1, 'X');
        }

        if (player == PLAYER2) {
            Platform.runLater(() -> {
                lblTitle.setText("Player 2 with token 'O'");
                lblStatus.setText("Waiting for player 1 to move");
            });
            return state.withUpdatedCell(-1, -1, 'O');
        }

        return state;
    }

    private GameState waitForPlayerAction(GameState state) throws InterruptedException {
        while (state.waiting) {
            Thread.sleep(100);
        }
        return state.withWaiting(true);
    }

    private void sendMove(GameState state) throws IOException {
        toServer.writeInt(state.rowSelected);
        toServer.writeInt(state.columnSelected);
    }

    private GameState receiveInfoFromServer(GameState state, Label lblStatus) throws IOException {
        int status = fromServer.readInt();
        return handleGameStatus(state, status, lblStatus);
    }

    private GameState handleGameStatus(GameState state, int status, Label lblStatus) throws IOException {
        if (status == PLAYER1_WON) {
            return handlePlayerWon(state, lblStatus, 'X', "Player 1 has won!");
        }

        if (status == PLAYER2_WON) {
            return handlePlayerWon(state, lblStatus, 'O', "Player 2 has won!");
        }

        if (status == DRAW) {
            Platform.runLater(() -> lblStatus.setText("Game is over, no winner!"));
            return state.withWaiting(false);
        }

        return state.withUpdatedTurn(true);
    }

    private GameState handlePlayerWon(GameState state, Label lblStatus, char token, String message) throws IOException {
        Platform.runLater(() -> lblStatus.setText(state.myToken == token ? "I won!" : message));
        return state.withWaiting(false).withUpdatedTurn(false);
    }

    public static void main(String[] args) {
        launch(args);
    }

    // The inner Cell class remains largely the same but should also be immutable
    public static final class Cell extends Pane {
        private final int row;
        private final int column;
        private final char token;

        public Cell(int row, int column) {
            this(row, column, ' ');
        }

        public Cell(int row, int column, char token) {
            this.row = row;
            this.column = column;
            this.token = token;
            setPrefSize(2000, 2000);
            setStyle("-fx-border-color: black");
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        public char getToken() {
            return token;
        }

        public void setToken(char token) {
            this.repaint(token);
        }

        protected void repaint(char token) {
            if (token == 'X') {
                Line line1 = new Line(10, 10, this.getWidth() - 10, this.getHeight() - 10);
                Line line2 = new Line(10, this.getHeight() - 10, this.getWidth() - 10, 10);

                line1.setStroke(Color.RED);
                line2.setStroke(Color.RED);

                this.getChildren().addAll(line1, line2);
            } else if (token == 'O') {
                Ellipse ellipse = new Ellipse(this.getWidth() / 2, this.getHeight() / 2,
                        this.getWidth() / 2 - 10, this.getHeight() / 2 - 10);
                ellipse.setStroke(Color.BLUE);
                ellipse.setFill(Color.TRANSPARENT);

                this.getChildren().add(ellipse);
            }
        }
    }
}

