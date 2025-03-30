package org.example.gamehaven.games.connect4;

public class ConnectFourAI {
    public int makeMove(ConnectFourGame game) {
        char[][] board = game.getBoard();

        for (int col = 0; col < 8; col++) {
            if (board[0][col] != ' ') continue;

            for (int row = 7; row >= 0; row--) {
                if (board[row][col] == ' ') {
                    return col;
                }
            }
        }
        return -1;
    }
}