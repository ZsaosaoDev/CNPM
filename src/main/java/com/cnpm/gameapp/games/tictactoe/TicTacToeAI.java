package com.cnpm.gameapp.games.tictactoe;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class TicTacToeAI {
    private static final int SIZE = 20;
    private final JButton[][] cells;
    private final TicTacToeLogic logic;

    public TicTacToeAI(JButton[][] cells, TicTacToeLogic logic) {
        this.cells = cells;
        this.logic = logic;
    }

    public void computerMove(BiConsumer<Integer, Integer> callback) {
        // Check for immediate win
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (cells[row][col].getText().isEmpty()) {
                    cells[row][col].setText("⭕");
                    if (logic.checkWin(row, col, "⭕")) {
                        cells[row][col].setText("");
                        callback.accept(row, col);
                        return;
                    }
                    cells[row][col].setText("");
                }
            }
        }

        // Check for immediate block
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (cells[row][col].getText().isEmpty()) {
                    cells[row][col].setText("❌");
                    if (logic.checkWin(row, col, "❌")) {
                        cells[row][col].setText("");
                        callback.accept(row, col);
                        return;
                    }
                    cells[row][col].setText("");
                }
            }
        }

        // Use Minimax
        int[] bestMove = minimax(4);
        callback.accept(bestMove[0], bestMove[1]);
    }

    private int[] minimax(int depthLimit) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[]{-1, -1};
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        List<int[]> possibleMoves = getPossibleMoves();
        if (possibleMoves.isEmpty()) {
            return new int[]{SIZE / 2, SIZE / 2};
        }

        for (int[] move : possibleMoves) {
            int row = move[0];
            int col = move[1];
            cells[row][col].setText("⭕");
            int score = minimaxScore(depthLimit - 1, false, alpha, beta, row, col);
            cells[row][col].setText("");
            if (score > bestScore) {
                bestScore = score;
                bestMove[0] = row;
                bestMove[1] = col;
            }
            alpha = Math.max(alpha, bestScore);
            if (beta <= alpha) break;
        }

        if (bestMove[0] == -1 || bestMove[1] == -1) {
            bestMove = possibleMoves.get(new Random().nextInt(possibleMoves.size()));
        }

        return bestMove;
    }

    private int minimaxScore(int depth, boolean isMaximizingPlayer, int alpha, int beta, int lastRow, int lastCol) {
        if (lastRow >= 0 && lastCol >= 0 && !cells[lastRow][lastCol].getText().isEmpty()) {
            String mark = cells[lastRow][lastCol].getText();
            if (logic.checkWin(lastRow, lastCol, mark)) {
                return mark.equals("⭕") ? 10000 - depth : -10000 + depth;
            }
        }

        if (logic.isBoardFull() || depth == 0) {
            return evaluateBoard();
        }

        List<int[]> possibleMoves = getPossibleMoves();
        if (isMaximizingPlayer) {
            int maxScore = Integer.MIN_VALUE;
            for (int[] move : possibleMoves) {
                int row = move[0];
                int col = move[1];
                cells[row][col].setText("⭕");
                int score = minimaxScore(depth - 1, false, alpha, beta, row, col);
                cells[row][col].setText("");
                maxScore = Math.max(maxScore, score);
                alpha = Math.max(alpha, maxScore);
                if (beta <= alpha) break;
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (int[] move : possibleMoves) {
                int row = move[0];
                int col = move[1];
                cells[row][col].setText("❌");
                int score = minimaxScore(depth - 1, true, alpha, beta, row, col);
                cells[row][col].setText("");
                minScore = Math.min(minScore, score);
                beta = Math.min(beta, minScore);
                if (beta <= alpha) break;
            }
            return minScore;
        }
    }

    private int evaluateBoard() {
        int score = 0;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (!cells[row][col].getText().isEmpty()) {
                    String mark = cells[row][col].getText();
                    boolean isComputer = mark.equals("⭕");
                    score += evaluateDirection(row, col, mark, 1, 0, isComputer);
                    score += evaluateDirection(row, col, mark, 0, 1, isComputer);
                    score += evaluateDirection(row, col, mark, 1, 1, isComputer);
                    score += evaluateDirection(row, col, mark, 1, -1, isComputer);
                }
            }
        }
        return score;
    }

    private int evaluateDirection(int row, int col, String mark, int dx, int dy, boolean isComputer) {
        int count = 0;
        int openEnds = 0;
        int i;

        for (i = 1; i < 5; i++) {
            int r = row + dx * i;
            int c = col + dy * i;
            if (r < 0 || c < 0 || r >= SIZE || c >= SIZE || !cells[r][c].getText().equals(mark)) {
                if (r >= 0 && c >= 0 && r < SIZE && c < SIZE && cells[r][c].getText().isEmpty()) {
                    openEnds++;
                }
                break;
            }
            count++;
        }

        for (i = 1; i < 5; i++) {
            int r = row - dx * i;
            int c = col - dy * i;
            if (r < 0 || c < 0 || r >= SIZE || c >= SIZE || !cells[r][c].getText().equals(mark)) {
                if (r >= 0 && c >= 0 && r < SIZE && c < SIZE && cells[r][c].getText().isEmpty()) {
                    openEnds++;
                }
                break;
            }
            count++;
        }

        int score = 0;
        if (count >= 4) score = isComputer ? 5000 : -5000;
        else if (count == 3 && openEnds > 0) score = isComputer ? 100 : -100;
        else if (count == 2 && openEnds > 0) score = isComputer ? 10 : -10;
        else if (count == 1 && openEnds > 0) score = isComputer ? 1 : -1;
        return score;
    }

    private List<int[]> getPossibleMoves() {
        List<int[]> moves = new ArrayList<>();
        boolean hasAdjacent = false;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (cells[row][col].getText().isEmpty()) {
                    cells[row][col].setText("⭕");
                    if (logic.checkWin(row, col, "⭕")) {
                        cells[row][col].setText("");
                        moves.add(new int[]{row, col});
                        return moves;
                    }
                    cells[row][col].setText("");

                    cells[row][col].setText("❌");
                    if (logic.checkWin(row, col, "❌")) {
                        cells[row][col].setText("");
                        moves.add(new int[]{row, col});
                        continue;
                    }
                    cells[row][col].setText("");

                    if (hasNeighbor(row, col)) {
                        moves.add(new int[]{row, col});
                        hasAdjacent = true;
                    }
                }
            }
        }

        if (hasAdjacent) {
            return moves;
        }

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (cells[row][col].getText().isEmpty()) {
                    moves.add(new int[]{row, col});
                }
            }
        }

        return moves;
    }

    private boolean hasNeighbor(int row, int col) {
        for (int dr = -2; dr <= 2; dr++) {
            for (int dc = -2; dc <= 2; dc++) {
                if (dr == 0 && dc == 0) continue;
                int nr = row + dr;
                int nc = col + dc;
                if (nr >= 0 && nr < SIZE && nc >= 0 && nc < SIZE && !cells[nr][nc].getText().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }
}
