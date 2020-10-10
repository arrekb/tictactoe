package tictactoe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final Board board = new Board();
    private static final Player xPlayer = new Player('X');
    private static final Player oPlayer = new Player('O');

    public static void main(String[] args) {
        // write your code here
        while (!"exit".equals(readCommand())) {
            board.clear();
            board.printCells();
            while (board.gameStatus() == gameStatusEnum.GAME_NOT_FINISHED) {
                getCurrentPlayer().makeMove(board);
                board.printCells();
            }
            System.out.println(board.gameStatus().getPrintStatus());
        }
    }

    public static String readCommand() {
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.print("Input command: ");
            String[] buf = scanner.nextLine().split("\\s");
            if ("exit".equals(buf[0])) {
                return "exit";
            }
            if (buf.length != 3) {
                System.out.println("Bad parameters!");
                continue;
            }
            if (!"start".equals(buf[0])) {
                System.out.println("Bad parameters!");
                continue;
            }
            if (!buf[1].matches("user|easy|medium|hard")) {
                System.out.println("Bad parameters!");
                continue;
            }
            if (!buf[2].matches("user|easy|medium|hard")) {
                System.out.println("Bad parameters!");
                continue;
            }
            switch (buf[1]) {
                case "user":
                    xPlayer.setLevel(playerLevel.USER);
                    break;
                case "easy":
                    xPlayer.setLevel(playerLevel.EASY);
                    break;
                case "medium":
                    xPlayer.setLevel(playerLevel.MEDIUM);
                    break;
                case "hard":
                    xPlayer.setLevel(playerLevel.HARD);
                    break;
            }
            switch (buf[2]) {
                case "user":
                    oPlayer.setLevel(playerLevel.USER);
                    break;
                case "easy":
                    oPlayer.setLevel(playerLevel.EASY);
                    break;
                case "medium":
                    oPlayer.setLevel(playerLevel.MEDIUM);
                    break;
                case "hard":
                    oPlayer.setLevel(playerLevel.HARD);
                    break;
            }
            break; // commandIsCorrect
        } while (true);
        return "";
    }

    public static Player getCurrentPlayer() {
        return board.getSymbolOfNextTurnPlayer() == 'X' ? xPlayer : oPlayer;
    }
}

