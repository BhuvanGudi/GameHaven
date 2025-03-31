package org.example.gamehaven.games.tictactoe;

import java.util.Random;

public class TicTacToeAI {
    private Random random;

    public TicTacToeAI() {
        random = new Random();
    }

    public int[] makeMove(char[][] board) {
        // First check if AI can win in the next move
        int[] winningMove = findWinningMove(board, 'O');
        if (winningMove != null) return winningMove;

        // Then check if player can win in the next move to block them
        int[] blockingMove = findWinningMove(board, 'X');
        if (blockingMove != null) return blockingMove;

        // Otherwise make a random move
        return makeRandomMove(board);
    }

    private int[] findWinningMove(char[][] board, char player) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = player;
                    boolean isWin = checkWin(board, player);
                    board[i][j] = ' '; // undo move
                    if (isWin) {
                        return new int[]{i, j};
                    }
                }
            }
        }
        return null;
    }

    private boolean checkWin(char[][] board, char player) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == player && board[1][j] == player && board[2][j] == player) {
                return true;
            }
        }

        // Check diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }

        return false;
    }

    private int[] makeRandomMove(char[][] board) {
        int emptySpots = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    emptySpots++;
                }
            }
        }

        if (emptySpots == 0) return null;

        int move = random.nextInt(emptySpots);
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    if (count == move) {
                        return new int[]{i, j};
                    }
                    count++;
                }
            }
        }
        return null;
    }
}