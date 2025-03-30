// ConnectFourMove.java
package org.example.gamehaven.games.connect4;

public class ConnectFourMove {
    private int row;
    private int col;
    private char player;

    public ConnectFourMove(int row, int col, char player) {
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