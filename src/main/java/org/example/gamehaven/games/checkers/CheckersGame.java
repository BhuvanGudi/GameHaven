package org.example.gamehaven.games.checkers;

public class CheckersGame {
    private static final int BOARD_SIZE = 8;
    private Piece[][] board;
    private Piece.PieceColor currentPlayer;
    private String gameId;

    public CheckersGame() {
        this.board = new Piece[BOARD_SIZE][BOARD_SIZE];
        this.currentPlayer = Piece.PieceColor.RED;
        initializeBoard();
    }

    private void initializeBoard() {
        // Set up red pieces (top side)
        for (int row = 0; row < 3; row++) {
            for (int col = (row + 1) % 2; col < BOARD_SIZE; col += 2) {
                board[row][col] = new Piece(Piece.PieceColor.RED, row, col);
            }
        }

        // Set up black pieces (bottom side)
        for (int row = 5; row < BOARD_SIZE; row++) {
            for (int col = (row + 1) % 2; col < BOARD_SIZE; col += 2) {
                board[row][col] = new Piece(Piece.PieceColor.BLACK, row, col);
            }
        }
    }

    public String getGameId() {
        return gameId;
    }

    public boolean isValidMove(Piece piece, int toRow, int toCol) {
        if (piece == null || piece.getColor() != currentPlayer) {
            return false;
        }

        int rowDiff = toRow - piece.getRow();
        int colDiff = toCol - piece.getCol();

        // Basic move validation
        if (Math.abs(rowDiff) == 1 && Math.abs(colDiff) == 1) {
            return board[toRow][toCol] == null;
        }

        if (Math.abs(rowDiff) == 2 && Math.abs(colDiff) == 2) {
            int midRow = (piece.getRow() + toRow) / 2;
            int midCol = (piece.getCol() + toCol) / 2;
            Piece midPiece = board[midRow][midCol];
            return midPiece != null && midPiece.getColor() != piece.getColor() && board[toRow][toCol] == null;
        }

        return false;
    }

    public void makeMove(Piece piece, int toRow, int toCol) {
        int rowDiff = toRow - piece.getRow();
        int colDiff = toCol - piece.getCol();

        if (Math.abs(rowDiff) == 2 && Math.abs(colDiff) == 2) {
            int midRow = (piece.getRow() + toRow) / 2;
            int midCol = (piece.getCol() + toCol) / 2;
            board[midRow][midCol] = null;
        }

        board[piece.getRow()][piece.getCol()] = null;
        piece.setPosition(toRow, toCol);
        board[toRow][toCol] = piece;

        if ((piece.getColor() == Piece.PieceColor.RED && toRow == BOARD_SIZE - 1) ||
                (piece.getColor() == Piece.PieceColor.BLACK && toRow == 0)) {
            piece.promoteToKing();
        }

        currentPlayer = (currentPlayer == Piece.PieceColor.RED) ?
                Piece.PieceColor.BLACK : Piece.PieceColor.RED;
    }

    public Piece.PieceColor getCurrentPlayer() {
        return currentPlayer;
    }

    public Piece getPieceAt(int row, int col) {
        if (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE) {
            return board[row][col];
        }
        return null;
    }
}