package org.example.gamehaven.games.checkers;

import javafx.scene.paint.Color;

public class Piece {
    public enum PieceColor {
        WHITE(Color.WHITE),
        BLACK(Color.BLACK);

        private final Color fxColor;

        PieceColor(Color fxColor) {
            this.fxColor = fxColor;
        }

        public Color getFxColor() {
            return fxColor;
        }
    }

    private final PieceColor color;
    private int row;
    private int col;
    private boolean isKing;

    public Piece(PieceColor color, int row, int col) {
        this.color = color;
        this.row = row;
        this.col = col;
        this.isKing = false;
    }

    public PieceColor getColor() {
        return color;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean isKing() {
        return isKing;
    }

    public void promoteToKing() {
        isKing = true;
    }
}