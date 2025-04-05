package org.example.gamehaven.games.checkers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CheckersAI {
    private final Random random = new Random();

    public int[] makeMove(CheckersGame game) {
        List<Piece> pieces = new ArrayList<>();
        List<int[]> moves = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = game.getPieceAt(row, col);
                if (piece != null && piece.getColor() == Piece.PieceColor.BLACK) {
                    for (int r = 0; r < 8; r++) {
                        for (int c = 0; c < 8; c++) {
                            if (game.isValidMove(piece, r, c)) {
                                pieces.add(piece);
                                moves.add(new int[]{row, col, r, c});
                            }
                        }
                    }
                }
            }
        }

        if (!moves.isEmpty()) {
            int index = random.nextInt(moves.size());
            return moves.get(index);
        }
        return null;
    }
}