class Coordinates {
    private int x;
    private int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates(int posInGameState) {
        switch (posInGameState) {
            case 0: {
                this.x = 1;
                this.y = 3;
                break;
            }
            case 1: {
                this.x = 2;
                this.y = 3;
                break;
            }
            case 2: {
                this.x = 3;
                this.y = 3;
                break;
            }
            case 3: {
                this.x = 1;
                this.y = 2;
                break;
            }
            case 4: {
                this.x = 2;
                this.y = 2;
                break;
            }
            case 5: {
                this.x = 3;
                this.y = 2;
                break;
            }
            case 6: {
                this.x = 1;
                this.y = 1;
                break;
            }
            case 7: {
                this.x = 2;
                this.y = 1;
                break;
            }
            case 8: {
                this.x = 3;
                this.y = 1;
                break;
            }
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean areCorrect() {
        return x > 0 && x < 4 && y > 0 && y < 4;
    }
}

class Board {

    private char[] boardState;

    public Board() {
        clear();
    }

    public Board(char[] boardState) {
        this.boardState = boardState.clone();
    }

    public char[] getBoardState() {
        return boardState;
    }

    public void clear() {
        this.boardState = "_________".toCharArray();
    }

    private int getCellPositionInBoardState(Coordinates coordinates) {
        int posInBoardState = 0;

        switch (coordinates.getY()) {
            case 1: {
                posInBoardState = 6;
                break;
            }
            case 2: {
                posInBoardState = 3;
                break;
            }
            case 3: {
                posInBoardState = 0;
                break;
            }
        }
        posInBoardState += coordinates.getX() - 1;
        return posInBoardState;
    }


    private char getCellValue(Coordinates coordinates) {
        char buf = boardState[getCellPositionInBoardState(coordinates)];
        return (buf == '_') ? ' ' : buf;
    }

    public boolean isCellOccupied(Coordinates coordinates) {
        char buf = boardState[getCellPositionInBoardState(coordinates)];
        return buf != '_';
    }

    public void printCells() {
        System.out.println("---------");
        for (int j = 3; j > 0; j--) {
            System.out.print("|");
            for (int i = 1; i < 4; i++) {
                System.out.print(" " + getCellValue(new Coordinates(i, j)));
            }
            System.out.println(" |");
        }
        System.out.println("---------");
    }

    public gameStatusEnum givenGameStatus(char[] givenBoardFields) {
        //Possible states:
        //
        //"Game not finished" - when no side has a three in a row but the field has empty cells;
        //"Draw" - when no side has a three in a row and the field has no empty cells;
        //"X wins" - when the field has three X in a row;
        //"O wins" - when the field has three O in a row;

        int[][] win_condition_array =
                {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

        for (int[] arr : win_condition_array) {
            if (givenBoardFields[arr[0]] == 'X' && givenBoardFields[arr[1]] == 'X' && givenBoardFields[arr[2]] == 'X') {
                return gameStatusEnum.X_WINS;
            }
            if (givenBoardFields[arr[0]] == 'O' && givenBoardFields[arr[1]] == 'O' && givenBoardFields[arr[2]] == 'O') {
                return gameStatusEnum.O_WINS;
            }
        }
        if (String.valueOf(givenBoardFields).contains("_")) {
            return gameStatusEnum.GAME_NOT_FINISHED;
        }
        return gameStatusEnum.DRAW;
    }

    public gameStatusEnum gameStatus() {
        return givenGameStatus(this.boardState);
    }

    public char getSymbolOfNextTurnPlayer() {
        int xCounter = 0;
        int oCounter = 0;
        for (char c : boardState) {
            switch (c) {
                case 'X': {
                    xCounter++;
                    break;
                }
                case 'O': {
                    oCounter++;
                    break;
                }
            }
        }
        return (xCounter <= oCounter) ? 'X' : 'O';
    }

    public void updateCell(Coordinates coordinates, char symbol) {
        boardState[getCellPositionInBoardState(coordinates)] = symbol;
    }
}

class Player {
    private final char symbol;
    private playerLevel level;

    Player(char symbol) {
        this.symbol = symbol;
    }

    public void setLevel(playerLevel level) {
        this.level = level;
    }

    private char getOpponentSymbol() {
        return symbol == 'X' ? 'O' : 'X';
    }

    public void makeMove(Board board) {
        Coordinates coordinates;
        switch (level) {
            case USER:
                coordinates = readUserCoordinates(board);
                break;
            case EASY:
                System.out.println("Making move level \"" + level.getLevelName() + "\"");
                coordinates = generateEasyCoordinates(board);
                break;
            case MEDIUM:
                System.out.println("Making move level \"" + level.getLevelName() + "\"");
                coordinates = generateMediumCoordinates(board);
                break;
            case HARD:
                System.out.println("Making move level \"" + level.getLevelName() + "\"");
                coordinates = generateHardCoordinates(board);
                break;
            default:
                coordinates = new Coordinates(0, 0);
        }
        board.updateCell(coordinates, symbol);
    }

    public Coordinates readUserCoordinates(Board board) {
        Coordinates coordinates;

        do {
            System.out.println("Enter the coordinates:");
            Scanner scanner = new Scanner(System.in);
            String[] buf = scanner.nextLine().split("\\s");
            if (buf.length != 2) {
                System.out.println("You should enter numbers!");
                continue;
            }
            if (!buf[0].matches("\\d+") || !buf[1].matches("\\d+")) {
                System.out.println("You should enter numbers!");
                continue;
            }
            coordinates = new Coordinates(Integer.parseInt(buf[0]), Integer.parseInt(buf[1]));
            if (!coordinates.areCorrect()) {
                System.out.println("Coordinates should be from 1 to 3!");
                continue;
            }
            if (board.isCellOccupied(coordinates)) {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            }
            break;
        } while (true);
        return coordinates;
    }

    public Coordinates generateEasyCoordinates(Board board) {
        Coordinates coordinates = new Coordinates(0, 0);
        Random random = new Random();

        do {
            coordinates.setX(random.nextInt(3) + 1);
            coordinates.setY(random.nextInt(3) + 1);
        } while (board.isCellOccupied(coordinates));

        return coordinates;
    }

    public Coordinates generateMediumCoordinates(Board board) {
        //If it can win in one move (if it has two in a row), it places a third to get three in a row and win.
        //If the opponent can win in one move, it plays the third itself to block the opponent to win.
        //Otherwise, it makes a random move.

        Coordinates coordinates = new Coordinates(0, 0);
        boolean coordinatesAreCorrect = false;

        for (int i = 0; i < board.getBoardState().length; i++) {
            if (board.getBoardState()[i] == '_') {
                char[] gameStateToCheck;
                gameStateToCheck = Arrays.copyOf(board.getBoardState(), board.getBoardState().length);
                gameStateToCheck[i] = this.symbol;
                gameStatusEnum status = board.givenGameStatus(gameStateToCheck);
                if (status == gameStatusEnum.X_WINS || status == gameStatusEnum.O_WINS) {
                    coordinatesAreCorrect = true;
                    coordinates = new Coordinates(i);
                    break;
                }
            }
        }
        if (!coordinatesAreCorrect) {
            coordinates = generateEasyCoordinates(board);
        }
        return coordinates;
    }

    public Coordinates generateHardCoordinates(Board board) {
        Coordinates bestCoordinates = new Coordinates(0, 0);
        int bestScore = Integer.MIN_VALUE;

        for (int x = 1; x < 4; x++) {
            for (int y = 1; y < 4; y++) {
                Coordinates coordinates = new Coordinates(x, y);
                if (!board.isCellOccupied(coordinates)) {
                    int score = miniMax(board, coordinates, true);
                    if (score > bestScore) {
                        bestScore = score;
                        bestCoordinates = coordinates;
                    }
                }
            }
        }
        return bestCoordinates;
    }

    private int miniMax(Board board, Coordinates coordinates, boolean isMaximizing) {
        Board checkedBoard = new Board(board.getBoardState());
        char playerSymbol = isMaximizing ? this.symbol : this.getOpponentSymbol();
        checkedBoard.updateCell(coordinates, playerSymbol);
        if (checkedBoard.gameStatus() == gameStatusEnum.DRAW) {
            return 0;
        }
        // current user wins
        if ((checkedBoard.gameStatus() == gameStatusEnum.X_WINS && this.symbol == 'X') ||
                (checkedBoard.gameStatus() == gameStatusEnum.O_WINS && this.symbol == 'O')) {
            return 1;
        }
        // opponent wins
        if ((checkedBoard.gameStatus() == gameStatusEnum.O_WINS && this.symbol == 'X') ||
                (checkedBoard.gameStatus() == gameStatusEnum.X_WINS && this.symbol == 'O')) {
            return -1;
        }

        int bestScore;
        if (isMaximizing) {
            bestScore = Integer.MAX_VALUE;
            for (int x = 1; x < 4; x++) {
                for (int y = 1; y < 4; y++) {
                    Coordinates checkedCoordinates = new Coordinates(x, y);
                    if (!checkedBoard.isCellOccupied(checkedCoordinates)) {
                        int score = miniMax(checkedBoard, checkedCoordinates, false);
                        bestScore = Math.min(bestScore, score);
                    }
                }
            }
        } else {
            bestScore = Integer.MIN_VALUE;
            for (int x = 1; x < 4; x++) {
                for (int y = 1; y < 4; y++) {
                    Coordinates checkedCoordinates = new Coordinates(x, y);
                    if (!checkedBoard.isCellOccupied(checkedCoordinates)) {
                        int score = miniMax(checkedBoard, checkedCoordinates, true);
                        bestScore = Math.max(bestScore, score);
                    }
                }
            }
        }
        return bestScore;
    }
}

enum playerLevel {
    USER("user"),
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard");

    private final String levelName;

    playerLevel(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelName() {
        return levelName;
    }
}

enum gameStatusEnum {
    GAME_NOT_FINISHED("Game not finished"),
    DRAW("Draw"),
    X_WINS("X wins"),
    O_WINS("O wins");

    private final String printStatus;

    gameStatusEnum(String printStatus) {
        this.printStatus = printStatus;
    }

    public String getPrintStatus() {
        return printStatus;
    }
}