package com.cnpm.gameapp.games.minesweeper;

import java.util.Random;

public class MinesweeperLogic {
    private final int SIZE;
    private boolean[][] mines;
    private boolean[][] revealed;
    private boolean[][] flagged;

    public MinesweeperLogic(int size) {
        this.SIZE = size;
        mines = new boolean[SIZE][SIZE];
        revealed = new boolean[SIZE][SIZE];
        flagged = new boolean[SIZE][SIZE];
    }

    public void initializeGame(int minesCount) {
        mines = new boolean[SIZE][SIZE];
        revealed = new boolean[SIZE][SIZE];
        flagged = new boolean[SIZE][SIZE];

        Random random = new Random();
        int placed = 0;
        while (placed < minesCount) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            if (!mines[row][col]) {
                mines[row][col] = true;
                placed++;
            }
        }
    }

    public boolean handleMove(int row, int col) {
        if (revealed[row][col] || flagged[row][col]) {
            return true;
        }

        revealed[row][col] = true;
        if (mines[row][col]) {
            return false; // Game over
        }

        if (getNeighboringMines(row, col) == 0) {
            revealSurroundingCells(row, col);
        }
        return true;
    }

    public void toggleFlag(int row, int col) {
        if (!revealed[row][col]) {
            flagged[row][col] = !flagged[row][col];
        }
    }

    public boolean isFlagged(int row, int col) {
        return flagged[row][col];
    }

    public boolean isMine(int row, int col) {
        return mines[row][col];
    }

    public boolean isRevealed(int row, int col) {
        return revealed[row][col];
    }

    public int getNeighboringMines(int row, int col) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int r = row + dr;
                int c = col + dc;
                if (r >= 0 && r < SIZE && c >= 0 && c < SIZE && mines[r][c]) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean checkWin() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (!revealed[row][col] && !mines[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void revealSurroundingCells(int row, int col) {
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int r = row + dr;
                int c = col + dc;
                if (r >= 0 && r < SIZE && c >= 0 && c < SIZE && !revealed[r][c] && !flagged[r][c]) {
                    revealed[r][c] = true;
                    if (getNeighboringMines(r, c) == 0) {
                        revealSurroundingCells(r, c); // Recursive call for zero-mine neighbors
                    }
                }
            }
        }
    }
}