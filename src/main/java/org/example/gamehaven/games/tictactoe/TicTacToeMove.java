package org.example.gamehaven.games.tictactoe;

public class TicTacToeMove {
    private int row;
    private int col;
    private char player;

    public TicTacToeMove(int row, int col, char player) {
        this.row = row;
        this.col = col;
        this.player = player;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public char getPlayer() {
        return player;
    }
}