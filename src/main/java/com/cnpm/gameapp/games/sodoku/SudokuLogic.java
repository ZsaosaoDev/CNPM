package com.cnpm.gameapp.games.sodoku;

public class SudokuLogic {
    private SudokuPuzzle puzzle;

    public void initializeGame(SudokuPuzzleType type) {
        puzzle = new SudokuGenerator().generateRandomSudoku(type);
    }

    public boolean makeMove(int row, int col, String value) {
        return puzzle.makeMove(row, col, value, true);
    }

    public boolean isValidMove(int row, int col, String value) {
        return puzzle.isValidMove(row, col, value);
    }

    public boolean isSlotMutable(int row, int col) {
        return puzzle.isSlotMutable(row, col);
    }

    public String getValue(int row, int col) {
        return puzzle.getValue(row, col);
    }

    public String[] getValidValues() {
        return puzzle.getValidValues();
    }

    public boolean isBoardFull() {
        return puzzle.boardFull();
    }

    public boolean isValidSolution() {
        return puzzle.isValidSolution();
    }

    public int getSize() {
        return puzzle.getNumRows();
    }
}