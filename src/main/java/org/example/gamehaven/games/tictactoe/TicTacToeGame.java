package org.example.gamehaven.games.tictactoe;

public class TicTacToeGame {
    private char[][] board;
    private char currentPlayer;
    private boolean gameOver;
    private String gameId;

    public TicTacToeGame() {
        board = new char[3][3];
        currentPlayer = 'X';
        gameOver = false;
        gameId = String.valueOf(System.currentTimeMillis());
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public boolean isGameOver() {
        return gameOver || checkWin() || isBoardFull();
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public void makeMove(int row, int col) {
        if (row < 0 || row >= 3 || col < 0 || col >= 3 || board[row][col] != ' ') {
            return;
        }

        board[row][col] = currentPlayer;

        if (!checkWin()) {
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        } else {
            gameOver = true;
        }
    }

    public String getGameId() {
        return gameId;
    }

    public boolean checkWin() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (board[0][j] != ' ' && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                return true;
            }
        }

        // Check diagonals
        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return true;
        }
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return true;
        }

        return false;
    }

    public boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }
}