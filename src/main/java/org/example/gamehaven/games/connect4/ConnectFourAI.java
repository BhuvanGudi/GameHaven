package org.example.gamehaven.games.connect4;

public class ConnectFourAI {
    private static final int DEPTH = 5; // Adjust for difficulty

    public int makeMove(ConnectFourGame game) {
        char[][] board = game.getBoard();
        char aiPlayer = 'Y'; // AI is always 'Y'
        char humanPlayer = 'R';

        // Check for immediate win/loss
        int winningMove = findWinningMove(board, aiPlayer);
        if (winningMove != -1) return winningMove;

        int blockingMove = findWinningMove(board, humanPlayer);
        if (blockingMove != -1) return blockingMove;

        // Use Minimax for the best move
        return minimax(board, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, true, aiPlayer, humanPlayer).getCol();
    }

    private int findWinningMove(char[][] board, char player) {
        for (int col = 0; col < 7; col++) {
            if (board[0][col] != ' ') continue;
            int row = getNextEmptyRow(board, col);
            if (row == -1) continue;

            board[row][col] = player;
            boolean wins = checkWin(board, row, col, player);
            board[row][col] = ' ';

            if (wins) return col;
        }
        return -1;
    }

    private boolean checkWin(char[][] board, int row, int col, char player) {
        return checkDirection(board, row, col, 1, 0, player) || checkDirection(board, row, col, 0, 1, player) ||
                checkDirection(board, row, col, 1, 1, player) || checkDirection(board, row, col, 1, -1, player);
    }

    private boolean checkDirection(char[][] board, int row, int col, int dRow, int dCol, char player) {
        int count = 1;
        count += countInDirection(board, row, col, dRow, dCol, player);
        count += countInDirection(board, row, col, -dRow, -dCol, player);
        return count >= 4;
    }

    private int countInDirection(char[][] board, int row, int col, int dRow, int dCol, char player) {
        int count = 0;
        int r = row + dRow, c = col + dCol;
        while (r >= 0 && r < 6 && c >= 0 && c < 7 && board[r][c] == player) {
            count++;
            r += dRow;
            c += dCol;
        }
        return count;
    }

    private Move minimax(char[][] board, int depth, int alpha, int beta, boolean maximizing, char aiPlayer, char humanPlayer) {
        if (depth == 0 || isTerminal(board)) {
            return new Move(-1, evaluate(board, aiPlayer, humanPlayer));
        }

        int bestCol = -1;
        int bestScore = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int col = 0; col < 7; col++) {
            if (board[0][col] != ' ') continue;
            int row = getNextEmptyRow(board, col);
            if (row == -1) continue;

            board[row][col] = maximizing ? aiPlayer : humanPlayer;
            int score = minimax(board, depth - 1, alpha, beta, !maximizing, aiPlayer, humanPlayer).getScore();
            board[row][col] = ' ';

            if (maximizing && score > bestScore) {
                bestScore = score;
                bestCol = col;
                alpha = Math.max(alpha, bestScore);
            } else if (!maximizing && score < bestScore) {
                bestScore = score;
                bestCol = col;
                beta = Math.min(beta, bestScore);
            }

            if (beta <= alpha) break;
        }
        return new Move(bestCol, bestScore);
    }

    private boolean isTerminal(char[][] board) {
        return checkWin(board, 'R') || checkWin(board, 'Y') || isBoardFull(board);
    }

    private boolean checkWin(char[][] board, char player) {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                if (board[row][col] == player && checkWin(board, row, col, player)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isBoardFull(char[][] board) {
        for (int col = 0; col < 7; col++) {
            if (board[0][col] == ' ') return false;
        }
        return true;
    }

    private int evaluate(char[][] board, char aiPlayer, char humanPlayer) {
        if (checkWin(board, aiPlayer)) return 1000;
        if (checkWin(board, humanPlayer)) return -1000;
        return 0; // Simple evaluation (can be enhanced)
    }

    private int getNextEmptyRow(char[][] board, int col) {
        for (int row = 5; row >= 0; row--) {
            if (board[row][col] == ' ') return row;
        }
        return -1;
    }

    private static class Move {
        private final int col;
        private final int score;

        public Move(int col, int score) {
            this.col = col;
            this.score = score;
        }

        public int getCol() {
            return col;
        }

        public int getScore() {
            return score;
        }
    }
}