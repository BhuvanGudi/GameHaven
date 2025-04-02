package org.example.gamehaven.games.connect4;

public class ConnectFourGame {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private final char[][] board;
    private char currentPlayer;
    private boolean gameOver;
    private final int[] lastMove;

    public ConnectFourGame() {
        board = new char[ROWS][COLS];
        currentPlayer = 'R';
        gameOver = false;
        lastMove = new int[]{-1, -1};
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public boolean isGameOver() {
        return gameOver || checkWin() || isBoardFull();
    }

    public void makeMove(int col) {
        if (gameOver || col < 0 || col >= COLS || board[0][col] != ' ') {
            return;
        }

        int row = ROWS - 1;
        while (row >= 0 && board[row][col] != ' ') {
            row--;
        }

        if (row >= 0) {
            board[row][col] = currentPlayer;
            lastMove[0] = row;
            lastMove[1] = col;
            if (checkWin()) {
                gameOver = true;
            } else if (isBoardFull()) {
                gameOver = true;
            } else {
                currentPlayer = (currentPlayer == 'R') ? 'Y' : 'R';
            }
        }
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean checkWin() {
        if (lastMove[0] == -1) return false;
        int row = lastMove[0], col = lastMove[1];
        char player = board[row][col];
        return checkDirection(row, col, 1, 0, player) || checkDirection(row, col, 0, 1, player) ||
                checkDirection(row, col, 1, 1, player) || checkDirection(row, col, 1, -1, player);
    }

    private boolean checkDirection(int row, int col, int dRow, int dCol, char player) {
        int count = 1;
        count += countInDirection(row, col, dRow, dCol, player);
        count += countInDirection(row, col, -dRow, -dCol, player);
        return count >= 4;
    }

    private int countInDirection(int row, int col, int dRow, int dCol, char player) {
        int count = 0;
        int r = row + dRow, c = col + dCol;
        while (r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c] == player) {
            count++;
            r += dRow;
            c += dCol;
        }
        return count;
    }

    public boolean isBoardFull() {
        for (int j = 0; j < COLS; j++) {
            if (board[0][j] == ' ') return false;
        }
        return true;
    }

    public char[][] getBoard() {
        return board;
    }

    public void setCurrentPlayer(char player) {
        currentPlayer = player;
    }
}