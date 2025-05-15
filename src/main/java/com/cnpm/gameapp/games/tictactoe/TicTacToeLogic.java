package com.cnpm.gameapp.games.tictactoe;

import javax.swing.*;

public class TicTacToeLogic {
    private static final int SIZE = 20;
    private final JButton[][] cells;
    private boolean xTurn = true;

    public TicTacToeLogic(JButton[][] cells) {
        this.cells = cells;
    }

    public boolean isValidMove(int row, int col) {
        return cells[row][col].getText().isEmpty();
    }

    public void setXTurn(boolean xTurn) {
        this.xTurn = xTurn;
    }

    public boolean isXTurn() {
        return xTurn;
    }

    public void toggleTurn() {
        xTurn = !xTurn;
    }

    public void setBoardEnabled(boolean enabled) {
        for (JButton[] row : cells) {
            for (JButton btn : row) {
                btn.setEnabled(enabled);
            }
        }
    }

    public boolean isBoardFull() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (cells[row][col].getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkWin(int row, int col, String mark) {
        return checkDir(row, col, mark, 1, 0) ||
                checkDir(row, col, mark, 0, 1) ||
                checkDir(row, col, mark, 1, 1) ||
                checkDir(row, col, mark, 1, -1);
    }

    private boolean checkDir(int row, int col, String mark, int dx, int dy) {
        int count = 1;
        count += countDirection(row, col, mark, dx, dy);
        count += countDirection(row, col, mark, -dx, -dy);
        return count >= 5;
    }

    private int countDirection(int row, int col, String mark, int dx, int dy) {
        int count = 0;
        for (int step = 1; step < 5; step++) {
            int r = row + dx * step;
            int c = col + dy * step;
            if (r < 0 || c < 0 || r >= SIZE || c >= SIZE) break;
            if (mark.equals(cells[r][c].getText())) count++;
            else break;
        }
        return count;
    }

    public void resetBoard() {
        for (JButton[] row : cells) {
            for (JButton btn : row) {
                btn.setText("");
                btn.setEnabled(true);
            }
        }
        xTurn = true;
    }
}