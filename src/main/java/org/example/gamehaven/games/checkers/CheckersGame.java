package org.example.gamehaven.games.checkers;

public class CheckersGame {
    private static final int BOARD_SIZE = 8;
    private final Piece[][] board;
    private Piece.PieceColor currentPlayer;
    private String gameId;

    public CheckersGame() {
        this.board = new Piece[BOARD_SIZE][BOARD_SIZE];
        this.currentPlayer = Piece.PieceColor.WHITE;
        initializeBoard();
    }

    private void initializeBoard() {
        // Set up white pieces (top side)
        for (int row = 0; row < 3; row++) {
            for (int col = (row + 1) % 2; col < BOARD_SIZE; col += 2) {
                board[row][col] = new Piece(Piece.PieceColor.WHITE, row, col);
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
        if (piece == null || piece.getColor() != currentPlayer) return false;

        int rowDiff = toRow - piece.getRow();
        int colDiff = toCol - piece.getCol();

        // Normal pieces can only move forward
        if (!piece.isKing()) {
            if (piece.getColor() == Piece.PieceColor.WHITE && rowDiff <= 0) return false;
            if (piece.getColor() == Piece.PieceColor.BLACK && rowDiff >= 0) return false;
        }

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

        if (Math.abs(rowDiff) == 2) {
            int midRow = (piece.getRow() + toRow) / 2;
            int midCol = (piece.getCol() + toCol) / 2;
            board[midRow][midCol] = null;
        }

        board[piece.getRow()][piece.getCol()] = null;
        piece.setPosition(toRow, toCol);
        board[toRow][toCol] = piece;

        if ((piece.getColor() == Piece.PieceColor.WHITE && toRow == BOARD_SIZE - 1) ||
                (piece.getColor() == Piece.PieceColor.BLACK && toRow == 0)) {
            piece.promoteToKing();
        }

        currentPlayer = (currentPlayer == Piece.PieceColor.WHITE) ? Piece.PieceColor.BLACK : Piece.PieceColor.WHITE;
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

    public boolean isGameOver() {
        int whitePieces = 0;
        int blackPieces = 0;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = board[row][col];
                if (piece != null) {
                    if (piece.getColor() == Piece.PieceColor.WHITE) whitePieces++;
                    else blackPieces++;
                }
            }
        }
        return whitePieces == 0 || blackPieces == 0 || !hasValidMoves(currentPlayer);
    }

    public Piece.PieceColor getWinner() {
        if (!(hasValidMoves(Piece.PieceColor.WHITE))) return Piece.PieceColor.BLACK;
        if (!(hasValidMoves(Piece.PieceColor.BLACK))) return Piece.PieceColor.WHITE;
        return null;
    }

    private boolean hasValidMoves(Piece.PieceColor color) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.getColor() == color) {
                    for (int r = 0; r < BOARD_SIZE; r++) {
                        for (int c = 0; c < BOARD_SIZE; c++) {
                            if (isValidMove(piece, r, c)) return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}